package com.example.aweweather.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aweweather.R
import com.example.aweweather.databinding.FragmentFirstBinding
import com.example.aweweather.ui.adapter.ForecastAdapter
import com.example.aweweather.ui.view.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class DashFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModel<MainActivityViewModel>()
    private lateinit var adapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        adapter = ForecastAdapter()
        binding.weatherForecastList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.weatherModel.observe(viewLifecycleOwner) {
            with(binding) {
                currentDegrees.text = getString(R.string.degrees, it.temp)
                currentWeather.text = it.main
                currentWeatherDetails.visibility = View.VISIBLE
                minTemp.text = getString(R.string.degrees, it.temp_min)
                maxTemp.text = getString(R.string.degrees, it.temp_max)
                currentTemp.text = getString(R.string.degrees, it.temp)
            }

            context?.let { context ->
                when (it.main) {
                    "Rain" -> {
                        Glide.with(context).load(R.drawable.forest_rainy)
                            .into(binding.weatherBackground)
                        binding.mainLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.rainy,
                                null
                            )
                        )
                    }
                    "Clear" -> {
                        Glide.with(context).load(R.drawable.forest_sunny)
                            .into(binding.weatherBackground)
                        binding.mainLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.sunny,
                                null
                            )
                        )
                    }
                    "Clouds" -> {
                        Glide.with(context).load(R.drawable.forest_cloudy)
                            .into(binding.weatherBackground)
                        binding.mainLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.cloudy,
                                null
                            )
                        )
                    }
                    //default
                    else -> {
                        binding.mainLayout.setBackgroundColor(
                            resources.getColor(
                                R.color.rainy,
                                null
                            )
                        )
                    }
                }
            }

            adapter.submitList(it.weather)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}