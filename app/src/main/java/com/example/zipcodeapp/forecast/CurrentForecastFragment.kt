package com.example.zipcodeapp.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zipcodeapp.*
import com.example.zipcodeapp.api.CurrentWeather
import com.example.zipcodeapp.api.DailyForecast

import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_current_forecast.*

/**
 * A simple [Fragment] subclass.
 */
class CurrentForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        //!! if arguments is null it will crash
        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val locationName: TextView = view.findViewById(R.id.locationName)
        val tempText: TextView = view.findViewById(R.id.tempText)


        val locationEntryButton : FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            showLocationEntry()
        }



        //create the observer which updates the UI in response to forecast updates
        val currentWeatherObserver = Observer<CurrentWeather> {weather ->
            locationName.text = weather.name
            tempText.text = formatTempForDisplay(weather.forecast.temp, tempDisplaySettingManager.getTempDisplaySetting())
        }

        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)


        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when(savedLocation){
                is Location.Zipcode -> forecastRepository.loadCurrentForecast(savedLocation.zipcode)
            }
        }

        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }

    private fun showLocationEntry(){
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)

    }

    companion object {
        //a companion object an object scoped to an instance of current forecast fragment, in java it acts like static methods
        const val KEY_ZIPCODE = "key_zipcode"

        // takes in the zipcode to load the required data
        fun newInstance(zipcode: String): CurrentForecastFragment {
            val fragment = CurrentForecastFragment()

            var args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }
}
