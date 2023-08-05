package com.example.macrostracker.data.repository

import com.example.macrostracker.data.dao.RecipeDao
import com.example.macrostracker.data.entity.FoodEntity
import com.example.macrostracker.data.entity.FoodRecipeCrossRef
import com.example.macrostracker.data.entity.asExternalModel
import com.example.macrostracker.model.Recipe
import com.example.macrostracker.model.RecipeWithFood
import com.example.macrostracker.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultRecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override suspend fun insertRecipe(recipe: Recipe): Long =
        recipeDao.insertRecipe(recipe.asEntity())

    override suspend fun updateRecipe(recipe: Recipe) =
        recipeDao.updateRecipe(recipe.asEntity())

    override suspend fun deleteRecipe(id: Long) =
        recipeDao.deleteRecipe(id)

    override suspend fun deleteListOfRecipes(list: List<Long>) =
        recipeDao.deleteListOfRecipes(list)

    override fun getRecipes(): Flow<List<RecipeWithFood>> =
        recipeDao.getRecipes().map { list ->
            list.map {
                RecipeWithFood(
                    id = it.recipe.id,
                    name = it.recipe.name,
                    description = it.recipe.description,
                    foods = it.foods.map { it.asExternalModel() },
                    crossRef = it.crossRef,
                    servingSize = calculateServingSize(it.crossRef),
                    calories = calculateNutriment("calories", it.foods, it.crossRef).toInt(),
                    protein = calculateNutriment("protein", it.foods, it.crossRef),
                    fat = calculateNutriment("fat", it.foods, it.crossRef),
                    carbs = calculateNutriment("carbs", it.foods, it.crossRef)
                )
            }
        }


    override fun getRecipeFromID(id: Long): Flow<RecipeWithFood> =
        recipeDao.getRecipeFromID(id).map {
            RecipeWithFood(
                id = it.recipe.id,
                name = it.recipe.name,
                description = it.recipe.description,
                foods = it.foods.map { it.asExternalModel() },
                crossRef = it.crossRef,
                servingSize = calculateServingSize(it.crossRef),
                calories = calculateNutriment("calories", it.foods, it.crossRef).toInt(),
                protein = calculateNutriment("protein", it.foods, it.crossRef),
                fat = calculateNutriment("fat", it.foods, it.crossRef),
                carbs = calculateNutriment("carbs", it.foods, it.crossRef)
            )
        }

    override suspend fun insertFoodRecipeCrossRef(foodRecipeCrossRef: FoodRecipeCrossRef) =
        recipeDao.insertFoodRecipeCrossRef(foodRecipeCrossRef)

    override suspend fun deleteIngredient(foodId: Long, recipeId: Long) =
        recipeDao.deleteIngredient(foodId, recipeId)

    private fun calculateNutriment(
        nutriment: String,
        foods: List<FoodEntity>,
        crossRef: List<FoodRecipeCrossRef>
    ): Double {

        var totalCalories = 0.0
        var totalFats = 0.0
        var totalProtein = 0.0
        var totalCarbs = 0.0


        return when (nutriment) {
            "calories" -> {
                foods.forEach { foodEntity ->

                    /* As it will only be one food type per recipe just get the first element that matches
                    * the foodId */
                    val foodServingSize = crossRef.first { it.foodId == foodEntity.id }.foodQuantity


                    totalCalories += calculateNutrient(
                        foodEntity.servingSize,
                        foodServingSize,
                        foodEntity.calories.toDouble()
                    )

                }
                totalCalories
            }

            "protein" -> {
                foods.forEach { foodEntity ->

                    val foodServingSize = crossRef.first { it.foodId == foodEntity.id }.foodQuantity

                    totalProtein += calculateNutrient(
                        foodEntity.servingSize,
                        foodServingSize,
                        foodEntity.protein
                    )

                }
                totalProtein
            }

            "fat" -> {
                foods.forEach { foodEntity ->

                    val foodServingSize = crossRef.first { it.foodId == foodEntity.id }.foodQuantity

                    totalFats += calculateNutrient(
                        foodEntity.servingSize,
                        foodServingSize,
                        foodEntity.fat
                    )

                }
                totalFats
            }

            "carbs" -> {
                foods.forEach { foodEntity ->

                    val foodServingSize = crossRef.first { it.foodId == foodEntity.id }.foodQuantity

                    totalCarbs += calculateNutrient(
                        foodEntity.servingSize,
                        foodServingSize,
                        foodEntity.carbs
                    )

                }
                totalCarbs
            }

            else -> 0.0
        }

    }

    private fun calculateNutrient(
        foodServing: Int,
        entryServing: Int,
        foodNutrient: Double
    ): Double {
        return entryServing.times(foodNutrient).div(foodServing)
    }

    private fun calculateServingSize(crossRef: List<FoodRecipeCrossRef>): Int {
        var servingSize = 0

        crossRef.forEach {
            servingSize += it.foodQuantity
        }

        return servingSize
    }
}