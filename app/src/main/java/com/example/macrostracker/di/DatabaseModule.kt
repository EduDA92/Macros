package com.example.macrostracker.di

import android.content.Context
import androidx.room.Room
import com.example.macrostracker.data.MacrosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMacrosDatabase(
        @ApplicationContext context: Context
    ): MacrosDatabase = Room.databaseBuilder(
        context,
        MacrosDatabase::class.java,
        "macros-database"
    ).build()

}