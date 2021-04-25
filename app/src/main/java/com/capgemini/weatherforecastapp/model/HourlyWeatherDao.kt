package com.capgemini.weatherforecastapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HourlyWeatherDao {

    @Insert
    suspend fun insert(hour: HourlyWeather)

    @Query("DELETE FROM hourly_weather_table")
    suspend fun deleteAll()

    @Query("select * from hourly_weather_table")
    suspend fun getHourlyWeather(): List<HourlyWeather>
}