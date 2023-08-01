package com.example.macrostracker.ui.user.userFoodList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFoodListViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    val foodList = foodRepository.getAllFood().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun deleteFoodList(list: List<Long>){
        viewModelScope.launch {
            foodRepository.deleteListOfFoods(list)
        }
    }

}