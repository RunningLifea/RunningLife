package com.example.runninglife.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.runninglife.R
import com.example.runninglife.WarmUpActivity
import com.example.runninglife.popup.PopupActivity
import com.example.runninglife.retrofit.WeatherService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)



        val warmUp = view.findViewById<ImageView>(R.id.btn_warm_up)

        warmUp.setOnClickListener {
            val warmUpIntent = Intent(activity, WarmUpActivity::class.java)
            startActivity(warmUpIntent)

        }
        val setgoal = view.findViewById<ImageView>(R.id.btn_setting)
        setgoal.setOnClickListener {
            val bottom = activity?.findViewById<BottomNavigationView>(R.id.bottom)
            bottom?.selectedItemId = R.id.mypage
        }

        val run = view.findViewById<ImageView>(R.id.btn_daily)

        run.setOnClickListener{
            val intentNRC = context?.packageManager?.getLaunchIntentForPackage("com.nike.plusgps")
            startActivity(intentNRC)
        }
        return view
    }

}


