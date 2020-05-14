package com.example.zipcodeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

//extends syntax (DailyForecastViewHolder( view: View): RecyclerView.ViewHolder(view))
class DailyForecastViewHolder(
    view: View,
    private val tempDisplaySettingManager: TempDisplaySettingManager

    ): RecyclerView.ViewHolder(view){

    private val tempText = view.findViewById<TextView>(R.id.tempText)
    private val descriptionText = view.findViewById<TextView>(R.id.descriptionText)

    fun bind(dailyForecast: DailyForecast){
        tempText.text = formatTempForDisplay(dailyForecast.temp, tempDisplaySettingManager.getTempDisplaySetting())
        descriptionText.text = dailyForecast.description
    }

}

class DailyForecastAdapter(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    //unit is similar to void in Java
    private val clickHandler: (DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder>(DIFF_CONFIG){

    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<DailyForecast>(){
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                //kotlin exact comparison
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(itemView, tempDisplaySettingManager)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}