package com.example.macrostracker.ui.diary.foodList.createEntry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.macrostracker.data.repository.EntryRepository
import com.example.macrostracker.data.repository.FoodRepository
import com.example.macrostracker.model.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateEntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val foodRepository: FoodRepository
) :
    ViewModel() {

    fun observeFood(foodId : Long) =
        foodRepository.getFood(foodId)

    fun insertEntry(entry: Entry) {
        viewModelScope.launch {
            entryRepository.insertEntry(entry)
        }
    }

}