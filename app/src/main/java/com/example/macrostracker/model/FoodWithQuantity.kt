package com.example.macrostracker.model

data class FoodWithQuantity (
    val food: Food,
    val quantity: Int,
    val calories: Int,
    val fat: Double,
    val carbs: Double,
    val protein: Double
)