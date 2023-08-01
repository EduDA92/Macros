package com.example.macrostracker.data.apiModel


import com.squareup.moshi.Json

data class Nutriments(
    @Json(name = "carbohydrates_100g")
    val carbohydrates100g: Double,
    @Json(name = "energy-kcal_100g")
    val energyKcal100g: Int,
    @Json(name = "fat_100g")
    val fat100g: Double,
    @Json(name = "proteins_100g")
    val proteins100g: Double
)