package com.example.runninglife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.runninglife.dao.User
import com.example.runninglife.retrofit.DataService
import com.example.runninglife.retrofit.UserService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val nickname = findViewById<TextView>(R.id.nickname).text

        val img_dist_plus = findViewById<ImageView>(R.id.dist_plus)
        val img_dist_minus = findViewById<ImageView>(R.id.dist_minus)
        img_dist_plus.setOnClickListener {
            Log.d("test", "plus")
        }

        img_dist_minus.setOnClickListener {
            Log.d("test", "minus")
        }

        val btn_signUp = findViewById<Button>(R.id.btn_signUp)
        btn_signUp.setOnClickListener {
            //Log.d("test", nickname.toString())
            RunningLifeApplication.prefs.setString("nickname", nickname.toString())

            val user  = User(nickname.toString(), 10, 2)

            // data service
            DataService.userService.signUp(user).enqueue(object : retrofit2.Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (! response.isSuccessful) {
                        // 실패 - 이미 존재하는 아이디
                    }else {
                        load_main()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("test", "failure")
                }

            })



        }
        val dist_plus = findViewById<ImageView>(R.id.dist_plus)
        var howlong = findViewById<TextView>(R.id.howlong)
        var target_dist: Int = Integer.parseInt(howlong.text.toString())
        val dist_minus = findViewById<ImageView>(R.id.dist_minus)


        dist_plus.setOnClickListener {
            if (target_dist >= 1) {
                target_dist -= 1
                howlong.text = target_dist.toString()

            }
            dist_minus.setOnClickListener {
                target_dist += 1
                howlong.text = target_dist.toString()
            }


        }
        val time_plus = findViewById<ImageView>(R.id.time_plus)
        var howmany = findViewById<TextView>(R.id.howmany)
        var target_time: Int = Integer.parseInt(howmany.text.toString())
        val time_minus = findViewById<ImageView>(R.id.time_minus)

        time_plus.setOnClickListener {
            if (target_time >= 1) {
                target_time -= 1
                howmany.text = target_time.toString()

            }
            time_minus.setOnClickListener {
                target_time += 1
                howmany.text = target_time.toString()
            }


        }

    }

    private fun load_main() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}