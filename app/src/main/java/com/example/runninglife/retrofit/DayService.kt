package com.example.runninglife.retrofit


import com.example.runninglife.dao.Day
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DayService {
    @POST("/day/upload")
    fun upload(@Body day: Day, @Query("name") name:String) : Call<Day>

    @GET("/day/find")
    fun find(@Query("name") name:String, @Query("date") date:String) : Call<List<Day>>

    @POST("/day/update")
    fun update(@Body map: Map<String, Day>, @Query("name") name:String): Call<Day>

    @POST("/day/complete")
    fun complete(@Body day:Day, @Query("name") name : String) : Call<Day>
}