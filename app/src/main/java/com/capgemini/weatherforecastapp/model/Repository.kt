package com.capgemini.weatherforecastapp.model

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class Repository(context: Context) {

    private val currWeatherDao = WeatherDataBase.getInstance(context).currentWeatherDao()

    suspend fun addCurrentWeather(curr: CurrentWeather) = currWeatherDao.insert(curr)
    suspend fun deleteAllCurrent() = currWeatherDao.deleteAll()
    suspend fun allCurrentDetails() : List<CurrentWeather>{
        var currList: List<CurrentWeather>? = null
        val result = CoroutineScope(Dispatchers.Default).async {
            currWeatherDao.getCurrentWeather()
        }
        currList = result.await()
        return currList
    }

    private val hourWeatherDao = WeatherDataBase.getInstance(context).hourlyWeatherDao()

    suspend fun addHourlyWeather(hour: HourlyWeather) = hourWeatherDao.insert(hour)
    suspend fun deleteAllHourly() = hourWeatherDao.deleteAll()
    suspend fun allHourlyDetails() : List<HourlyWeather>{
        var hourList: List<HourlyWeather>? = null
        val result = CoroutineScope(Dispatchers.Default).async {
            hourWeatherDao.getHourlyWeather()
        }
        hourList = result.await()
        return hourList
    }

    private val dailyWeatherDao = WeatherDataBase.getInstance(context).dailyWeatherDao()

    suspend fun addDailyWeather(daily: DailyWeather) = dailyWeatherDao.insert(daily)
    suspend fun deleteAllDaily() = dailyWeatherDao.deleteAll()
    suspend fun allDailyDetails(): List<DailyWeather>{
        var dailyList : List<DailyWeather>? = null
        val result = CoroutineScope(Dispatchers.Default).async {
            dailyWeatherDao.getDailyWeather()
        }
        dailyList = result.await()
        return dailyList
    }

}
