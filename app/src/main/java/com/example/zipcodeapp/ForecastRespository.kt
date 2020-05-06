package com.example.zipcodeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class ForecastRespository {

    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>()
    val weeklyForecast : LiveData<List<DailyForecast>> = _weeklyForecast

    fun loadForecast(zipcode:String){
        val randomValues = List(10) { Random.nextFloat().rem(100) * 100 }
        val forecastItems = randomValues.map {temp ->
            DailyForecast(temp,getTempDescription(temp))
        }

        _weeklyForecast.setValue(forecastItems)
    }

    private fun getTempDescription(temp: Float) : String {
//(when) is similar to switch statement
        return when (temp){
            in Float.MIN_VALUE.rangeTo(0f) -> "Anything below 0 doesn't make sense"
            in 0f.rangeTo(32f) -> " Way too cold!"
            in 32f.rangeTo(55f) -> " Still cold!"
            in 55f.rangeTo(68f) -> " It's Spring in Seattle, but still cold!"
            in 68f.rangeTo(80f) -> " Perfect Weather!"
            in 80f.rangeTo(90f) -> " Still Perfect Weather!"
            in 90f.rangeTo(100f) -> " You live in the valley, turn your AC on!"
            in 100f.rangeTo(Float.MAX_VALUE) -> " What is this, Arizona?"
            else -> "Does not compute"
        }

    }
}