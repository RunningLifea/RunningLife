package com.example.runninglife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

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
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
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
}