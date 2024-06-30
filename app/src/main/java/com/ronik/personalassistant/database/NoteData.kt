package com.ronik.personalassistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ronik.personalassistant.model.Note


@Database(entities = [Note::class], version = 1)
abstract class NoteData: RoomDatabase() {
    abstract fun notes(): Notes

    companion object {
        @Volatile
        private var instance: NoteData? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
            synchronized(LOCK) {
                instance ?:
                    createDatabase(context).also {
                        instance = it
                    }
            }

        private fun createDatabase(context: Context): NoteData {
            return Room.databaseBuilder(
                context = context.applicationContext, NoteData::class.java, "app_db"
            ).build()
        }
    }
}