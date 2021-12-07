package com.example.runninglife.dao

import java.time.LocalTime

data class Daily(
    var name : String = "",
    var date: String = "",
    var start: LocalTime = LocalTime.of(0,0,0),
    var end : LocalTime = LocalTime.of(0,0,0),
    var location : String = "",
    var temperature : Int = 0,
    var complete : Boolean = false,
    var isExpanded: Boolean = false
)
