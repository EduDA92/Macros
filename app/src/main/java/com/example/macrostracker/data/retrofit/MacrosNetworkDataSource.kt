package com.example.macrostracker.data.retrofit

import com.example.macrostracker.data.apiModel.FoodApiModel
import retrofit2.Response

interface MacrosNetworkDataSource {
    suspend fun getFood(barcode: String): Response<FoodApiModel>
}