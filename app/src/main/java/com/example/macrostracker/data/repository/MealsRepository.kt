package com.example.macrostracker.data.repository

import com.example.macrostracker.model.Meal
import kotlinx.coroutines.flow.Flow

interface MealsRepository {

    /* Return flow with the list of Meals */
    fun getMeals(): Flow<List<Meal>>

    /* Update Meal visibility */
    suspend fun updateMealVisibility(id: Long, showMeal: Boolean)

    /* Update Meal name */
    suspend fun updateMealName(id: Long, mealName: String)

    /* Used to insert Meals when first time opening the app */
    suspend fun insertMeals(mealList: List<Meal>)
}