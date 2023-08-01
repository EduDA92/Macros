package com.example.macrostracker.data.repository

import android.util.Log
import com.example.macrostracker.data.apiModel.asExternalModel
import com.example.macrostracker.data.dao.FoodDao
import com.example.macrostracker.data.entity.asExternalModel
import com.example.macrostracker.data.retrofit.MacrosNetworkDataSource
import com.example.macrostracker.data.util.ApiResult
import com.example.macrostracker.model.Food
import com.example.macrostracker.model.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DefaultFoodRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val network: MacrosNetworkDataSource
) : FoodRepository {
    override suspend fun insertFood(food: Food): Long =
        foodDao.insertFood(food.asEntity())

    override suspend fun updateFood(food: Food) =
        foodDao.updateFood(food.asEntity())

    override suspend fun deleteFood(id: Long) =
        foodDao.deleteFood(id)

    override suspend fun deleteListOfFoods(list: List<Long>) =
        foodDao.deleteListOfFoods(list)

    override fun getFood(foodId: Long): Flow<Food> =
        foodDao.getFood(foodId).map {
            it.asExternalModel()
        }

    override fun getAllFood(): Flow<List<Food>> =
        foodDao.getAllFood().map {
            it.map {
                it.asExternalModel()
            }
        }


    override suspend fun getBarcodeFood(barcode: String): ApiResult<Food> {

        val barcodeFood = withContext(Dispatchers.IO) {
            network.getFood(barcode)
        }

        return if (barcodeFood.isSuccessful) {
            ApiResult.Success(barcodeFood.body()?.asExternalModel())
        } else {
            val errorMessage = barcodeFood.errorBody()?.string()
            barcodeFood.errorBody()?.close()
            ApiResult.Error(errorMessage ?: "")
        }

    }


}