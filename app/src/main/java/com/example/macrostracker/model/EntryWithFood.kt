package com.example.macrostracker.model

import java.time.LocalDate

data class EntryWithFood (
    val id: Long,
    val date: LocalDate,
    val mealId: Long,
    val servingSize: Int,
    val food: Food,
    val entryCalories: Int,
    val entryCarbs: Int,
    val entryFat: Int,
    val entryProtein: Int
)