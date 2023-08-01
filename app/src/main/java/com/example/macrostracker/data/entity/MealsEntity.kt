package com.example.macrostracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.macrostracker.model.Meal

/* This defines the meals that every diary entry have, like the diary entry the meals can't be deleted
* only its name can be modified, this table will be prepopulated with a preset list of meals */

@Entity(
    tableName = "meals"
)
data class MealsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val showMeal: Boolean
)

fun MealsEntity.asExternalModel() = Meal(
    id = id,
    name = name,
    showMeal = showMeal
)




