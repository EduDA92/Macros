package com.example.macrostracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.macrostracker.data.entity.FoodRecipeCrossRef
import com.example.macrostracker.data.entity.RecipeEntity
import com.example.macrostracker.data.entity.RecipeFoodAndCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id LIKE :id")
    suspend fun deleteRecipe(id: Long)

    @Query("DELETE FROM recipes WHERE id IN (:list)")
    suspend fun deleteListOfRecipes(list: List<Long>)

    @Transaction
    @Query("SELECT * FROM recipes")
    fun getRecipes(): Flow<List<RecipeFoodAndCrossRef>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id LIKE :id")
    fun getRecipeFromID(id: Long): Flow<RecipeFoodAndCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodRecipeCrossRef(foodRecipeCrossRef: FoodRecipeCrossRef)

    @Query("DELETE FROM foodrecipecrossref WHERE foodId LIKE :foodId AND recipeId LIKE :recipeId")
    suspend fun deleteIngredient(foodId: Long, recipeId: Long)

}