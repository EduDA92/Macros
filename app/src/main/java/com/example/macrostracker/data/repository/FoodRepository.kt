package com.example.macrostracker.data.repository


import com.example.macrostracker.data.apiModel.FoodApiModel
import com.example.macrostracker.data.entity.FoodEntity
import com.example.macrostracker.data.util.ApiResult
import com.example.macrostracker.model.Food
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface FoodRepository {

    suspend fun insertFood(food: Food): Long

    suspend fun updateFood(food: Food)

    suspend fun deleteFood(id : Long)

    suspend fun deleteListOfFoods(list: List<Long>)

    suspend fun getBarcodeFood(barcode: String): ApiResult<Food>

    fun getFood(foodId : Long): Flow<Food>

    fun getAllFood() : Flow<List<Food>>

}