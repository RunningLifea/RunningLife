package com.example.runninglife.retrofit

import com.example.runninglife.dao.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface UserService {
    @POST("user/signUp")
    @JvmSuppressWildcards
    fun signUp(@Body user: User): Call<Unit>

    @GET("user/findByName")
    fun findByName(@Query("name") name:String) : Call<User>

    @POST("user/update")
    fun update(@Body user:User, @Query("name") name:String):Call<Unit>
}