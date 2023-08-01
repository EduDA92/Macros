package com.example.macrostracker.data.repository

import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.model.UserData
import com.example.macrostracker.data.util.enums.WeightEnum
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /* Stream of [UserData] */
    val userData: Flow<UserData>

    /* Updates Weight value */
    suspend fun setWeight(weight: Double)

    /* Updates Height value */
    suspend fun setHeight(height: Double)

    /* Updates Age value */
    suspend fun setAge(age: Int)

    /* Updates Gender value */
    suspend fun setGender(gender: GenderEnum)

    /* Updates Weight Units */
    suspend fun setWeightUnits( weightUnits: WeightEnum)

    /* Updates Height Units */
    suspend fun setHeightUnits( heightUnits: HeightEnum)

    /* Updates Activity level */
    suspend fun setActivityLevel(activityLevel: ActivityLevelEnum)

    /* Updates Objective */
    suspend fun setObjective(objective: ObjectiveEnum)

    /* Flag to change the flag to check if its the first time opening the app */
    suspend fun setFirstTime(firstTime: Boolean)
}