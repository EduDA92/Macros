package com.example.macrostracker.ui.diary.foodList.createFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository
): ViewModel() {

    fun insertFood(food: Food){
        viewModelScope.launch {
            foodRepository.insertFood(food)
        }
    }

}