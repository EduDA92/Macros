package com.example.macrostracker.data.repository


import com.example.macrostracker.data.entity.FoodRecipeCrossRef
import com.example.macrostracker.model.Recipe
import com.example.macrostracker.model.RecipeWithFood
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun insertRecipe(recipe: Recipe): Long

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun deleteRecipe(id: Long)

    suspend fun deleteListOfRecipes(list: List<Long>)

    fun getRecipes(): Flow<List<RecipeWithFood>>

    fun getRecipeFromID(id: Long): Flow<RecipeWithFood>

    suspend fun insertFoodRecipeCrossRef(foodRecipeCrossRef: FoodRecipeCrossRef)

    suspend fun deleteIngredient(foodId: Long, recipeId: Long)
}