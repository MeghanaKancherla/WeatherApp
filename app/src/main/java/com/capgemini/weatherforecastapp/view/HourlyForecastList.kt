package com.capgemini.weatherforecastapp.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capgemini.weatherforecastapp.R
import com.capgemini.weatherforecastapp.model.HourlyWeather
import com.capgemini.weatherforecastapp.viewModels.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_hourly_forecast_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "lat"
private const val ARG_PARAM2 = "lng"

/**
 * A simple [Fragment] subclass.
 * Use the [HourlyForecastList.newInstance] factory method to
 * create an instance of this fragment.
 */
class HourlyForecastList : Fragment() {
    // TODO: Rename and change types of parameters
    private var lat: Double? = 0.0
    private var lng: Double? = 0.0
    lateinit var model: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getDouble(ARG_PARAM1)
            lng = it.getDouble(ARG_PARAM2)
        }

        val vmProvider = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
        model = vmProvider.get(WeatherViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly_forecast_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hourlyRView.layoutManager = LinearLayoutManager(activity)

        val pref = view.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if(pref.getBoolean("isNetworkAvailable", true))
        {
            lat = pref.getFloat("lat", 12.7723f).toDouble()
            lng = pref.getFloat("long", 77.865f).toDouble()

            model.deleteAllHourly()

            val key = "90c7f12988c84f53adb45f54591712bb"
            val url = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lng&units=metric&exclude=current,minutely,daily&appid=$key"
            val request = HourlyWeatherInterface.getInstance().getHourlyDeatils(url)
            request.enqueue(HourlyWeatherCallback())
        }
        else {
            model.getHourlyWeather()
            model.hourlyList.observe(requireActivity(), Observer {
                val hList = mutableListOf<com.capgemini.weatherforecastapp.view.HourlyWeather>()
                for(i in it){
                    val weather = listOf(HourlyCondition(i.icon, i.description))
                    hList.add(HourlyWeather(i.dt, i.temp, i.pressure, i.humidity, i.dew_point, i.clouds, i.wind_speed, weather))
                }
                hourlyRView.adapter = HourlyWeatherAdapter(hList)

            })
        }
    }

    inner class HourlyWeatherCallback: Callback<DailyHourlyDeatils>{

        override fun onResponse(
                call: Call<DailyHourlyDeatils>,
                response: Response<DailyHourlyDeatils>
        ) {
            if(response.isSuccessful){
                val hour = response.body()
                hour?.hourly?.let {
                    hourlyRView.adapter = HourlyWeatherAdapter(it)
                    for(list in it){
                        model.addHourlyWeather(HourlyWeather(list.dt, list.temp, list.pressure, list.humidity, list.dew_point, list.clouds, list.wind_speed, list.weather[0].icon, list.weather[0].description))
                    }
                }
            }
        }

        override fun onFailure(call: Call<DailyHourlyDeatils>, t: Throwable) {
            Toast.makeText(activity, "Failed to get Hourly Details: ${t.message}", Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HourlyForecastList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double) =
            HourlyForecastList().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                }
            }
    }
}