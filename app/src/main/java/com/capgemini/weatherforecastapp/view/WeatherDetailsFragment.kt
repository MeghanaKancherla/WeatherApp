package com.capgemini.weatherforecastapp.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capgemini.weatherforecastapp.R
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.android.synthetic.main.fragment_weather_details.*
import java.io.Serializable
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "daily"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var weather: Weather? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            weather = it.get(ARG_PARAM1) as Weather?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Toast.makeText(activity, "Date: $weather", Toast.LENGTH_LONG).show()
        val convertDate = Date((weather?.date!!)*1000)
        val convertedDate = convertDate.toLocaleString()
        detailDateT.append(convertedDate)
        humidityT.append("${weather!!.humidity}")
        conditionT.append(weather!!.weatherCondition)
        mornT.append("${weather!!.day}")
        eveT.append("${weather!!.eve}")
        nightT.append("${weather!!.night}")
        sunriseT.append(Date((weather?.sunrise!!)*1000).toLocaleString())
        sunsetT.append(Date((weather?.sunset!!)*1000).toLocaleString())

        val imageUrl = "https://openweathermap.org/img/wn/${weather?.img}@2x.png"
        Glide.with(requireActivity()).load(Uri.parse(imageUrl)).into(imgIcon)

        Toast.makeText(activity, "Lat: ${weather!!.lat}, Long: ${weather!!.long}", Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WeatherDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Serializable) =
            WeatherDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    //put(ARG_PARAM1, param1)
                }
            }
    }
}