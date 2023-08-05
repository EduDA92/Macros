package com.example.macrostracker.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.macrostracker.model.EntryWithFood

data class EntryAndFood(
    @Embedded
    val entryEntity: EntryEntity,
    @Relation(
        parentColumn = "foodId",
        entityColumn = "id"
    )
    val foodEntity: FoodEntity
)

fun EntryAndFood.asExternalModel() = EntryWithFood(
    id = entryEntity.id,
    date = entryEntity.date,
    mealId = entryEntity.mealId,
    servingSize = entryEntity.servingSize,
    food = foodEntity.asExternalModel(),
    entryCalories = calculateNutrient(foodEntity.servingSize, entryEntity.servingSize, foodEntity.calories.toDouble()).toInt(),
    entryCarbs = calculateNutrient(foodEntity.servingSize, entryEntity.servingSize, foodEntity.carbs),
    entryFat = calculateNutrient(foodEntity.servingSize, entryEntity.servingSize, foodEntity.fat),
    entryProtein = calculateNutrient(foodEntity.servingSize, entryEntity.servingSize, foodEntity.protein),
)

private fun calculateNutrient(foodServing: Int, entryServing: Int, foodNutrient: Double): Double{
    return entryServing.times(foodNutrient).div(foodServing)
}