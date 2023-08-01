package com.example.macrostracker.model

import com.example.macrostracker.data.entity.FoodEntity

data class Food(
    val id: Long = 0,
    val name: String,
    val brand: String,
    val calories: Int,
    val fat: Int,
    val carbs: Int,
    val protein: Int,
    val servingSize: Int
){

    fun doesMatchQuery(query: String): Boolean{
        return name.contains(query, ignoreCase = true)
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


