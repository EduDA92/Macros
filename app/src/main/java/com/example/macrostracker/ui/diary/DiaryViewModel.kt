package com.example.macrostracker.ui.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.model.EntryWithFood
import com.example.macrostracker.model.Meal
import com.example.macrostracker.data.repository.EntryRepository
import com.example.macrostracker.data.repository.MealsRepository
import com.example.macrostracker.data.repository.UserDataRepository
import com.example.macrostracker.data.util.enums.ActivityLevelEnum
import com.example.macrostracker.data.util.enums.GenderEnum
import com.example.macrostracker.data.util.enums.HeightEnum
import com.example.macrostracker.data.util.enums.ObjectiveEnum
import com.example.macrostracker.data.util.enums.WeightEnum
import com.example.macrostracker.model.EntryWithRecipe
import com.example.macrostracker.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val mealsRepository: MealsRepository,
    private val entryRepository: EntryRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {


    /* Flow to observe the current selected date */
    private val _date = MutableStateFlow(LocalDate.now())
    val date = _date.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val diaryEntries: StateFlow<List<UiModel>?> = date.flatMapLatest {

        val mealList = mealsRepository.getMeals()
        val entryList = entryRepository.getEntriesFromDate(it)
        val recipeEntryList = entryRepository.getRecipeEntriesFromDate(it)
        val userData = userDataRepository.userData

        combine(mealList, entryList, recipeEntryList, userData) { meals, entries, recipeEntries, user ->

            val formattedDiaryEntries = mutableListOf<UiModel>()
            var entriesAdded: Boolean
            var entryCalories = 0
            var totalCalories = 0
            var entryCarbohydrate = 0.0
            var totalCarbohydrate = 0.0
            var entryProtein = 0.0
            var totalProtein = 0.0
            var entryFat = 0.0
            var totalFat = 0.0

            /* Reference macros calculation */
            val referenceCalories = calculateReferenceCalories(user)
            val referenceCarbohydrate = calculateReferenceCarbs(referenceCalories)
            val referenceFat = calculateReferenceFats(user, referenceCalories)
            val referenceProtein = calculateReferenceProtein(user, referenceCalories)


            /* The intended Structure for the List is:
            * TOTAL SUMMARY - HEADER - ENTRY - ENTRY - ADDFOODITEM - SUMMARY
            * if the entry list is empty then:
            * HEADER - ADDFOODITEM - SUMMARY*/

            meals.filter { meal -> meal.showMeal }.forEach { currentMeal ->
                // Reset the flag each time the loop starts
                entriesAdded = false

                formattedDiaryEntries.add(UiModel.TitleItem(currentMeal))

                /* Food entries */
                entries.forEach { currentEntry ->

                    if (currentEntry.mealId == currentMeal.id) {
                        formattedDiaryEntries.add(UiModel.EntryItem(currentEntry))
                        entryCalories += currentEntry.entryCalories
                        totalCalories += currentEntry.entryCalories
                        entryCarbohydrate += currentEntry.entryCarbs
                        totalCarbohydrate += currentEntry.entryCarbs
                        entryProtein += currentEntry.entryProtein
                        totalProtein += currentEntry.entryProtein
                        entryFat += currentEntry.entryFat
                        totalFat += currentEntry.entryFat
                    }
                    entriesAdded = true
                }

                /* Recipe entries */

                recipeEntries.forEach { currentRecipe ->

                    if(currentRecipe.mealId == currentMeal.id){
                        formattedDiaryEntries.add(UiModel.EntryRecipeItem(currentRecipe))
                        entryCalories += currentRecipe.entryCalories
                        totalCalories += currentRecipe.entryCalories
                        entryCarbohydrate += currentRecipe.entryCarbs
                        totalCarbohydrate += currentRecipe.entryCarbs
                        entryProtein += currentRecipe.entryProtein
                        totalProtein += currentRecipe.entryProtein
                        entryFat += currentRecipe.entryFat
                        totalFat += currentRecipe.entryFat
                    }
                    entriesAdded = true
                }

                /*If  formattedDiaryEntries.last() is UiModel.TitleItem means that there is no entries so
                * add footer after the header
                * Also if entriesAdded == true means that there are added entries so add footer after the entries*/

                if (formattedDiaryEntries.last() is UiModel.TitleItem || entriesAdded) {
                    formattedDiaryEntries.add(UiModel.AddFoodItem(currentMeal))
                    formattedDiaryEntries.add(
                        UiModel.EntrySummaryItem(
                            MacrosSummary(
                                entryCalories,
                                entryCarbohydrate,
                                entryProtein,
                                entryFat
                            )
                        )
                    )
                    entryCalories = 0
                    entryCarbohydrate = 0.0
                    entryProtein = 0.0
                    entryFat = 0.0
                }
            }

            // Add total summary item
            formattedDiaryEntries.add(
                0, UiModel.TotalSummaryItem(
                    MacrosSummary(
                        totalCalories,
                        totalCarbohydrate,
                        totalProtein,
                        totalFat
                    ),
                    MacrosSummary(
                        referenceCalories,
                        referenceCarbohydrate,
                        referenceProtein,
                        referenceFat
                    )
                )
            )
            // Return formatted list
            formattedDiaryEntries
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )


    fun nextDate() {
        _date.update {
            it.plusDays(1)
        }
    }

    fun prevDate() {
        _date.update {
            it.minusDays(1)
        }
    }

    fun deleteEntry(entryId: Long) {
        viewModelScope.launch {
            entryRepository.deleteEntry(entryId)
        }
    }

    /* Modifiers from https://www.bodybuilding.com/fun/macronutrients_calculator.htm */

    private fun calculateReferenceCalories(userData: UserData): Int {

        /* Activity modifiers */
        val sedentaryModifier = 1.2
        val lightlyActiveModifier = 1.375
        val moderatelyActiveModifier = 1.55
        val activeModifier = 1.725
        val veryActiveModifier = 1.9

        /* Objective modifiers */
        val objectiveModifier = 0.2

        val referenceCalories = calculateBMR(userData)


        when (userData.activityLevel) {
            ActivityLevelEnum.SEDENTARY -> return when (userData.objective) {
                ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(sedentaryModifier).minus(referenceCalories.times(objectiveModifier)).toInt()
                ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(sedentaryModifier).toInt()
                ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(sedentaryModifier).plus(referenceCalories.times(objectiveModifier)).toInt()
            }

            ActivityLevelEnum.LIGHTLY_ACTIVE -> return when (userData.objective) {
                ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(lightlyActiveModifier).minus(referenceCalories.times(objectiveModifier)).toInt()
                ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(lightlyActiveModifier).toInt()
                ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(lightlyActiveModifier).plus(referenceCalories.times(objectiveModifier)).toInt()
            }

            ActivityLevelEnum.MODERATELY_ACTIVE -> return when (userData.objective) {
                ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(moderatelyActiveModifier).minus(referenceCalories.times(objectiveModifier)).toInt()
                ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(moderatelyActiveModifier).toInt()
                ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(moderatelyActiveModifier).plus(referenceCalories.times(objectiveModifier)).toInt()
            }

            ActivityLevelEnum.ACTIVE -> return when (userData.objective) {
                ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(activeModifier).minus(referenceCalories.times(objectiveModifier)).toInt()
                ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(activeModifier).toInt()
                ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(activeModifier).plus(referenceCalories.times(objectiveModifier)).toInt()
            }

            ActivityLevelEnum.VERY_ACTIVE -> return when (userData.objective) {
                ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(veryActiveModifier).minus(referenceCalories.times(objectiveModifier)).toInt()
                ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(veryActiveModifier).toInt()
                ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(veryActiveModifier).plus(referenceCalories.times(objectiveModifier)).toInt()
            }
        }

    }

    /* Using Mifflin St Jeor equation more info: https://en.wikipedia.org/wiki/Basal_metabolic_rate */
    private fun calculateBMR(userData: UserData): Int {

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


    private fun calculateReferenceCarbs(referenceCalories: Int): Double{
        val carbsWorthCalories = 4
        val carbsModifier = 0.4
        return referenceCalories.times(carbsModifier).div(carbsWorthCalories)
    }

    private fun calculateReferenceProtein(userData: UserData, referenceCalories: Int): Double{

        val proteinWorthCalories = 4
        val weightLossModifier = 0.4
        val weightGainMaintainModifier = 0.3

        return when(userData.objective){
            ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(weightLossModifier).div(proteinWorthCalories)
            ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(weightGainMaintainModifier).div(proteinWorthCalories)
            ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(weightGainMaintainModifier).div(proteinWorthCalories)
        }

    }

    private fun calculateReferenceFats(userData: UserData, referenceCalories: Int): Double{

        val fatsWorthCalories = 9
        val weightLossModifier = 0.2
        val weightGainMaintainModifier = 0.3

        return when(userData.objective){
            ObjectiveEnum.LOSE_WEIGHT -> referenceCalories.times(weightLossModifier).div(fatsWorthCalories)
            ObjectiveEnum.MAINTAIN_WEIGHT -> referenceCalories.times(weightGainMaintainModifier).div(fatsWorthCalories)
            ObjectiveEnum.GAIN_WEIGHT -> referenceCalories.times(weightGainMaintainModifier).div(fatsWorthCalories)
        }

    }




}

data class MacrosSummary(
    val calories: Int,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double
)

sealed class UiModel {
    data class TotalSummaryItem(
        val totalMacros: MacrosSummary,
        val referenceMacros: MacrosSummary
    ) : UiModel()

    data class TitleItem(val meal: Meal) : UiModel()
    data class EntryItem(val entry: EntryWithFood) : UiModel()

    data class EntryRecipeItem(val entry: EntryWithRecipe): UiModel()
    data class AddFoodItem(val meal: Meal) : UiModel()
    data class EntrySummaryItem(val macros: MacrosSummary) : UiModel()
}
