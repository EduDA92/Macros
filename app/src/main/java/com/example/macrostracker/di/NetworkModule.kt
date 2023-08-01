package com.example.macrostracker.di

import com.example.macrostracker.data.retrofit.MacrosNetworkDataSource
import com.example.macrostracker.data.retrofit.RetrofitMacrosNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsMacrosNetworkDataSource(
        retrofitMacrosNetwork: RetrofitMacrosNetwork
    ): MacrosNetworkDataSource

}