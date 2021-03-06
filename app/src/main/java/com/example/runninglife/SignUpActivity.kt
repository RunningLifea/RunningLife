package com.example.runninglife

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.runninglife.dao.User
import com.example.runninglife.retrofit.DataService
import retrofit2.Call
import retrofit2.Response

import java.util.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val nickname = findViewById<TextView>(R.id.nickname)

        val dist_plus = findViewById<ImageView>(R.id.dist_plus)
        val dist_minus = findViewById<ImageView>(R.id.dist_minus)
        var howlong = findViewById<TextView>(R.id.howlong)
        var target_dist: Int = Integer.parseInt(howlong.text.toString())


        dist_plus.setOnClickListener {
            target_dist += 1
            howlong.text = target_dist.toString()
        }

        dist_minus.setOnClickListener {
            if (target_dist >=1){
                target_dist -= 1
                howlong.text = target_dist.toString()
            }
        }

        val time_plus = findViewById<ImageView>(R.id.time_plus)
        val time_minus = findViewById<ImageView>(R.id.time_minus)
        var howmany = findViewById<TextView>(R.id.howmany)
        var target_time: Int = Integer.parseInt(howmany.text.toString())

        time_plus.setOnClickListener {
            target_time += 1
            howmany.text = target_time.toString()
        }

        time_minus.setOnClickListener {
            if (target_time >=1){
                target_time -= 1
                howmany.text = target_time.toString()
            }
        }

        val btn_signUp = findViewById<Button>(R.id.btn_signUp)
        btn_signUp.setOnClickListener {
            RunningLifeApplication.prefs.setString("nickname", nickname.text.toString())

            val user  = User(nickname.text.toString(), target_dist, target_time)

            // data service
            DataService.userService.signUp(user).enqueue(object : retrofit2.Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (! response.isSuccessful) {
                        // ?????? - ?????? ???????????? ?????????
                        nickname.text = ""
                        nickname.hint = "existing nickname"
                        nickname.setHintTextColor(Color.RED)
                    }else {
                        RunningLifeApplication.prefs.setString("distance", target_dist.toString())
                        RunningLifeApplication.prefs.setString("time", target_time.toString())

                        load_main()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("test", "failure")
                }

            })


        }

    }

    private fun load_main() {


        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}