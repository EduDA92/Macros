package com.example.macrostracker.ui.diary.foodList.createRecipeEntry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.EntryRepository
import com.example.macrostracker.data.repository.RecipeRepository
import com.example.macrostracker.model.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRecipeEntryViewModel @Inject constructor(
    recipeRepository: RecipeRepository,
    private val entryRepository: EntryRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    val recipeWithFood = recipeRepository.getRecipeFromID(savedStateHandle["recipeId"]!!).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun insertEntry(entry: Entry){
        viewModelScope.launch {
            entryRepository.insertEntry(entry)
        }
    }

}