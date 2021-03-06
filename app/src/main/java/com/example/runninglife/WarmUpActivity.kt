package com.example.runninglife


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.view.circulartimerview.CircularTimerListener
import com.view.circulartimerview.CircularTimerView
import com.view.circulartimerview.TimeFormatEnum


class WarmUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warm_up)

        val play = findViewById<ImageView>(R.id.start)
        var state = true

        var i = 0
        val warmUpList = listOf<String>( "Stretch your legs!", "Stretch your arms!", "Waist warming up!", "Hamstring warming up!")
        val subtitle = findViewById<TextView>(R.id.subtitle_warming_up)
        val next = findViewById<TextView>(R.id.text_next)
        val photo = findViewById<ImageView>(R.id.warmup1)
        val imgList = listOf(R.drawable.warm_up1, R.drawable.arm, R.drawable.waist, R.drawable.ham)

        val  progressBar = findViewById<CircularTimerView>(R.id.progress_circular)
        progressBar.progress = 0F

        // To Initialize Timer
        progressBar.setCircularTimerListener(object : CircularTimerListener {
            override fun updateDataOnTick(remainingTimeInMs: Long): String? {
                return ((remainingTimeInMs/1000f).toInt()+1).toString()
            }

            override fun onTimerFinished() {
                i += 1
                progressBar.setSuffix("")


                if (i == warmUpList.size-1){
                    progressBar.setText("END !!")
                    next.text = "Let's running !!"
                    play.setOnClickListener {
                        // "running 이동"
                    }
                } else {
                    subtitle.text = warmUpList.get(i)
                    progressBar.setText("NEXT !!")
                    next.text = warmUpList.get(i+1)
                    photo.setImageResource(imgList.get(i))

                }

                play.setImageResource(R.drawable.warm_up_play)
                state = true
            }
        }, 10, TimeFormatEnum.SECONDS, 10)


        // To start timer
        play.setOnClickListener {
            if (state) {
                progressBar.setSuffix(" sec")
                progressBar.startTimer()
                play.setImageResource(R.drawable.warm_up_stop)
                state = false
            }else {
                progressBar.stopTimer()
                play.setImageResource(R.drawable.warm_up_play)
                state = true
            }
        }




    }
}