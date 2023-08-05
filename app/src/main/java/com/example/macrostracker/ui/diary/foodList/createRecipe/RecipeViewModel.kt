package com.example.macrostracker.ui.diary.foodList.createRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.entity.FoodRecipeCrossRef
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.data.repository.RecipeRepository
import com.example.macrostracker.model.FoodWithQuantity
import com.example.macrostracker.model.Recipe
import com.example.macrostracker.model.RecipeWithFood
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    foodRepository: FoodRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")

    private val _foodList = foodRepository.getAllFood()

    val foodList = _searchText.combine(_foodList) { text, foodList ->
        if (text.isBlank()) {
            foodList
        } else {
            foodList.filter { food ->
                food.doesMatchQuery(text)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun getRecipeFromId(id: Long) = recipeRepository.getRecipeFromID(id).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun createFoodRecipeCrossRef(recipeId: Long, foodId: Long, quantity: Int) {
        viewModelScope.launch {
            recipeRepository.insertFoodRecipeCrossRef(
                FoodRecipeCrossRef(
                    recipeId = recipeId,
                    foodId = foodId,
                    foodQuantity = quantity
                )
            )
        }
    }

    suspend fun insertRecipe(name: String, description: String): Long {

        return recipeRepository.insertRecipe(Recipe(name = name, description = description))

    }

    /* This function will create a list to pass it to the RecipeIngredientsAdapter it will pair
    * each food with its designed quantity */
    fun formatToFoodWithQuantity(recipeWithFood: RecipeWithFood): List<FoodWithQuantity> {
        val list = mutableListOf<FoodWithQuantity>()

        recipeWithFood.foods.forEach { food ->
            val quantity = recipeWithFood.crossRef.first { it.foodId == food.id }.foodQuantity
            list.add(
                FoodWithQuantity(
                    food = food,
                    quantity = quantity,
                    calories = calculateNutrient(
                        food.servingSize,
                        quantity,
                        food.calories.toDouble()
                    ).toInt(),
                    fat = calculateNutrient(food.servingSize, quantity, food.fat),
                    carbs = calculateNutrient(food.servingSize, quantity, food.carbs),
                    protein = calculateNutrient(food.servingSize, quantity, food.protein)
                )
            )
        }

        return list
    }

    fun deleteIngredient(recipeId: Long, foodId: Long) {
        viewModelScope.launch {
            recipeRepository.deleteIngredient(foodId = foodId, recipeId = recipeId)
        }
    }


    fun updateSearchText(text: String) {
        _searchText.update {
            text
        }
    }

    private fun calculateNutrient(
        foodServing: Int,
        entryServing: Int,
        foodNutrient: Double
    ): Double {
        return entryServing.times(foodNutrient).div(foodServing)
    }

}