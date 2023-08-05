package com.example.macrostracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.macrostracker.model.Recipe

/* Recipe has N to M relation with food */
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String
)

fun RecipeEntity.asExternalModel() = Recipe(
    id = id,
    name = name,
    description = description
)