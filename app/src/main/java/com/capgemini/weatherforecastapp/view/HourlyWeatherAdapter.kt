package com.capgemini.weatherforecastapp.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capgemini.weatherforecastapp.R
import com.capgemini.weatherforecastapp.viewModels.WeatherViewModel
import java.util.*

class HourlyWeatherAdapter(val hourList: List<HourlyWeather>) : RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>(){

    lateinit var model: WeatherViewModel

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val timeT = view.findViewById<TextView>(R.id.timeT)
        val tempT = view.findViewById<TextView>(R.id.currTempT)
        val pressure = view.findViewById<TextView>(R.id.pressureT)
        val humidity = view.findViewById<TextView>(R.id.humidT)
        val duePt = view.findViewById<TextView>(R.id.dueT)
        val clouds = view.findViewById<TextView>(R.id.cloudsT)
        val wind = view.findViewById<TextView>(R.id.windT)
        val image = view.findViewById<ImageView>(R.id.hourlyIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.hourly_list, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hour = hourList[position]
        val time = Date((hour.dt)*1000).toLocaleString()
        //holder.timeT.append(time.substringBeforeLast(" ").substringAfterLast(" "))
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            holder.timeT.setText("Time: ${time}")
        }
        holder.tempT.setText("Temperature: ${hour.temp}")
        holder.pressure.setText("Pressure: ${hour.pressure}")
        holder.humidity.setText("Humidity: ${hour.humidity}")
        holder.duePt.setText("Dew Point: ${hour.dew_point}")
        holder.clouds.setText("Clouds: ${hour.clouds}")
        holder.wind.setText("Wind Speed: ${hour.wind_speed}")

        val iconList = hour.weather
        val icon = iconList[0].icon

        val imageUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
        Glide.with(holder.itemView.context).load(Uri.parse(imageUrl)).into(holder.image)

    }

    override fun getItemCount() = hourList.size
}