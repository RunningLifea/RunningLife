package com.example.runninglife.dao

import java.io.Serializable

data class Day (
    var title: String = "",
    var start: String = "",
    var end: String = "",
    var date: String = "",
    var location: String = "",
    var temperature: Int = 0,
    var complete: Boolean = false
) : Serializable
