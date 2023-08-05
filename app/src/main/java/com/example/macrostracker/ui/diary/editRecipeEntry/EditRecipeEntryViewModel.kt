package com.example.macrostracker.ui.diary.editRecipeEntry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.EntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeEntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val entryWithRecipe = entryRepository.getRecipeEntry(savedStateHandle["entryId"]!!).stateIn(
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