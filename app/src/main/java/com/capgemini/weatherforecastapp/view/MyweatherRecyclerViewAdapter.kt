package com.capgemini.weatherforecastapp.view

import android.graphics.Color
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.capgemini.weatherforecastapp.R
import java.util.*

//import com.capgemini.weatherforecastapp.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyweatherRecyclerViewAdapter(
        private val values: List<Weather>,
        val listener: (Weather) -> Unit
    ) : RecyclerView.Adapter<MyweatherRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if(position == 0){
            holder.dateT.text = "DATE"
            holder.minT.text = "MIN"
            holder.maxT.text = "MAX"
            holder.linear.setBackgroundColor(Color.CYAN)
        }
        else {
            val dateObj = Date(item.date*1000)
            val date = dateObj.toLocaleString()
            holder.dateT.text = date
            holder.minT.text = item.min.toString()
            holder.maxT.text = item.max.toString()

            val imageUrl = "https://openweathermap.org/img/wn/${item.img}@2x.png"
            Glide.with(holder.itemView.context).load(Uri.parse(imageUrl)).into(holder.icon)

            holder.itemView.setOnClickListener {
                listener(item)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateT: TextView = view.findViewById(R.id.dateT)
        val minT: TextView = view.findViewById(R.id.minT)
        val maxT: TextView = view.findViewById(R.id.maxT)
        val linear: LinearLayout = view.findViewById(R.id.linearL)
        val icon: ImageView = view.findViewById(R.id.imgView)
    }
}