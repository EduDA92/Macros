package com.example.macrostracker.ui.diary.foodList.barcodeScanner


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.model.Food
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BarcodeFoodViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _barcodeFood: MutableStateFlow<BarcodeFoodUiModel> =
        MutableStateFlow(BarcodeFoodUiModel.Loading(true))
    val barcodeFood = _barcodeFood.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val result = foodRepository.getBarcodeFood(savedStateHandle["barcode"]!!)
                _barcodeFood.update {
                    BarcodeFoodUiModel.Success(result.data)
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _barcodeFood.update {
                    BarcodeFoodUiModel.Error(e.message.toString())
                }
            }

        }
    }

    fun insertFood(food: Food){
        viewModelScope.launch {
            foodRepository.insertFood(food)
        }
    }

}

sealed class BarcodeFoodUiModel {
    data class Loading(val isLoading: Boolean) : BarcodeFoodUiModel()
    data class Success(val food: Food?) : BarcodeFoodUiModel()
    data class Error(val error: String) : BarcodeFoodUiModel()
}

