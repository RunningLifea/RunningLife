package com.example.runninglife


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        RunningLifeApplication.prefs.clear()

        val getContent = this

        val splashText = findViewById<TextView>(R.id.splashText)
        val slowly_appear = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        splashText.animation = slowly_appear

        val nickname = RunningLifeApplication.prefs.getString("nickname", "")


        splashText.animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // 소리 실행?
            }
            override fun onAnimationEnd(animation: Animation?) {
                if(nickname != ""){
                    val mainIntent = Intent(getContent, MainActivity::class.java)
                    startActivity(mainIntent)
                }else{
                    // 가입 activity 이동
                    val signUpIntent = Intent(getContent, SignUpActivity::class.java)
                    startActivity(signUpIntent)
                }
            }
            override fun onAnimationRepeat(animation: Animation?) {
            }

        })




    }

}