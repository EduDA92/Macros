package com.example.macrostracker.di

import com.example.macrostracker.data.MacrosDatabase
import com.example.macrostracker.data.dao.EntryDao
import com.example.macrostracker.data.dao.FoodDao
import com.example.macrostracker.data.dao.MealsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesMealsDao(
        database: MacrosDatabase
    ): MealsDao = database.MealsDao()

    @Provides
    fun providesFoodDao(
        database: MacrosDatabase
    ): FoodDao = database.FoodDao()

    @Provides
    fun providesEntryDao(
        database: MacrosDatabase
    ): EntryDao = database.EntryDao()

}