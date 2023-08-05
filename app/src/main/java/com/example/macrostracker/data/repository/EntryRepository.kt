package com.example.macrostracker.data.repository

import com.example.macrostracker.model.Entry
import com.example.macrostracker.model.EntryWithFood
import com.example.macrostracker.model.EntryWithRecipe
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EntryRepository {

    suspend fun insertEntry(entry: Entry): Long

    suspend fun updateEntry(entry: Entry)

    suspend fun deleteEntry(id: Long)

    fun getEntry(id: Long): Flow<EntryWithFood>

    fun getRecipeEntry(id: Long): Flow<EntryWithRecipe>

    fun getEntriesFromDate(date: LocalDate): Flow<List<EntryWithFood>>

    fun getRecipeEntriesFromDate(date: LocalDate): Flow<List<EntryWithRecipe>>

    suspend fun updateEntryServingSize(id: Long, servingSize: Int)

}