package com.example.runninglife.dao

import com.google.gson.annotations.SerializedName

// 필요한 객체의 이름
class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("name") var name : String? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null

}