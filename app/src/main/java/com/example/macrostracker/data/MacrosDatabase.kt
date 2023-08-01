package com.example.macrostracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.macrostracker.data.dao.EntryDao
import com.example.macrostracker.data.dao.FoodDao
import com.example.macrostracker.data.dao.MealsDao
import com.example.macrostracker.data.entity.EntryEntity
import com.example.macrostracker.data.entity.FoodEntity
import com.example.macrostracker.data.entity.MealsEntity
import com.example.macrostracker.data.util.DateConverter

@Database(
    entities = [
        MealsEntity::class,
        FoodEntity::class,
        EntryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateConverter::class
)
abstract class MacrosDatabase : RoomDatabase() {

    abstract fun MealsDao(): MealsDao
    abstract fun EntryDao(): EntryDao
    abstract fun FoodDao(): FoodDao

}