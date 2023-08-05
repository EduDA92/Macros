package com.example.macrostracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.macrostracker.data.entity.EntryAndFood
import com.example.macrostracker.data.entity.EntryAndRecipe
import com.example.macrostracker.data.entity.EntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EntryDao {

    @Insert
    suspend fun insertEntry(entry: EntryEntity): Long

    @Update
    suspend fun updateEntry(entry: EntryEntity)

    @Query("DELETE FROM entries WHERE id LIKE :id")
    suspend fun deleteEntry(id: Long)

    @Transaction
    @Query("SELECT * FROM entries WHERE id LIKE :id")
    fun getEntry(id: Long): Flow<EntryAndFood>

    @Transaction
    @Query("SELECT * FROM entries WHERE id LIKE :id")
    fun getRecipeEntry(id: Long): Flow<EntryAndRecipe>

    @Query("UPDATE entries SET servingSize = :servingSize WHERE id = :id")
    suspend fun updateEntryServingSize(id: Long, servingSize: Int)

    /* Get only food entries, food entries will have recipeID set to null */
    @Transaction
    @Query("SELECT * FROM entries WHERE date LIKE :date AND entries.foodId NOT NULL ")
    fun getEntriesFromDate(date: LocalDate): Flow<List<EntryAndFood>>

    /* Get only recipe entries, recipe entries will have foodId set to null */
    @Transaction
    @Query("SELECT * FROM entries WHERE date LIKE :date AND entries.recipeId NOT NULL")
    fun getRecipeEntriesFromDate(date: LocalDate): Flow<List<EntryAndRecipe>>

}