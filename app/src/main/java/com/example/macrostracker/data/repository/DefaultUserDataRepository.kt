package com.example.macrostracker.data.repository

import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.model.UserData
import com.example.macrostracker.data.util.enums.WeightEnum
import com.example.macrostracker.data.datastore.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
):UserDataRepository {

    override val userData: Flow<UserData> =
        userPreferencesDataSource.userData

    override suspend fun setWeight(weight: Double) =
        userPreferencesDataSource.setWeight(weight)

    override suspend fun setHeight(height: Double) =
        userPreferencesDataSource.setHeight(height)

    override suspend fun setAge(age: Int) =
        userPreferencesDataSource.setAge(age)

    override suspend fun setGender(gender: GenderEnum) =
        userPreferencesDataSource.setGender(gender)

    override suspend fun setWeightUnits( weightUnits: WeightEnum) =
        userPreferencesDataSource.setWeightUnits(weightUnits)

    override suspend fun setHeightUnits( heightUnits: HeightEnum) =
        userPreferencesDataSource.setHeightUnits(heightUnits)

    override suspend fun setActivityLevel(activityLevel: ActivityLevelEnum) =
        userPreferencesDataSource.setActivityLevel(activityLevel)

    override suspend fun setObjective(objective: ObjectiveEnum) =
        userPreferencesDataSource.setObjective(objective)


    override suspend fun setFirstTime(firstTime: Boolean) =
        userPreferencesDataSource.setFirstTime(firstTime)
}