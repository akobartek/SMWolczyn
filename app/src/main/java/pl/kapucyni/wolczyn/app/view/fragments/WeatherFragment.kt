package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weather.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.WeatherRecord
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.adapters.WeatherRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel
import pl.kapucyni.wolczyn.app.viewmodels.WeatherViewModel

class WeatherFragment : Fragment() {

    private lateinit var mWeatherViewModel: WeatherViewModel
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mAdapter: WeatherRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_weather, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = WeatherRecyclerAdapter()
        view.weatherRecyclerView.layoutManager = LinearLayoutManager(view.context)
        view.weatherRecyclerView.itemAnimator = DefaultItemAnimator()
        view.weatherRecyclerView.adapter = mAdapter

        mWeatherViewModel = ViewModelProviders.of(this@WeatherFragment).get(WeatherViewModel::class.java)
        activity?.let {
            mMainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            if (mMainViewModel.weatherList == null) fetchWeather()
            else mWeatherViewModel.weatherRecords.postValue(mMainViewModel.weatherList)
        }

        mWeatherViewModel.weatherRecords.observe(this@WeatherFragment, Observer { weatherList ->
            val days = weatherList.map { it.dt_txt.split(" ")[0] }.distinct()
            var weatherDays = arrayListOf<List<WeatherRecord>>()
            days.forEach { day ->
                weatherDays.add(weatherList.filter {
                    val dayOfMonth = day.split("-").last().toInt() + 1
                    val nextDayMidnight =
                        day.replaceRange(8, 10, if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth") + " 00:00:00"
                    it.dt_txt.split(" ")[0] == day || it.dt_txt == nextDayMidnight
                })
            }
            weatherDays = weatherDays.filter { weatherDay ->
                weatherDay[weatherDay.size - 1].dt_txt.split(" ")[1] == "00:00:00"
            } as ArrayList<List<WeatherRecord>>
            mAdapter.setWeatherList(weatherDays)
            mMainViewModel.weatherList = weatherList
            view.loadingIndicator.hide()
            view.weatherSwipeToRefresh.isRefreshing = false
        })

        view.weatherSwipeToRefresh.apply {
            setOnRefreshListener { fetchWeather() }
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.swipe_refresh_color_1),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_2),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_3),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_4)
            )
        }
    }

    override fun onStop() {
        mWeatherViewModel.cancelAllRequests()
        super.onStop()
    }

    private fun fetchWeather() {
        activity?.tryToRunFunctionOnInternet { mWeatherViewModel.fetchWeather() }
    }

}
