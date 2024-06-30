package com.ronik.personalassistant.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ronik.personalassistant.model.Note

/*
    Интерфейс для взаимодействия с таблицей заметок
 */

@Dao
interface Notes {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("select * from notes order by id desc")
    fun all(): LiveData<List<Note>>

    @Query("select * from notes where title like :search or description like :search")
    fun allWithSearch(search: String?): LiveData<List<Note>>
}