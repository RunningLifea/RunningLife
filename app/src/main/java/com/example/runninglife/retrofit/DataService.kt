package com.example.runninglife.retrofit

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class DataService {

    companion object {
        val serverURL = "http://3.131.200.153:8080/"
        val localURl = "http://10.0.2.2:8080/"

        var gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(serverURL)
            .build()

        val userService : UserService = retrofit.create(UserService::class.java)
        val dayService : DayService = retrofit.create(DayService::class.java)
    }



}