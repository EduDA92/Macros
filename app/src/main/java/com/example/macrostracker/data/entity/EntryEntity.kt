package com.example.macrostracker.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.macrostracker.model.Entry
import java.time.LocalDate

/* Entry has 1:N relation with meals, 1 meal can have N entries, 1 entry belongs to only 1 meal
*  Entry has N:1 relation with food,  1 entry only contains 1 food, 1 food can belong to N different entries
* Entry has N:1 relation with recipes, 1 entry only contains 1 recipe, 1 recipe can belong to N different entries*/

@Entity(
    tableName = "entries",
    foreignKeys = [
        ForeignKey(
            entity = MealsEntity::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        ),
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
        Index(value = ["mealId"]),
        Index(value = ["foodId"]),
        Index(value = ["recipeId"])
    ]
)
data class EntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    @ColumnInfo(name = "mealId")
    val mealId: Long,
    @ColumnInfo(name = "foodId")
    val foodId: Long?,
    @ColumnInfo(name = "recipeId")
    val recipeId: Long?,
    @ColumnInfo(name = "servingSize")
    val servingSize: Int
)

fun EntryEntity.asExternalModel() = Entry(
    id = id,
    date = date,
    mealId = mealId,
    foodId = foodId,
    recipeId = recipeId,
    servingSize = servingSize
)
