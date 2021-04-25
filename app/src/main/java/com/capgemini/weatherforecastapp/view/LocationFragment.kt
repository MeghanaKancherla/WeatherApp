package com.capgemini.weatherforecastapp.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.capgemini.weatherforecastapp.R
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "lattitude"
private const val ARG_PARAM2 = "longitude"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var lattitude: Double? = null
    private var longitude: Double? = null

    var cityList = mutableListOf("Select", "Bangalore", "Chennai", "New Delhi", "Hyderabad", "Kerala", "Mumbai")
    lateinit var adapter : ArrayAdapter<String>

    var lat = 0.0
    var long = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lattitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, cityList)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        super.onViewCreated(view, savedInstanceState)

        locT.setText(getAddress(lattitude!!, longitude!!))

        currentLocB.setOnClickListener {
            val currentLocIntent = Intent(activity, WeatherBottomNavigation::class.java)
            val bundle = Bundle()
            bundle.putDouble("sentLattitude", lattitude!!)
            bundle.putDouble("sentLongitude", longitude!!)
            currentLocIntent.putExtra("values", bundle)
            startActivity(currentLocIntent)
        }

        selectB.setOnClickListener {
            val city = cityE.text.toString()
            getAddressLatLong(city)
            val selectedLocIntent = Intent(activity, WeatherBottomNavigation::class.java)
            val bundle = Bundle()
            bundle.putDouble("sentLattitude", lat)
            bundle.putDouble("sentLongitude", long)
            selectedLocIntent.putExtra("values", bundle)
            startActivity(selectedLocIntent)
        }
    }


    private fun getAddressLatLong(city: String){
        val gCoder = Geocoder(activity)
        val addressList = gCoder.getFromLocationName(city, 10)
        if(addressList.isNotEmpty()){
            val addr = addressList[0]
            lat = addr.latitude
            long = addr.longitude
        }
    }

    private fun getAddress(lat: Double, long: Double): String {
        var strAddress = ""
        val gCoder = Geocoder(activity)
        val addressList = gCoder.getFromLocation(lat, long, 10)
        if(addressList.isNotEmpty()){
            val addr = addressList[0]
//            for(i in 0..addr.maxAddressLineIndex){
//                strAddress += addr.getAddressLine(i)
//            }
                strAddress =  "${addr.subLocality}, ${addr.locality}"
        }
        return strAddress
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(lattitude: Double, longitude: Double) =
                LocationFragment().apply {
                    arguments = Bundle().apply {
                        putDouble(ARG_PARAM1, lattitude)
                        putDouble(ARG_PARAM2, longitude)
                    }
                }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        cityE.setText("${cityList[position]}")
//        if(position != 0) {
//            locT.setText("${cityList[position]}")
//        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        cityE.setText("Bangalore")
    }
}