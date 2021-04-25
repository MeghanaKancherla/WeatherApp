package com.capgemini.weatherforecastapp.view

import java.io.Serializable

data class Weather(val lat: Double,
                   val long: Double,
                   val date: Long,
                   val min: Double,
                   val max: Double,
                   val weatherCondition: String,
                   val humidity: Int,
                   val day: Double,
                   val night: Double,
                   val eve: Double,
                   val img: String,
                   val sunrise: Long,
                   val sunset: Long) : Serializable

data class HourlyWeather(val dt: Long,
                         val temp: Double,
                         val pressure: Int,
                         val humidity: Int,
                         val dew_point: Double,
                         val clouds: Int,
                         val wind_speed: Double,
                         val weather: List<HourlyCondition>)


data class HourlyCondition(val icon: String,
                           val description: String)

data class DailyHourlyDeatils(val hourly: List<HourlyWeather>)

data class CurrentDetails(val dt: Long,
                          val temp: Double,
                          val sunrise: Long,
                          val sunset: Long,
                          val pressure: Int,
                          val humidity: Int,
                          val dew_point: Double,
                          val clouds: Int,
                          val weather: List<HourlyCondition>)

data class CurrentWeather(val current: CurrentDetails)