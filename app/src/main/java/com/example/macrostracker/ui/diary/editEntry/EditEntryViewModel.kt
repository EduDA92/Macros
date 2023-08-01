package com.example.macrostracker.ui.diary.editEntry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.EntryRepository
import com.example.macrostracker.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val foodRepository: FoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val food = foodRepository.getFood(savedStateHandle["foodId"]!!).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    val entryWithFood = entryRepository.getEntry(savedStateHandle["entryId"]!!).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun updateEntryServingSize(id: Long, servingSize: Int) {
        viewModelScope.launch {
            entryRepository.updateEntryServingSize(id, servingSize)
        }
    }

}