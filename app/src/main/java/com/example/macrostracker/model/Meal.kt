package com.example.macrostracker.model

import com.example.macrostracker.data.entity.MealsEntity

data class Meal (
    val id: Long = 0,
    val name: String,
    val showMeal: Boolean
)

fun Meal.asEntity() = MealsEntity(
    id = id,
    name = name,
    showMeal = showMeal
)