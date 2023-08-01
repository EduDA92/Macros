package com.example.macrostracker.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.model.UserData
import com.example.macrostracker.data.util.enums.WeightEnum
import com.example.macrostracker.model.Meal
import com.example.macrostracker.data.repository.MealsRepository
import com.example.macrostracker.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val mealsRepository: MealsRepository
) : ViewModel() {

    /* Data to prepopulate de db with meals */
    private val mealList = listOf(
        Meal(id = 0,name = "Breakfast", showMeal = true),
        Meal(id = 0,name = "Lunch", showMeal = true),
        Meal(id = 0,name = "Dinner", showMeal = true),
        Meal(id = 0,name = "Snack", showMeal = true),
        Meal(id = 0,name = "Meal 5", showMeal = false),
        Meal(id = 0,name = "Meal 6", showMeal = false),
        Meal(id = 0,name = "Meal 7", showMeal = false),
        Meal(id = 0,name = "Meal 8", showMeal = false),
        Meal(id = 0,name = "Meal 9", showMeal = false),
        Meal(id = 0,name = "Meal 10", showMeal = false),
    )

    val userData = userDataRepository.userData.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(1000),
        null
    )

    fun setWeight(weight: Double) {
        viewModelScope.launch {
            userDataRepository.setWeight(weight)
        }
    }

    fun setHeight(height: Double) {
        viewModelScope.launch {
            userDataRepository.setHeight(height)
        }
    }

    fun setAge(age: Int) {
        viewModelScope.launch {
            userDataRepository.setAge(age)
        }
    }

    fun setGender(gender: GenderEnum) {
        viewModelScope.launch {
            userDataRepository.setGender(gender)
        }
    }

    fun setWeightUnits(weightUnits: WeightEnum) {
        viewModelScope.launch {
            userDataRepository.setWeightUnits(weightUnits)
        }
    }

    fun setHeightUnits(heightUnits: HeightEnum) {
        viewModelScope.launch {
            userDataRepository.setHeightUnits(heightUnits)
        }
    }

    fun setActivityLevel(activityLevel: ActivityLevelEnum) {
        viewModelScope.launch {
            userDataRepository.setActivityLevel(activityLevel)
        }
    }

    fun setObjective(objective: ObjectiveEnum) {
        viewModelScope.launch {
            userDataRepository.setObjective(objective)
        }
    }

    /* Change the first time flag to true (By default its false) */
    fun noLongerFirstTime() {
        viewModelScope.launch {
            userDataRepository.setFirstTime(true)
        }
    }

    /* populate database with preset Meal list */
    fun populateMeals(){
        viewModelScope.launch {
            mealsRepository.insertMeals(mealList)
        }
    }

    /* Using Mifflin St Jeor equation more info: https://en.wikipedia.org/wiki/Basal_metabolic_rate */
    fun calculateBMR(userData: UserData): Int {

        /* This modifiers are to convert from Kilograms to pounds in case of mass and in case
        * of height from centimeters to meters, and from centimeters to Feet */

        val lbsModifier = 2.2046
        val meterModifier = 100
        val footModifier = 0.032808
        val maleModifier = 5
        val womanModifier = 161

        when (userData.gender) {
            GenderEnum.MALE -> return when (userData.heightUnits) {
                HeightEnum.M -> when (userData.weightUnits) {
                    WeightEnum.KG -> ((10 * userData.weight) + (6.25 * userData.height * meterModifier) - (5 * userData.age) + maleModifier).toInt()
                    WeightEnum.LB -> ((10 * userData.weight / lbsModifier) + (6.25 * userData.height * meterModifier) - (5 * userData.age) + maleModifier).toInt()
                }

                HeightEnum.FT -> when (userData.weightUnits) {
                    WeightEnum.KG -> ((10 * userData.weight) + (6.25 * userData.height / footModifier) - (5 * userData.age) + maleModifier).toInt()
                    WeightEnum.LB -> ((10 * userData.weight / lbsModifier) + (6.25 * userData.height / footModifier) - (5 * userData.age) + maleModifier).toInt()
                }
            }

            GenderEnum.FEMALE -> return when (userData.heightUnits) {
                HeightEnum.M -> when (userData.weightUnits) {
                    WeightEnum.KG -> ((10 * userData.weight) + (6.25 * userData.height * meterModifier) - (5 * userData.age) - womanModifier).toInt()
                    WeightEnum.LB -> ((10 * userData.weight / lbsModifier) + (6.25 * userData.height * meterModifier) - (5 * userData.age) - womanModifier).toInt()
                }

                HeightEnum.FT -> when (userData.weightUnits) {
                    WeightEnum.KG -> ((10 * userData.weight) + (6.25 * userData.height / footModifier) - (5 * userData.age) - womanModifier).toInt()
                    WeightEnum.LB -> ((10 * userData.weight / lbsModifier) + (6.25 * userData.height / footModifier) - (5 * userData.age) - womanModifier).toInt()
                }
            }

        }

    }

    fun calculateIMC(userData: UserData): Double {

        return when (userData.heightUnits) {
            HeightEnum.M -> when (userData.weightUnits) {
                WeightEnum.KG -> (userData.weight.div(userData.height.pow(2)))
                WeightEnum.LB -> (userData.weight.div(2.2).div(userData.height.pow(2)))
            }

            HeightEnum.FT -> when (userData.weightUnits) {
                WeightEnum.KG -> (userData.weight.div(userData.height.div(3.28).pow(2)))
                WeightEnum.LB -> (userData.weight.div(2.2)
                    .div(userData.height.div(3.28).pow(2)))
            }
        }
    }
}
