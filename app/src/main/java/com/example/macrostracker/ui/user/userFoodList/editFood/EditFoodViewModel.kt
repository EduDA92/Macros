package com.example.macrostracker.ui.user.userFoodList.editFood

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val food = foodRepository.getFood(savedStateHandle["foodId"]!!).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun updateFood(food: Food){
        viewModelScope.launch {
            foodRepository.updateFood(food)
        }
    }

}