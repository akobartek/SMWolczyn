package pl.kapucyni.wolczyn.app.view.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.item_weather.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.WeatherRecord
import pl.kapucyni.wolczyn.app.utils.collapse
import pl.kapucyni.wolczyn.app.utils.expand
import pl.kapucyni.wolczyn.app.utils.getAttributeColor

class WeatherRecyclerAdapter : RecyclerView.Adapter<WeatherRecyclerAdapter.WeatherViewHolder>() {

    private var mWeatherList = arrayListOf<List<WeatherRecord>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false))

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) =
        holder.bindView(mWeatherList[position], position)

    override fun getItemCount(): Int = mWeatherList.size

    fun setWeatherList(list: ArrayList<List<WeatherRecord>>) {
        mWeatherList = list
        notifyDataSetChanged()
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(weatherRecords: List<WeatherRecord>, position: Int) {
            val date = weatherRecords[0].dt_txt.split(" ")[0].split("-")
            itemView.weatherDayText.text = "${date[2]}.${date[1]}.${date[0]}"
            val index = if (position == 0) 0 else weatherRecords.indexOf(weatherRecords.maxBy { it.main.temp })
            if (position == 0 && itemView.weatherCardToExpand.visibility == View.GONE) expandClick()
            val weatherPair = weatherValues[weatherRecords[index].weather[0].icon.substring(0, 2)]
                ?: Pair(R.drawable.ic_weather_clear_sky, R.string.clear_sky)
            loadChart(weatherRecords)
            itemView.weatherTemperature.text = weatherRecords[index].main.temp.toInt().toString()
            itemView.weatherIcon.setImageResource(weatherPair.first)
            itemView.weatherDescription.text = itemView.context.getString(weatherPair.second)

            itemView.weatherHeader.setOnClickListener { expandClick() }
            itemView.expandViewBtn.setOnClickListener { expandClick() }
        }

        private fun expandClick() {
            if (itemView.weatherCardToExpand.visibility == View.GONE) {
                itemView.weatherCardToExpand.expand()
                itemView.expandViewBtn.animate().rotation(180f).duration = 300
            } else {
                itemView.weatherCardToExpand.collapse()
                itemView.expandViewBtn.animate().rotation(0f).duration = 300
            }
        }

        private fun loadChart(weatherRecords: List<WeatherRecord>) {
            val hours = weatherRecords.map { it.dt_txt.split(" ")[1].substring(0, 5) }
            Log.d("xDDD", hours.toString())

            itemView.weatherChart.apply {
                isDragEnabled = false
                setDrawGridBackground(false)
                setScaleEnabled(false)
                setPinchZoom(false)
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                axisLeft.axisMinimum = (weatherRecords.minBy { it.main.temp }!!.main.temp - 4).toFloat()
                axisRight.axisMinimum = (weatherRecords.minBy { it.main.temp }!!.main.temp - 4).toFloat()
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawLabels(true)
                    textColor = itemView.context.getAttributeColor(R.attr.colorText)
                    valueFormatter = IndexAxisValueFormatter(hours)
                }
                legend.isEnabled = false
                description.isEnabled = false
                offsetLeftAndRight(0)
            }

            val chartValues = weatherRecords.mapIndexed { i, record ->
                Entry(
                    i.toFloat(),
                    record.main.temp.toFloat(),
                    ResourcesCompat.getDrawable(
                        itemView.context.resources,
                        weatherValues[record.weather[0].icon.substring(0, 2)]?.first ?: R.drawable.ic_weather_clear_sky,
                        null
                    )
                )
            }
            val lineDataSet = LineDataSet(chartValues, itemView.context.getString(R.string.menu_weather))
            lineDataSet.apply {
                setDrawIcons(true)
                iconsOffset.y = 16f
                lineWidth = 0.1f
                setDrawFilled(true)
                fillColor = ResourcesCompat.getColor(itemView.resources, R.color.colorLineChart, null)
                setDrawCircles(false)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String = value.toInt().toString()
                }
                valueTextSize = 16f
                valueTextColor = itemView.context.getAttributeColor(R.attr.colorText)
            }

            itemView.weatherChart.apply {
                data = LineData(arrayListOf<ILineDataSet>(lineDataSet))
                animateX(300)
            }
        }
    }

    companion object {
        val weatherValues = hashMapOf(
            "01" to Pair(R.drawable.ic_weather_clear_sky, R.string.clear_sky),
            "02" to Pair(R.drawable.ic_weather_few_clouds, R.string.few_clouds),
            "03" to Pair(R.drawable.ic_weather_scattered_clouds, R.string.scattered_clouds),
            "04" to Pair(R.drawable.ic_weather_broken_clouds, R.string.broken_clouds),
            "09" to Pair(R.drawable.ic_weather_shower_rain, R.string.shower_rain),
            "10" to Pair(R.drawable.ic_weather_rain, R.string.rain),
            "11" to Pair(R.drawable.ic_weather_thunderstorm, R.string.thunderstorm),
            "13" to Pair(R.drawable.ic_weather_snow, R.string.snow),
            "50" to Pair(R.drawable.ic_weather_mist, R.string.mist)
        )
    }
}