package com.example.runninglife.dao

import java.time.LocalDate


data class RunRecord(
    val date: LocalDate = LocalDate.of(0,0,0),
    val dist: Int = 0
)


