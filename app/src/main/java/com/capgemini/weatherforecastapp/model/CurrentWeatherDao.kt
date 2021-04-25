package com.capgemini.weatherforecastapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrentWeatherDao {

    @Insert
    suspend fun insert(curr: CurrentWeather)

    @Query("DELETE FROM current_weather_table")
    suspend fun deleteAll()

    @Query("select * from current_weather_table")
    suspend fun getCurrentWeather(): List<CurrentWeather>
}