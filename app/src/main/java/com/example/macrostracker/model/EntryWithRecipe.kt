package com.example.macrostracker.model

import java.time.LocalDate

data class EntryWithRecipe(
    val id: Long,
    val date: LocalDate,
    val mealId: Long,
    val servingSize: Int,
    val recipe: Recipe,
    val entryCalories: Int,
    val entryCarbs: Double,
    val entryFat: Double,
    val entryProtein: Double,
)

