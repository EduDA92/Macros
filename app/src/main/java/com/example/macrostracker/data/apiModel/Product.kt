package com.example.macrostracker.data.apiModel


import com.squareup.moshi.Json

data class Product(
    @Json(name = "brands")
    val brands: String,
    @Json(name = "nutriments")
    val nutriments: Nutriments,
    @Json(name = "product_name")
    val productName: String
)