package com.example.macrostracker.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecipeFoodAndCrossRef(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = FoodRecipeCrossRef::class,
            parentColumn = "recipeId",
            entityColumn = "foodId"
        )
    )
    val foods: List<FoodEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val crossRef: List<FoodRecipeCrossRef>

)



