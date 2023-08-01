package com.example.macrostracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.macrostracker.data.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert
    suspend fun insertFood(food: FoodEntity): Long

    @Update
    suspend fun updateFood(food: FoodEntity)

    @Query("DELETE FROM foods WHERE id LIKE :id")
    suspend fun deleteFood(id: Long)

    @Query("DELETE FROM foods WHERE id IN (:list)")
    suspend fun deleteListOfFoods(list: List<Long>)

    @Query("SELECT * FROM foods WHERE id like :foodId")
    fun getFood(foodId: Long): Flow<FoodEntity>

    @Query("SELECT * FROM foods")
    fun getAllFood(): Flow<List<FoodEntity>>
}