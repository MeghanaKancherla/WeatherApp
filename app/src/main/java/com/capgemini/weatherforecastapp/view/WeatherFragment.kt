package com.capgemini.weatherforecastapp.view

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.capgemini.weatherforecastapp.R
import com.capgemini.weatherforecastapp.model.DailyWeather
import com.capgemini.weatherforecastapp.viewModels.WeatherViewModel
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * A fragment representing a list of Items.
 */
class WeatherFragment : Fragment() {

    private var columnCount = 1
    var lat = 0.0f
    var long = 0.0f
    var weatherList = mutableListOf(Weather(0.0,0.0,0,0.0,0.0,"xyz", 0, 0.0, 0.0, 0.0, "", 0, 0))
    lateinit var weatherAdapter: MyweatherRecyclerViewAdapter
    lateinit var model: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vmProvider = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
        model = vmProvider.get(WeatherViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        weatherList.clear()
        weatherList.add(Weather(0.0,0.0,0,0.0,0.0,"xyz", 0, 0.0, 0.0, 0.0, "", 0, 0))
        //model.addDailyWeather(DailyWeather(0.0,0.0,0,0.0,0.0,"xyz", 0, 0.0, 0.0, 0.0, "", 0, 0))

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
//                val pref = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//                if(!pref.getBoolean("isNetworkAvailable", true)){
//                    model.dailyList.observe(requireActivity(), Observer {
//                        for(i in it){
//                            weatherList.add(Weather(i.lat, i.lng, i.date, i.min, i.max, i.weatherCondition, i.humidity, i.day, i.night, i.eve, i.img, i.sunrise, i.sunset))
//                        }
//                        Toast.makeText(activity, "$weatherList", Toast.LENGTH_LONG).show()
//                    })
//                }
                weatherAdapter = MyweatherRecyclerViewAdapter(weatherList) {
                    Toast.makeText(activity, "Date: $it", Toast.LENGTH_LONG).show()
                    //val detailsFrag = WeatherDetailsFragment.newInstance(it)
                    val bundle = bundleOf("daily" to it)
                    findNavController().navigate(R.id.action_weatherFragment_to_weatherDetailsFragment, bundle)
                }
                adapter = weatherAdapter
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = view.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if(pref.getBoolean("isNetworkAvailable", true)) {
            lat = pref.getFloat("lat", 12.7723f)
            long = pref.getFloat("long", 77.865f)
            model.deleteAllDaily()
            TempTask().execute(lat.toDouble(), long.toDouble())
        }
        else{
            model.getDailyWeather()
            model.dailyList.observe(requireActivity(), Observer {
                for(i in it){
                    weatherList.add(Weather(i.lat, i.lng, i.date, i.min, i.max, i.weatherCondition, i.humidity, i.day, i.night, i.eve, i.img, i.sunrise, i.sunset))
                }
                //Toast.makeText(activity, "$weatherList", Toast.LENGTH_LONG).show()
                weatherAdapter.notifyDataSetChanged()
            })
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    fun getTemperature(lat: Double, long: Double): String?{
        val urlS = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&units=metric&exclude=current,minutely&appid=90c7f12988c84f53adb45f54591712bb"
        val url = URL(urlS)
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 15000
        connection.readTimeout = 15000
        if(connection.responseCode == 200){
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line = reader.readLine()
            var response = ""
            while(line != null){
                response += line
                line = reader.readLine()
            }
            Log.d("PlaceListFragment", "RESPONSE: $response")
            return response
        }
        else{
            Log.d("PlaceListFragment", "Did not get results: ${connection.responseCode}, ${connection.responseMessage}")
        }
        return null
    }

    inner class TempTask : AsyncTask<Double, Void, String>(){

        override fun doInBackground(vararg coord: Double?): String {
            val latCoord = coord[0]!!
            val longCoord = coord[1]!!
            val response = getTemperature(latCoord, longCoord)
            return response ?: ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("WeatherFragment", "$result")
            if(result?.isNotEmpty()!!){
                val responseObj = JSONObject(result)
                val tempArray = responseObj.getJSONArray("daily")
                for (i in 0 until tempArray.length()){
                    val tempObj = tempArray[i] as JSONObject
                    val date = tempObj.getLong("dt")
                    val tobj = tempArray.getJSONObject(i)
                    val t = tobj.getJSONObject("temp")
                    val min = t.getDouble("min")
                    val max = t.getDouble("max")
                    val day = t.getDouble("morn")
                    val night = t.getDouble("night")
                    val eve = t.getDouble("eve")
                    val sunrise = tempObj.getLong("sunrise")
                    val sunset = tempObj.getLong("sunset")
                    val weatherCond = tobj.getJSONArray("weather")
                    var cond = ""
                    var image = ""
                    for(j in 0 until weatherCond.length()){
                        val condObj = weatherCond[j] as JSONObject
                        cond = condObj.getString("description")
                        image = condObj.getString("icon")
                    }
                    val humidity = tempObj.getInt("humidity")
                    weatherList.add(Weather(lat.toDouble(), long.toDouble(), date, min, max, cond, humidity, day, night, eve, image, sunrise, sunset))
                    model.addDailyWeather(DailyWeather(lat.toDouble(), long.toDouble(), date, min, max, cond, humidity, day, night, eve, image, sunrise, sunset))
                }
                weatherAdapter.notifyDataSetChanged()
            }
            else{
                Toast.makeText(activity, "Unable to get weather conditions at $lat, $long", Toast.LENGTH_LONG).show()
            }
        }

    }
}