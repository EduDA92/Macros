package com.example.macrostracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.macrostracker.model.Food

@Entity(
    tableName = "foods"
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String,
    val calories: Int,
    val fat: Double,
    val carbs: Double,
    val protein: Double,
    val servingSize: Int
)

fun FoodEntity.asExternalModel() = Food(
    id = id,
    name = name,
    brand = brand,
    calories = calories,
    fat = fat,
    carbs = carbs,
    protein = protein,
    servingSize = servingSize
)
