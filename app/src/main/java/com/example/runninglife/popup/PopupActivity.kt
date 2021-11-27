package com.example.runninglife.popup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import com.example.runninglife.R

class PopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_daily)

        val text = findViewById<TextView>(R.id.txtText)

        val data = intent.getStringExtra("data")
        text.text = data

        val btn_daily_dialog = findViewById<Button>(R.id.btn_daily_dialog)
        btn_daily_dialog.setOnClickListener {
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