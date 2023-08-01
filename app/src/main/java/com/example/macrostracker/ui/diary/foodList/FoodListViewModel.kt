package com.example.macrostracker.ui.diary.foodList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
      private val foodRepository: FoodRepository
) : ViewModel() {


    private val _searchText = MutableStateFlow("")

    private val _foodList: StateFlow<List<Food>> = foodRepository.getAllFood().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val foodList = _searchText.combine(_foodList) { text, foodList ->
        if(text.isBlank()){
            foodList
        } else {
            foodList.filter { food ->
                food.doesMatchQuery(text)
            }
        }
    }

    fun updateSearchText(text: String){
        _searchText.update {
            text
        }
    }

    fun deleteFood(foodId: Long){
        viewModelScope.launch {
            foodRepository.deleteFood(foodId)
        }
    }



}