package com.example.photoapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDAO {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photo")
    suspend fun getAllPhoto(): List<Photo>
}
