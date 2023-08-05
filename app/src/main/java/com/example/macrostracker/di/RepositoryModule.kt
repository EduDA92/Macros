package com.example.macrostracker.di

import com.example.macrostracker.data.repository.DefaultEntryRepository
import com.example.macrostracker.data.repository.DefaultFoodRepository
import com.example.macrostracker.data.repository.DefaultUserDataRepository
import com.example.macrostracker.data.repository.DefaultMealsRepository
import com.example.macrostracker.data.repository.DefaultRecipeRepository
import com.example.macrostracker.data.repository.EntryRepository
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.data.repository.MealsRepository
import com.example.macrostracker.data.repository.RecipeRepository
import com.example.macrostracker.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: DefaultUserDataRepository
    ): UserDataRepository

    @Binds
    fun bindsMealsRepository(
        mealsRepository: DefaultMealsRepository
    ): MealsRepository

    @Binds
    fun bindsEntryRepository(
        entryRepository: DefaultEntryRepository
    ): EntryRepository

    @Binds
    fun bindsFoodRepository(
        foodRepository: DefaultFoodRepository
    ): FoodRepository

    @Binds
    fun bindsRecipeRepository(
        recipeRepository: DefaultRecipeRepository
    ): RecipeRepository
}