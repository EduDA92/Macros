package com.example.macrostracker.data.apiModel


import com.example.macrostracker.model.Food
import com.squareup.moshi.Json


data class FoodApiModel(
    @Json(name = "code")
    val code: String,
    @Json(name = "product")
    val product: Product,
    @Json(name = "status")
    val status: Int,
    @Json(name = "status_verbose")
    val statusVerbose: String
)

fun FoodApiModel.asExternalModel() = Food(
    id = 0,
    name = product.productName,
    brand = product.brands,
    calories = product.nutriments.energyKcal100g,
    carbs = product.nutriments.carbohydrates100g,
    fat = product.nutriments.fat100g,
    protein = product.nutriments.proteins100g,
    servingSize = 100
)