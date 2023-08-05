package com.example.macrostracker.model

import com.example.macrostracker.data.entity.RecipeEntity

data class Recipe(
    val id: Long = 0,
    val name: String,
    val description: String
)

fun Recipe.asEntity() = RecipeEntity(
    id = id,
    name = name,
    description = description
)
