package com.example.zipcodeapp.details

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.zipcodeapp.*
import com.example.zipcodeapp.databinding.FragmentForecastDetailsBinding
import kotlinx.android.synthetic.main.item_daily_forecast.*
import java.text.SimpleDateFormat
import java.util.*


class ForecastDetailsFragment : Fragment() {

    private val args : ForecastDetailsFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private val viewModel : ForecastDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private var _binding: FragmentForecastDetailsBinding? = null
    //this property is only valid between onCreateview and onDestroyView
    private val binding get()= _binding!!


    private  lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> {viewState ->
            //update the UI
            binding.tempText.text= formatTempForDisplay(viewState.temp, tempDisplaySettingManager.getTempDisplaySetting())
            binding.descriptionText.text = viewState.description
            binding.dateTextDetails.text = viewState.date
            binding.forecastIconDetails.load(viewState.iconUrl)
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
