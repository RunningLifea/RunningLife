package com.example.runninglife.dao

// 필요한 객체의 이름
class WeatherResponse(){
    var weather = ArrayList<Weather>()
    var name : String? = null
}

class Weather {
    var id: Int = 0
    var main : String? = null

}