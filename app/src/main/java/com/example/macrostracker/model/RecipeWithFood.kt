package com.example.macrostracker.model

import com.example.macrostracker.data.entity.FoodRecipeCrossRef


data class RecipeWithFood(
    val id: Long,
    val name: String,
    val description: String,
    val foods:List<Food>,
    val crossRef: List<FoodRecipeCrossRef>,
    val servingSize: Int,
    val calories: Int,
    val fat: Double,
    val carbs: Double,
    val protein: Double
){
    fun doesMatchQuery(query: String): Boolean{
        val matchingCombinations = listOf(
            name
        )
        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }
}

