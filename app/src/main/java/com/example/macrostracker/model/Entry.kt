package com.example.macrostracker.model

import com.example.macrostracker.data.entity.EntryEntity
import java.time.LocalDate

data class Entry(
    val id: Long = 0,
    val date: LocalDate,
    val mealId: Long,
    val foodId: Long?,
    val servingSize: Int
)

fun Entry.asEntity() = EntryEntity(
    id = id,
    date = date,
    mealId = mealId,
    foodId = foodId,
    servingSize = servingSize
)