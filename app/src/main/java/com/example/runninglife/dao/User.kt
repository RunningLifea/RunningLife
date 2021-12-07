package com.example.runninglife.dao

import java.io.Serializable

data class User(
    val name: String = "",

    val distance: Int = 0,

    val time: Int = 0

) : Serializable
