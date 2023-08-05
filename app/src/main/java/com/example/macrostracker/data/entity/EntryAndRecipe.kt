package com.example.macrostracker.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/* The parentColumn is the one that belongs to the @Embedded entity
* The entityColum belongs to the relation entity */
data class EntryAndRecipe(
    @Embedded
    val entry: EntryEntity,

    @Relation(
        parentColumn = "recipeId",
        entityColumn = "id"
    )
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "id",
        associateBy = Junction(
            value = FoodRecipeCrossRef::class,
            parentColumn = "recipeId",
            entityColumn = "foodId"
        )
    )
    val foods: List<FoodEntity>,

    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeId"
    )
    val crossRef: List<FoodRecipeCrossRef>


)
