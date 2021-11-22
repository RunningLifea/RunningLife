package com.example.runninglife.DAO

import com.google.gson.annotations.SerializedName

// 필요한 객체의 이름
class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("name") var name : String? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon : String? = null

}