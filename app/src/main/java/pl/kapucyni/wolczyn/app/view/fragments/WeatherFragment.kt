package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentWeatherBinding
import pl.kapucyni.wolczyn.app.model.WeatherRecord
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.adapters.WeatherRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel
import pl.kapucyni.wolczyn.app.viewmodels.WeatherViewModel

class WeatherFragment : BindingFragment<FragmentWeatherBinding>() {

    private val mMainViewModel: MainViewModel by activityViewModels()
    private lateinit var mWeatherViewModel: WeatherViewModel
    private lateinit var mAdapter: WeatherRecyclerAdapter

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentWeatherBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        mAdapter = WeatherRecyclerAdapter()
        binding.weatherRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        mWeatherViewModel =
            ViewModelProvider(this@WeatherFragment)[WeatherViewModel::class.java]
        requireActivity().let {
            if (mMainViewModel.weatherList == null) fetchWeather()
            else mWeatherViewModel.weatherRecords.postValue(mMainViewModel.weatherList)
        }

        mWeatherViewModel.weatherRecords.observe(viewLifecycleOwner) { weatherList ->
            val days = weatherList.map { it.dt_txt.split(" ")[0] }.distinct()
            var weatherDays = arrayListOf<List<WeatherRecord>>()
            days.forEach { day ->
                weatherDays.add(weatherList.filter {
                    val dayOfMonth = day.split("-").last().toInt() + 1
                    val nextDayMidnight =
                        day.replaceRange(
                            8,
                            10,
                            if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                        ) + " 00:00:00"
                    it.dt_txt.split(" ")[0] == day || it.dt_txt == nextDayMidnight
                })
            }
            weatherDays = weatherDays.filter { weatherDay ->
                weatherDay[weatherDay.size - 1].dt_txt.split(" ")[1] == "00:00:00"
            } as ArrayList<List<WeatherRecord>>
            mAdapter.setWeatherList(weatherDays)
            mMainViewModel.weatherList = weatherList
            binding.loadingIndicator.hide()
            binding.weatherSwipeToRefresh.isRefreshing = false
        }

        binding.weatherSwipeToRefresh.apply {
            setOnRefreshListener { fetchWeather() }
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.swipe_refresh_color_1),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_2),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_3),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_4)
            )
        }
    }

    private fun fetchWeather() {
        requireActivity().tryToRunFunctionOnInternet { mWeatherViewModel.fetchWeather() }
    }
}
