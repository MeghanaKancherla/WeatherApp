package com.capgemini.weatherforecastapp.view

import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capgemini.weatherforecastapp.R
import com.capgemini.weatherforecastapp.viewModels.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_current_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "lat"
private const val ARG_PARAM2 = "long"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentWeatherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var lat: Float? = 0.0f
    private var long: Float? = 0.0f
    lateinit var model: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getFloat(ARG_PARAM1)
            long = it.getFloat(ARG_PARAM2)
        }
        val vmProvider = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
        model = vmProvider.get(WeatherViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = view.context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        if(pref.getBoolean("isNetworkAvailable", true)){
            lat = pref.getFloat("lat", 12.7723f)
            long = pref.getFloat("long", 77.865f)

            val key = "90c7f12988c84f53adb45f54591712bb"
            val url = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&units=metric&exclude=hourly,minutely,daily&appid=$key"
            val request = HourlyWeatherInterface.getInstance().getCurrentDetails(url)
            request.enqueue(CurrentCallBack())
        }
        else{
            model.getCurrentWeather()
            model.currentList.observe(requireActivity(), Observer {
                val cList = it[0]
                currDateT.append(Date((cList.dt)*1000).toLocaleString())
                currTempT.append(cList.temp.toString())
                currHumidityT.append("${cList.humidity}")
                currPressureT.append("${cList.pressure}")
                currDewT.append("${cList.dew_point}")
                currCloudT.append("${cList.clouds}")
                val icon = cList.icon

                val imageUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
                Glide.with(requireActivity()).load(Uri.parse(imageUrl)).into(imageIcon)

                currDesT.append(cList.description)
                currSunriseT.append(Date((cList.sunrise)*1000).toLocaleString())
                currSunsetT.append(Date((cList.sunset)*1000).toLocaleString())
            })
        }
    }

    inner class CurrentCallBack: Callback<CurrentWeather>{

        override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
            if(response.isSuccessful){
                val current = response.body()
                current?.current?.let {
                    currDateT.append(Date((it.dt)*1000).toLocaleString())
                    currTempT.append(it.temp.toString())
                    currHumidityT.append("${it.humidity}")
                    currPressureT.append("${it.pressure}")
                    currDewT.append("${it.dew_point}")
                    currCloudT.append("${it.clouds}")
                    val icon = it.weather[0].icon

                    val imageUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
                    Glide.with(activity!!).load(Uri.parse(imageUrl)).into(imageIcon)

                    currDesT.append(it.weather[0].description)
                    currSunriseT.append(Date((it.sunrise)*1000).toLocaleString())
                    currSunsetT.append(Date((it.sunset)*1000).toLocaleString())

                    model.deleteAllCurrent()
                    model.addCurrentWeather(com.capgemini.weatherforecastapp.model.CurrentWeather(it.dt, it.temp, it.sunrise, it.sunset, it.pressure, it.humidity, it.dew_point, it.clouds, it.weather[0].icon, it.weather[0].description))
                }
            }
        }

        override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
            Toast.makeText(activity, "No data available for your location", Toast.LENGTH_LONG).show()
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentWeatherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Double, param2: Double) =
                CurrentWeatherFragment().apply {
                    arguments = Bundle().apply {
                        putDouble(ARG_PARAM1, param1)
                        putDouble(ARG_PARAM2, param2)
                    }
                }
    }
}