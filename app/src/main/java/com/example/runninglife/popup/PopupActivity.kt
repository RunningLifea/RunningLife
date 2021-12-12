package com.example.runninglife.popup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.widget.*
import com.example.runninglife.R
import com.example.runninglife.RunningLifeApplication
import com.example.runninglife.dao.Day
import com.example.runninglife.dao.Record
import com.example.runninglife.retrofit.DataService
import com.example.runninglife.retrofit.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class PopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_daily)

        val lat = intent.getStringExtra("lat").toString()
        val lon = intent.getStringExtra("lon").toString()

        val nickname = RunningLifeApplication.prefs.getString("nickname", "")

        val text = findViewById<EditText>(R.id.edit_title)
        val location = findViewById<EditText>(R.id.edit_location)

        val date = (intent.getSerializableExtra("date") as Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        val df = DecimalFormat("00")

        if (date != null) {
            Log.d("test", date.toString())
        }

        val hour_list = (0..24).toList()
        val min_list = (0..60).toList()

        val hour = LocalDateTime.now().hour
        val min = LocalDateTime.now().minute

        val hour_adpater = ArrayAdapter(this, R.layout.spinner_time, hour_list)
        val min_adapter = ArrayAdapter(this, R.layout.spinner_time, min_list)

        // time spinner 제작
        val start_hour = findViewById<Spinner>(R.id.add_start_hour)
        val start_min = findViewById<Spinner>(R.id.add_start_min)

        start_hour.adapter = hour_adpater
        start_min.adapter = min_adapter

        start_hour.setSelection(hour_list.indexOf(hour))
        start_min.setSelection(hour_list.indexOf(min))

        val end_hour = findViewById<Spinner>(R.id.add_end_hour)
        val end_min = findViewById<Spinner>(R.id.add_end_min)

        end_hour.adapter = hour_adpater
        end_min.adapter = min_adapter

        end_hour.setSelection(hour_list.indexOf(hour))
        end_min.setSelection(hour_list.indexOf(min))


        val btn_daily_dialog = findViewById<Button>(R.id.btn_daily_dialog)
        btn_daily_dialog.setOnClickListener {
            if (text.text.isEmpty() || location.text.isEmpty()) {
                finish()
            } else {
                val start = "${df.format(start_hour.selectedItem)}:${df.format(start_min.selectedItem)}"
                val end = "${df.format(end_hour.selectedItem)}:${df.format(end_min.selectedItem)}"

                // unixtime
                val uniTime =  SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(date.toString()).time.toString()

                finish()

                // 시간 별 날씨 불러오기
                val APPID = "216f63e517f56ad4f20ba181f5bb04f5"
                val url = "https://api.openweathermap.org/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val weatherService = retrofit.create(WeatherService::class.java)

                Thread(Runnable {
                    kotlin.run () {
                        Log.d("test",
                            weatherService.getHourlyTemperature(lat, lon, uniTime, APPID).execute().body()
                                .toString()
                        )
                    }
                }).start()

                Thread.sleep(1000)

                val day = Day(text.text.toString(),  start, end, date.toString(), location.text.toString())

                DataService.dayService.upload(day, nickname).enqueue(object : Callback<Day>{
                    override fun onResponse(call: Call<Day>, response: Response<Day>) {
                        if (response.isSuccessful) {
                            val new_intent = Intent()
                            new_intent.putExtra("date", date);
                            setResult(RESULT_OK, new_intent);

                            //토스트 띄우기
                            Toast.makeText(this@PopupActivity, "Upload Success", Toast.LENGTH_LONG).show()

                            //액티비티(팝업) 닫기
                            finish();
                        }else{
                            //토스트 띄우기
                            Toast.makeText(this@PopupActivity, "Existing Schedule", Toast.LENGTH_LONG).show()

                        }

                    }

                    override fun onFailure(call: Call<Day>, t: Throwable) {
                        Log.d("test", "fail")
                    }

                })
            }

        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_OUTSIDE){
            return false
        }
        return true
    }

    override fun onBackPressed() {
        finish()
    }
}