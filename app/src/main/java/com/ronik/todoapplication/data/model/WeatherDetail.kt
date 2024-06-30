package com.ronik.todoapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ronik.todoapplication.data.model.WeatherDetail.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class WeatherDetail(
    @PrimaryKey
    var id: Int? = 0,
    var city: String? = null,
    var temp: Double? = null,
    var pressure: Int? = null,
    var sunrise: String? = null, // восход
    var sunset: String? = null,  // закат
    var dateTime: String? = null
) {
    companion object {
        const val TABLE_NAME = "weather_detail"
    }
}