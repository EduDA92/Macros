package com.example.macrostracker.data.datastore

import androidx.datastore.core.DataStore
import com.example.macrostracker.UserPreferences
import com.example.macrostracker.UserPreferences.Gender
import com.example.macrostracker.UserPreferences.HeightUnits
import com.example.macrostracker.UserPreferences.WeightUnits
import com.example.macrostracker.UserPreferences.ActivityLevel
import com.example.macrostracker.UserPreferences.Objective
import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.model.UserData
import com.example.macrostracker.data.util.enums.WeightEnum
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {

    val userData = userPreferences.data.map {
        UserData(
            weight = it.weight,
            height = it.height,
            age = it.age,
            gender = when (it.gender) {
                Gender.MALE, Gender.UNRECOGNIZED, null -> GenderEnum.MALE
                Gender.FEMALE -> GenderEnum.FEMALE
            },
            weightUnits = when (it.weightUnits) {
                WeightUnits.KG, WeightUnits.UNRECOGNIZED, null -> WeightEnum.KG
                WeightUnits.LB -> WeightEnum.LB
            },
            heightUnits = when (it.heightUnits) {
                HeightUnits.M, HeightUnits.UNRECOGNIZED, null -> HeightEnum.M
                HeightUnits.FT -> HeightEnum.FT
            },
            activityLevel = when (it.activityLevel) {
                ActivityLevel.SEDENTARY, ActivityLevel.UNRECOGNIZED, null -> ActivityLevelEnum.SEDENTARY
                ActivityLevel.LIGHTLY_ACTIVE -> ActivityLevelEnum.LIGHTLY_ACTIVE
                ActivityLevel.MODERATELY_ACTIVE -> ActivityLevelEnum.MODERATELY_ACTIVE
                ActivityLevel.ACTIVE -> ActivityLevelEnum.ACTIVE
                ActivityLevel.VERY_ACTIVE -> ActivityLevelEnum.VERY_ACTIVE
            },
            objective = when (it.objective) {
                Objective.LOSE_WEIGHT -> ObjectiveEnum.LOSE_WEIGHT
                Objective.MAINTAIN_WEIGHT, Objective.UNRECOGNIZED, null -> ObjectiveEnum.MAINTAIN_WEIGHT
                Objective.GAIN_WEIGHT -> ObjectiveEnum.GAIN_WEIGHT
            },
            firstTime = it.firstTime
        )
    }


    suspend fun setWeight(weight: Double) {
        userPreferences.updateData {
            it.toBuilder()
                .setWeight(weight)
                .build()
        }
    }

    suspend fun setHeight(height: Double) {
        userPreferences.updateData {
            it.toBuilder()
                .setHeight(height)
                .build()
        }
    }

    suspend fun setAge(age: Int) {
        userPreferences.updateData {
            it.toBuilder()
                .setAge(age)
                .build()
        }
    }

    suspend fun setGender(gender: GenderEnum) {
        userPreferences.updateData {
            it.toBuilder()
                .setGender(
                    when (gender) {
                        GenderEnum.MALE -> Gender.MALE
                        GenderEnum.FEMALE -> Gender.FEMALE
                    }
                )
                .build()
        }
    }

    suspend fun setWeightUnits(weightUnits: WeightEnum) {
        userPreferences.updateData {
            it.toBuilder()
                .setWeightUnits(
                    when (weightUnits) {
                        WeightEnum.KG -> WeightUnits.KG
                        WeightEnum.LB -> WeightUnits.LB
                    }
                )
                .build()
        }
    }

    suspend fun setHeightUnits(heightUnits: HeightEnum) {
        userPreferences.updateData {
            it.toBuilder()
                .setHeightUnits(
                    when (heightUnits) {
                        HeightEnum.M -> HeightUnits.M
                        HeightEnum.FT -> HeightUnits.FT
                    }
                )
                .build()
        }
    }

    suspend fun setActivityLevel(activityLevel: ActivityLevelEnum){
        userPreferences.updateData {
            it.toBuilder()
                .setActivityLevel(
                    when(activityLevel){
                        ActivityLevelEnum.SEDENTARY -> ActivityLevel.SEDENTARY
                        ActivityLevelEnum.LIGHTLY_ACTIVE -> ActivityLevel.LIGHTLY_ACTIVE
                        ActivityLevelEnum.MODERATELY_ACTIVE -> ActivityLevel.MODERATELY_ACTIVE
                        ActivityLevelEnum.ACTIVE -> ActivityLevel.ACTIVE
                        ActivityLevelEnum.VERY_ACTIVE -> ActivityLevel.VERY_ACTIVE
                    }
                )
                .build()
        }
    }

    suspend fun setObjective(objective: ObjectiveEnum){
        userPreferences.updateData {
            it.toBuilder()
                .setObjective(
                    when(objective){
                        ObjectiveEnum.LOSE_WEIGHT -> Objective.LOSE_WEIGHT
                        ObjectiveEnum.MAINTAIN_WEIGHT -> Objective.MAINTAIN_WEIGHT
                        ObjectiveEnum.GAIN_WEIGHT -> Objective.GAIN_WEIGHT
                    }
                )
                .build()
        }
    }

    suspend fun setFirstTime(firstTime: Boolean){
        userPreferences.updateData {
            it.toBuilder()
                .setFirstTime(firstTime)
                .build()
        }
    }


}