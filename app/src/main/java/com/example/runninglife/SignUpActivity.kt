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
    }
}