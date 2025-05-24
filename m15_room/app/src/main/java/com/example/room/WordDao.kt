package com.example.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(Word::class, OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word): Long

    @Transaction
    suspend fun insertOrUpdate(word: Word) {
        val id = insert(word)
        if (id == -1L) update(word.word)
    }

    @Query("SELECT * FROM dictionary ORDER BY count DESC")
    fun getFirstFive(): Flow<List<Word>>

    @Query("DELETE FROM dictionary")
    suspend fun deleteAll()

    @Query("UPDATE dictionary SET count = count+1 WHERE word = :string")
    suspend fun update(string: String)
}
