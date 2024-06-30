package com.ronik.todoapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ronik.todoapplication.data.local.dao.WeatherDetailDao
import com.ronik.todoapplication.data.model.WeatherDetail

@Database(
    entities = [WeatherDetail::class],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDetailDao

    companion object {
        private const val DB_NAME = "todoapp"

        @Volatile
        private var INSTANCE: com.ronik.todoapplication.data.Database? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also{
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) {
            Room.databaseBuilder(
                context.applicationContext,
                com.ronik.todoapplication.data.Database::class.java,
                DB_NAME
            ).build()
        }
    }
}