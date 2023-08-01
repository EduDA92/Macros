package com.example.macrostracker.data.repository

import com.example.macrostracker.data.dao.EntryDao
import com.example.macrostracker.data.entity.asExternalModel
import com.example.macrostracker.model.Entry
import com.example.macrostracker.model.EntryWithFood
import com.example.macrostracker.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DefaultEntryRepository @Inject constructor(
    private val entryDao: EntryDao
) : EntryRepository {
    override suspend fun insertEntry(entry: Entry): Long =
        entryDao.insertEntry(entry.asEntity())

    override suspend fun updateEntry(entry: Entry) =
        entryDao.updateEntry(entry.asEntity())

    override suspend fun deleteEntry(id: Long) =
        entryDao.deleteEntry(id)

    override fun getEntry(id: Long): Flow<EntryWithFood> =
        entryDao.getEntry(id).map {
            it.asExternalModel()
        }

    override fun getEntriesFromDate(date: LocalDate): Flow<List<EntryWithFood>> =
        entryDao.getEntriesFromDate(date).map {
            it.map {
                it.asExternalModel()
            }
        }

    override suspend fun updateEntryServingSize(id: Long, servingSize: Int) =
        entryDao.updateEntryServingSize(id, servingSize)
}