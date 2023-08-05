package com.example.macrostracker.ui.user.userFoodList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFoodListViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")

    val _foodList = foodRepository.getAllFood()

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

    fun deleteFoodList(list: List<Long>){
        viewModelScope.launch {
            foodRepository.deleteListOfFoods(list)
        }
    }

}