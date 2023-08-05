package com.example.macrostracker.model

import com.example.macrostracker.data.entity.FoodEntity

data class Food(
    val id: Long = 0,
    val name: String,
    val brand: String,
    val calories: Int,
    val fat: Double,
    val carbs: Double,
    val protein: Double,
    val servingSize: Int
){

    fun doesMatchQuery(query: String): Boolean{
        val matchingCombinations = listOf(
            name,
            brand
        )
        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }

}

fun Food.asEntity() = FoodEntity(
    id = id,
    name = name,
    brand = brand,
    calories = calories,
    fat = fat,
    carbs = carbs,
    protein = protein,
    servingSize = servingSize
)


