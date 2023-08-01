package com.example.macrostracker.data.repository

import com.example.macrostracker.data.dao.MealsDao
import com.example.macrostracker.data.entity.asExternalModel
import com.example.macrostracker.model.Meal
import com.example.macrostracker.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultMealsRepository @Inject constructor(
    private val mealsDao: MealsDao
) : MealsRepository {

    override fun getMeals(): Flow<List<Meal>> =
        mealsDao.getMeals().map {
            it.map {
                it.asExternalModel()
            }
        }

    override suspend fun updateMealVisibility(id: Long, showMeal: Boolean) =
        mealsDao.updateMealVisibility(id, showMeal)

    override suspend fun updateMealName(id: Long, mealName: String) =
        mealsDao.updateMealName(id, mealName)

    override suspend fun insertMeals(mealList: List<Meal>) =
        mealsDao.insertMeals(mealList.map {
            it.asEntity()
        })
}