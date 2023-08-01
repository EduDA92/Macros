package com.example.macrostracker.ui.diary.editMeals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.MealsRepository
import com.example.macrostracker.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMealsViewModel @Inject constructor(
    private val mealsRepository: MealsRepository
) : ViewModel() {


    val mealList: StateFlow<List<Meal>> = mealsRepository.getMeals().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun updateMealVisibility(id: Long, showMeal: Boolean){
        viewModelScope.launch {
            mealsRepository.updateMealVisibility(id, showMeal)
        }
    }

    fun updateMealName(id: Long, mealName: String){
        viewModelScope.launch {
            mealsRepository.updateMealName(id, mealName)
        }
    }

}