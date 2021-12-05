package com.example.runninglife.popup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
import com.example.runninglife.R
import java.time.LocalDateTime

class PopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_daily)

        val text = findViewById<EditText>(R.id.edit_title)

        val data = intent.getStringExtra("data")

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
            Log.d("test", start_hour.selectedItem.toString()) // 선택된 아이템 불러오기
            val new_intent = Intent()
            new_intent.putExtra("result", "Close Popup");
            setResult(RESULT_OK, new_intent);

            //액티비티(팝업) 닫기
            finish();

        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_OUTSIDE){
            return false
        }
        return true
    }

    override fun onBackPressed() {
        return
    }
}