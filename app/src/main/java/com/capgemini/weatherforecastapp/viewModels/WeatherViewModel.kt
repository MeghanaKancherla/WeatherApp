package com.capgemini.weatherforecastapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.capgemini.weatherforecastapp.model.CurrentWeather
import com.capgemini.weatherforecastapp.model.DailyWeather
import com.capgemini.weatherforecastapp.model.HourlyWeather
import com.capgemini.weatherforecastapp.model.Repository
import kotlinx.coroutines.launch

class WeatherViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = Repository(app)
    var currentList = MutableLiveData<List<CurrentWeather>>()
    var hourlyList = MutableLiveData<List<HourlyWeather>>()
    var dailyList = MutableLiveData<List<DailyWeather>>()

    fun addCurrentWeather(curr: CurrentWeather){
        viewModelScope.launch {
            repo.addCurrentWeather(curr)
        }
    }

    fun deleteAllCurrent(){
        viewModelScope.launch {
            repo.deleteAllCurrent()
        }
    }

    fun getCurrentWeather(){
        viewModelScope.launch {
            val list = repo.allCurrentDetails()
            currentList.postValue(list)
        }
    }

    fun addHourlyWeather(hour: HourlyWeather){
        viewModelScope.launch {
            repo.addHourlyWeather(hour)
        }
    }

    fun deleteAllHourly(){
        viewModelScope.launch {
            repo.deleteAllHourly()
        }
    }

    fun getHourlyWeather(){
        viewModelScope.launch {
            val list = repo.allHourlyDetails()
            hourlyList.postValue(list)
        }
    }

    fun addDailyWeather(daily: DailyWeather){
        viewModelScope.launch {
            repo.addDailyWeather(daily)
        }
    }

    fun deleteAllDaily(){
        viewModelScope.launch {
            repo.deleteAllDaily()
        }
    }

    fun getDailyWeather(){
        viewModelScope.launch {
            val list = repo.allDailyDetails()
            dailyList.postValue(list)
        }
    }

}