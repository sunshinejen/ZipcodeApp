package com.example.zipcodeapp.details

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.zipcodeapp.*
import kotlinx.android.synthetic.main.item_daily_forecast.*
import java.text.SimpleDateFormat
import java.util.*




class ForecastDetailsFragment : Fragment() {

    private val args : ForecastDetailsFragmentArgs by navArgs()

    private val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyy")


    private  lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_forecast_details, container, false)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val tempText = layout.findViewById<TextView>(R.id.tempText)
        val descriptionText = layout.findViewById<TextView>(R.id.descriptionText)
        val dateText = layout.findViewById<TextView>(R.id.dateTextDetails)
        val forecastIcon = layout.findViewById<ImageView>(R.id.forecastIconDetails)

        tempText.text = formatTempForDisplay(args.temp, tempDisplaySettingManager.getTempDisplaySetting())
        descriptionText.text = args.description

        val iconID = args.icon
        forecastIcon.load("http://openweathermap.org/img/wn/${iconID}@2x.png")

        dateText.text = DATE_FORMAT.format(Date(args.date * 1000))

        return layout
    }
}
