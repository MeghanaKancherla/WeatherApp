package com.capgemini.weatherforecastapp.view

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface HourlyWeatherInterface {

    @GET
    fun getHourlyDeatils(@Url url: String): Call<DailyHourlyDeatils>

    @GET
    fun getCurrentDetails(@Url url: String): Call<CurrentWeather>

    companion object{
        val BASE_URL = "https://api.openweathermap.org/"

        fun getInstance() : HourlyWeatherInterface {

            val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)

            val retrofit = builder.build()
            return retrofit.create(HourlyWeatherInterface::class.java)
        }
    }
}