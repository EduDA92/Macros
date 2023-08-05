package com.example.macrostracker.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "FoodRecipeCrossRef",
    primaryKeys = ["foodId", "recipeId"],
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["foodId"]),
        Index(value = ["recipeId"])
    ]
)
data class FoodRecipeCrossRef(
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "foodId")
    val foodId: Long,
    @ColumnInfo(name = "foodQuantity")
    val foodQuantity: Int
)
