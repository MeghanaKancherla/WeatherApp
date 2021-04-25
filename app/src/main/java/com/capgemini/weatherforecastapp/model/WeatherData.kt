package com.capgemini.weatherforecastapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_weather_table")
data class DailyWeather(var lat: Double,
                        var lng: Double,
                        var date: Long,
                        var min: Double,
                        var max: Double,
                        var weatherCondition: String,
                        var humidity: Int,
                        var day: Double,
                        var night: Double,
                        var eve: Double,
                        var img: String,
                        var sunrise: Long,
                        var sunset: Long,
                        @PrimaryKey(autoGenerate = true) var id: Int = 0)

@Entity(tableName = "hourly_weather_table")
data class HourlyWeather(var dt: Long,
                         var temp: Double,
                         var pressure: Int,
                         var humidity: Int,
                         var dew_point: Double,
                         var clouds: Int,
                         var wind_speed: Double,
                         var icon: String,
                         var description: String,
                         @PrimaryKey(autoGenerate = true) var id: Int = 0)

@Entity(tableName = "current_weather_table")
data class CurrentWeather(var dt: Long,
                          var temp: Double,
                          var sunrise: Long,
                          var sunset: Long,
                          var pressure: Int,
                          var humidity: Int,
                          var dew_point: Double,
                          var clouds: Int,
                          var icon: String,
                          var description: String,
                          @PrimaryKey(autoGenerate = true) var id: Int = 0)
