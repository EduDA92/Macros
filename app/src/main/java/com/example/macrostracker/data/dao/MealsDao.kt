package com.example.macrostracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.macrostracker.data.entity.MealsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealsDao {

    @Query("SELECT * FROM meals")
    fun getMeals(): Flow<List<MealsEntity>>

    @Query("UPDATE meals SET showMeal = :showMeal WHERE id = :id")
    suspend fun updateMealVisibility(id: Long, showMeal: Boolean)

    @Query("UPDATE meals SET name = :mealName WHERE id = :id")
    suspend fun updateMealName(id: Long, mealName: String)

    @Insert
    suspend fun insertMeals(meals: List<MealsEntity>)

}