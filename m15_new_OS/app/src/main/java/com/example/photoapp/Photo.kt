package com.example.photoapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class Photo(
    @PrimaryKey
    @ColumnInfo
    val id: Int? = null,
    @ColumnInfo
    val src: String,
    @ColumnInfo
    val date: String
)

