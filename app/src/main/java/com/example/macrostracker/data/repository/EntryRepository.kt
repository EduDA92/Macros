package com.example.macrostracker.data.repository

import com.example.macrostracker.data.entity.EntryAndFood
import com.example.macrostracker.data.entity.EntryEntity
import com.example.macrostracker.model.Entry
import com.example.macrostracker.model.EntryWithFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EntryRepository {

    suspend fun insertEntry(entry: Entry): Long

    suspend fun updateEntry(entry: Entry)

    suspend fun deleteEntry(id: Long)

    fun getEntry(id: Long): Flow<EntryWithFood>

    fun getEntriesFromDate(date: LocalDate): Flow<List<EntryWithFood>>

    suspend fun updateEntryServingSize(id: Long, servingSize: Int)

}