package com.capgemini.weatherforecastapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DailyWeatherDao {

    @Insert
    suspend fun insert(daily: DailyWeather)

    @Query("DELETE FROM daily_weather_table")
    suspend fun deleteAll()

    @Query("select * from daily_weather_table")
    suspend fun getDailyWeather(): List<DailyWeather>
}