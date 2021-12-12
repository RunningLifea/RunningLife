package com.example.runninglife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.runninglife.adapter.DailyExpandableAdapter
import com.example.runninglife.fragment.FragmentDaily
import com.example.runninglife.fragment.FragmentHome
import com.example.runninglife.fragment.FragmentMonthly
import com.example.runninglife.fragment.FragmentMyPage
import com.example.runninglife.retrofit.DataService
import com.example.runninglife.retrofit.WeatherService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val fragmentHome =  FragmentHome()
    private val fragmentDaily = FragmentDaily()
    private val fragmentMonthly = FragmentMonthly()
    private val fragmentMyPage = FragmentMyPage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationBar()

    }

    private fun initNavigationBar() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom)
        bottomNavigation.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(fragmentHome)
                    }
                    R.id.daily -> {
                        changeFragment(fragmentDaily)
                    }
                    R.id.monthly -> {
                        changeFragment(fragmentMonthly)
                    }
                    R.id.mypage -> {
                        changeFragment(fragmentMyPage)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }


}