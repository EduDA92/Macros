package com.example.macrostracker.model

import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.data.util.enums.WeightEnum

data class UserData(
    val weight: Double,
    val height: Double,
    val age: Int,
    val gender: GenderEnum,
    val weightUnits: WeightEnum,
    val heightUnits: HeightEnum,
    val objective: ObjectiveEnum,
    val activityLevel: ActivityLevelEnum,
    val firstTime : Boolean
)


