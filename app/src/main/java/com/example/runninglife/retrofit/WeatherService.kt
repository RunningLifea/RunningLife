package com.example.runninglife.retrofit

import com.example.runninglife.dao.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String) :
            Call<WeatherResponse>

    @GET("data/2.5/onecall/timemachine")
    fun getHourlyTemperature(
        @Query("lat") lat: String,
        @Query("lon") lon : String,
        @Query("dt") dt : String,
        @Query("appid") appid: String
        ) : Call<Any>

}