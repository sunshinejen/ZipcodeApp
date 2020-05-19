package com.example.zipcodeapp.forecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zipcodeapp.*

import com.example.zipcodeapp.details.ForecastDetailsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class CurrentForecastFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    private val forecastRepository = ForecastRespository()

    private lateinit var appNavigator: AppNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        //!! if arguments is null it will crash
        val zipcode = arguments!!.getString(KEY_ZIPCODE) ?: ""

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_forecast, container, false)

        val locationEntryButton : FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener{
            appNavigator.navigateToLocationEntry()
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


        val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
            //update our list adapter
            dailyForecastAdapter.submitList(forecastItems)
        }
        forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)

        forecastRepository.loadForecast(zipcode)

        return view
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val forecastDetailsIntent = Intent(requireContext(), ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("key_temp", forecast.temp)
        forecastDetailsIntent.putExtra("key_description", forecast.description)
        startActivity(forecastDetailsIntent)
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
