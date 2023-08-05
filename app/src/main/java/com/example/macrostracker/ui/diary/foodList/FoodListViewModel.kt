package com.example.macrostracker.ui.diary.foodList


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.data.repository.RecipeRepository
import com.example.macrostracker.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {


     private val _recipeWithFoodList = recipeRepository.getRecipes().stateIn(
         viewModelScope,
         SharingStarted.WhileSubscribed(),
         emptyList()
     )


    private val _searchText = MutableStateFlow("")

    private val _foodList: StateFlow<List<Food>> = foodRepository.getAllFood().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val recipeWithFood = _searchText.combine(_recipeWithFoodList) {text, recipeList ->
        if (text.isBlank()) {
            recipeList
        } else {
            recipeList.filter { recipe ->
                recipe.doesMatchQuery(text)
            }
        }
    }

    val foodList = _searchText.combine(_foodList) { text, foodList ->
        if (text.isBlank()) {
            foodList
        } else {
            foodList.filter { food ->
                food.doesMatchQuery(text)
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.update {
            text
        }
    }

    fun deleteFood(foodId: Long) {
        viewModelScope.launch {
            foodRepository.deleteFood(foodId)
        }
    }

    fun deleteRecipe(id: Long){
        viewModelScope.launch {
            recipeRepository.deleteRecipe(id)
        }
    }


}