package com.example.zipcodeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zipcodeapp.api.CurrentWeather
import com.example.zipcodeapp.api.WeeklyForecast
import com.example.zipcodeapp.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response

import kotlin.random.Random

class ForecastRepository {

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather


    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast : LiveData<WeeklyForecast> = _weeklyForecast

    fun loadWeeklyForecast(zipcode: String){
        val call = createOpenWeatherMapService().currentWeather(zipcode, "imperial", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading location for weekly forecast", t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if(weatherResponse != null) {
                    //load 7 day forecast
                    val forecastCall = createOpenWeatherMapService().sevenDayForecast(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        exclude = "current, minutely, hourly",
                        units = "imperial",
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                    )

                    forecastCall.enqueue(object: Callback<WeeklyForecast>{
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(ForecastRepository::class.java.simpleName, "error loading weekly forecast")
                        }

                        override fun onResponse(
                            call: Call<WeeklyForecast>,
                            response: Response<WeeklyForecast>
                        ) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse != null) {
                                _weeklyForecast.value = weeklyForecastResponse
                            }
                        }

                    })
                }
            }

        })

    }


    fun loadCurrentForecast(zipcode: String){
        val call = createOpenWeatherMapService().currentWeather(zipcode, "imperial", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading current weather", t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if(weatherResponse != null) {
                    _currentWeather.value = weatherResponse
                }
            }

        })
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