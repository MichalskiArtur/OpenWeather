package com.example.openweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var weatherData: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherData = findViewById(R.id.textView)

        findViewById<View>(R.id.button).setOnClickListener { getCurrentData() }

    }

    private fun getCurrentData() {

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder = Html.fromHtml("<b>Country:</b> " +
                            weatherResponse.sys!!.country +
                            "<br>" +
                            "<b>Temperature:</b> " +
                            (weatherResponse.main!!.temp - 273).toString().substring(0, 3) + " ºC" +
                            "<br>" +
                            "<b>Temperature(Min):</b> " +
                            (weatherResponse.main!!.tempMin - 273).toString().substring(0, 3) + " ºC" +
                            "<br>" +
                            "<b>Temperature(Max):</b> " +
                            (weatherResponse.main!!.tempMax - 273).toString().substring(0, 3) + " ºC" +
                            "<br>" +
                            "<b>Humidity:</b> " +
                            weatherResponse.main!!.humidity +
                            "<br>" +
                            "<b>Pressure:</b> " +
                            weatherResponse.main!!.pressure)

                    weatherData!!.text = stringBuilder
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherData!!.text = t.message
            }
        })
    }

    companion object {

        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "8cd5458b2845c395f6452f4e4047ffdd"
        var lat = "52"
        var lon = "0"
    }
}

