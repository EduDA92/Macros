package com.example.macrostracker.data.repository

import com.example.macrostracker.data.dao.EntryDao
import com.example.macrostracker.data.entity.FoodEntity
import com.example.macrostracker.data.entity.FoodRecipeCrossRef
import com.example.macrostracker.data.entity.asExternalModel
import com.example.macrostracker.model.Entry
import com.example.macrostracker.model.EntryWithFood
import com.example.macrostracker.model.EntryWithRecipe
import com.example.macrostracker.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DefaultEntryRepository @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {
    override suspend fun insertEntry(entry: Entry): Long =
        entryDao.insertEntry(entry.asEntity())

    override suspend fun updateEntry(entry: Entry) =
        entryDao.updateEntry(entry.asEntity())

    override suspend fun deleteEntry(id: Long) =
        entryDao.deleteEntry(id)

    override fun getEntry(id: Long): Flow<EntryWithFood> =
        entryDao.getEntry(id).map {
            it.asExternalModel()
        }

    override fun getEntriesFromDate(date: LocalDate): Flow<List<EntryWithFood>> =
        entryDao.getEntriesFromDate(date).map {
            it.map {
                it.asExternalModel()
            }
        }

    override fun getRecipeEntry(id: Long): Flow<EntryWithRecipe> =
        entryDao.getRecipeEntry(id).map {
            EntryWithRecipe(
                id = it.entry.id,
                date = it.entry.date,
                mealId = it.entry.mealId,
                servingSize = it.entry.servingSize,
                recipe = it.recipe.asExternalModel(),
                entryCalories = calculateNutrient(
                    foodServing = calculateServingSize(it.crossRef),
                    entryServing = it.entry.servingSize,
                    foodNutrient = calculateNutriment("calories", it.foods, it.crossRef)
                ).toInt(),
                entryProtein = calculateNutrient(
                    foodServing = calculateServingSize(it.crossRef),
                    entryServing = it.entry.servingSize,
                    foodNutrient = calculateNutriment("protein", it.foods, it.crossRef)
                ),
                entryFat = calculateNutrient(
                    foodServing = calculateServingSize(it.crossRef),
                    entryServing = it.entry.servingSize,
                    foodNutrient = calculateNutriment("fat", it.foods, it.crossRef)
                ),
                entryCarbs = calculateNutrient(
                    foodServing = calculateServingSize(it.crossRef),
                    entryServing = it.entry.servingSize,
                    foodNutrient = calculateNutriment("carbs", it.foods, it.crossRef)
                )

            )
        }

    /* Serving size * nutrient / crossref size */
    override fun getRecipeEntriesFromDate(date: LocalDate): Flow<List<EntryWithRecipe>> =
        entryDao.getRecipeEntriesFromDate(date).map { list ->
            list.map {
                EntryWithRecipe(
                    id = it.entry.id,
                    date = it.entry.date,
                    mealId = it.entry.mealId,
                    servingSize = it.entry.servingSize,
                    recipe = it.recipe.asExternalModel(),
                    entryCalories = calculateNutrient(
                        foodServing = calculateServingSize(it.crossRef),
                        entryServing = it.entry.servingSize,
                        foodNutrient = calculateNutriment("calories", it.foods, it.crossRef)
                    ).toInt(),
                    entryProtein = calculateNutrient(
                        foodServing = calculateServingSize(it.crossRef),
                        entryServing = it.entry.servingSize,
                        foodNutrient = calculateNutriment("protein", it.foods, it.crossRef)
                    ),
                    entryFat = calculateNutrient(
                        foodServing = calculateServingSize(it.crossRef),
                        entryServing = it.entry.servingSize,
                        foodNutrient = calculateNutriment("fat", it.foods, it.crossRef)
                    ),
                    entryCarbs = calculateNutrient(
                        foodServing = calculateServingSize(it.crossRef),
                        entryServing = it.entry.servingSize,
                        foodNutrient = calculateNutriment("carbs", it.foods, it.crossRef)
                    )

                )
            }

        }

    override suspend fun updateEntryServingSize(id: Long, servingSize: Int) =
        entryDao.updateEntryServingSize(id, servingSize)

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