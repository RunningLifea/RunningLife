package com.example.runninglife.retrofit

import com.example.runninglife.dao.Record
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecordService {
    @POST("/record/upload")
    fun upload(@Body record: Record, @Query("name") name:String) : Call<Unit>

    @GET("/record/month")
    fun read(
        @Query("name") name: String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
    ) : Call<List<Record>>
}