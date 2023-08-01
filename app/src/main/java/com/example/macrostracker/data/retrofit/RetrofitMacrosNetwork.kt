package com.example.macrostracker.data.retrofit

import com.example.macrostracker.data.apiModel.FoodApiModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton


private const val BASE_URL = "https://world.openfoodfacts.net/api/v2/product/"

val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

/**
 * Retrofit API declaration for OpenFoodFacts API
 */
private interface RetrofitMacrosNetworkApi{

    @Headers("User-Agent: MacrosTracker App")
    @GET("{barcode}?fields=product_name,brands,nutriments")
    suspend fun getFood(
        @Path("barcode") barcode: String
    ): Response<FoodApiModel>

}

@Singleton
class RetrofitMacrosNetwork @Inject constructor(): MacrosNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(RetrofitMacrosNetworkApi::class.java)

    override suspend fun getFood(barcode: String): Response<FoodApiModel> =
        networkApi.getFood(barcode)

}