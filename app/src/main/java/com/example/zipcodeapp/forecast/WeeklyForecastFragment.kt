package com.example.zipcodeapp.forecast

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zipcodeapp.*
import com.example.zipcodeapp.api.DailyForecast
import com.example.zipcodeapp.api.WeeklyForecast

import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_forecast_details.*
import kotlinx.android.synthetic.main.fragment_location_entry.*
import kotlinx.android.synthetic.main.fragment_location_entry.view.*
import kotlinx.android.synthetic.main.item_daily_forecast.*

/**
 * A simple [Fragment] subclass.
 */
class WeeklyForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private val forecastRepository = ForecastRepository()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weekly_forecast, container, false)
        val emptyText = view.findViewById<TextView>(R.id.emptyText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())


        val locationEntryButton : FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            showLocationEntry()
        }


        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)

        //informs the recycler view how layout items should be laid out
        forecastList.layoutManager = LinearLayoutManager(requireContext())

        //trailing lambda syntax, if you are passing a function to another function, if the function you are passing is the last argument then you can pass it outside the parenthesis DailyForecastAdapter(){}
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager){forecast ->
            //it -> implicit receiver type or you can rename like we did to forecastItem
            showForecastDetails(forecast)

        }
        forecastList.adapter = dailyForecastAdapter


        val weeklyForecastObserver = Observer<WeeklyForecast> { weeklyForecast ->
            emptyText.visibility = View.GONE
            progressBar.visibility = View.GONE
            //update our list adapter
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        }

        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> {savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    progressBar.visibility = View.VISIBLE
                    forecastRepository.loadWeeklyForecast(savedLocation.zipcode)
                }
            }
        }

        locationRepository.savedLocation.observe(viewLifecycleOwner,savedLocationObserver)

        return view
    }

    private fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)

    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val date = forecast.date
        val icon = forecast.weather[0].icon

        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(temp, description, date, icon)
        findNavController().navigate(action)
    }

    companion object {
        //a companion object an object scoped to an instance of current forecast fragment, in java it acts like static methods
        const val KEY_ZIPCODE = "key_zipcode"

        // takes in the zipcode to load the required data
        fun newInstance(zipcode: String): WeeklyForecastFragment {
            val fragment = WeeklyForecastFragment()

            var args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }


}
