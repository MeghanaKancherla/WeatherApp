package com.capgemini.weatherforecastapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DailyWeather::class, HourlyWeather::class, CurrentWeather::class], version = 1)
abstract class WeatherDataBase: RoomDatabase() {
    
    abstract fun dailyWeatherDao() : DailyWeatherDao
    abstract fun hourlyWeatherDao() : HourlyWeatherDao
    abstract fun currentWeatherDao() : CurrentWeatherDao
    
    companion object{
        private var instance: WeatherDataBase? = null
        
        fun getInstance(context: Context) = instance ?: buildDatabase(context).also { instance = it }

        private fun buildDatabase(context: Context): WeatherDataBase{
            val builder = Room.databaseBuilder(context.applicationContext, WeatherDataBase::class.java, "weather.db")
            return builder.build()
        }
    }
}