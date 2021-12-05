package com.example.runninglife.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.runninglife.R
import com.example.runninglife.dao.Daily
import java.text.DecimalFormat
import java.time.LocalDateTime

class DailyExpandableAdapter (private val dailyList: List<Daily>) : RecyclerView.Adapter<DailyExpandableAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(daily: Daily) {
            val df = DecimalFormat("00")
            var check = true
            val text_title = itemView.findViewById<TextView>(R.id.txt_title_time)
            val btn_expand = itemView.findViewById<ImageButton>(R.id.img_more)
            val layout_title = itemView.findViewById<ConstraintLayout>(R.id.no_expanded)
            val layout_body = itemView.findViewById<ConstraintLayout>(R.id.layout_expanded)
            val text_daily_name = itemView.findViewById<TextView>(R.id.txt_name)
            val text_temperature = itemView.findViewById<TextView>(R.id.text_temperature)
            val text_location = itemView.findViewById<TextView>(R.id.text_location_daily)
            val text_time = itemView.findViewById<TextView>(R.id.text_clock)
            val img_temperature = itemView.findViewById<ImageView>(R.id.img_temperature)
            val text_expand_title = itemView.findViewById<TextView>(R.id.text_expand_title)
            val edit_daily_location = itemView.findViewById<EditText>(R.id.edit_location_daily)
            val time_set = itemView.findViewById<LinearLayout>(R.id.edit_time)
            val btn_setting = itemView.findViewById<ImageView>(R.id.setting)

            val edit_start_hour = itemView.findViewById<Spinner>(R.id.edit_start_hour)
            val edit_start_min = itemView.findViewById<Spinner>(R.id.edit_start_min)
            val edit_end_hour = itemView.findViewById<Spinner>(R.id.edit_end_hour)
            val edit_end_min = itemView.findViewById<Spinner>(R.id.edit_end_min)

            val hour_list = (0..24).toList()
            val min_list = (0..60).toList()

            val hour_adpater = ArrayAdapter(itemView.context, R.layout.spinner_time, hour_list)
            val min_adapter = ArrayAdapter(itemView.context, R.layout.spinner_time, min_list)

            edit_start_hour.adapter = hour_adpater
            edit_start_min.adapter = min_adapter
            edit_end_hour.adapter = hour_adpater
            edit_end_min.adapter = min_adapter

            text_expand_title.isClickable = true

            text_expand_title.text = daily.name
            text_daily_name.text = daily.name
            text_title.text = "${df.format(daily.start.hour)}:${df.format(daily.start.minute)} ~ ${df.format(daily.end.hour)}:${df.format(daily.end.minute)}"

            text_temperature.text = "${daily.temperature}°"
            text_location.text = daily.location
            text_time.text = "${df.format(daily.start.hour)}:${df.format(daily.start.minute)} ~ ${df.format(daily.end.hour)}:${df.format(daily.end.minute)}"

            if (daily.name != "Running") {
                text_temperature.visibility = View.GONE
                img_temperature.visibility = View.GONE
            }

            btn_expand.setOnClickListener {
                val show = toggleLayout(!daily.isExpanded, it, layout_title, layout_body)
                daily.isExpanded = show
            }

            layout_title.setOnClickListener {
                val show = toggleLayout(!daily.isExpanded, btn_expand, layout_title, layout_body)
                daily.isExpanded = show
            }

            btn_setting.setOnClickListener {
                if(check) {
                    text_location.visibility = View.GONE
                    edit_daily_location.visibility = View.VISIBLE
                    text_time.visibility = View.GONE
                    time_set.visibility = View.VISIBLE
                    if(daily.name == "Running"){
                        text_temperature.visibility = View.GONE
                        img_temperature.visibility = View.GONE
                    }

                    text_expand_title.text = "edit schedule"



                }else {
                    Log.d("test", "설정 완료")
                    text_expand_title.text = daily.name
                    text_location.visibility = View.VISIBLE
                    edit_daily_location.visibility = View.GONE
                    text_time.visibility = View.VISIBLE
                    time_set.visibility = View.GONE
                    if(daily.name == "Running"){
                        text_temperature.visibility = View.VISIBLE
                        img_temperature.visibility = View.VISIBLE
                    }

                }

                check = ! check

            }

            text_expand_title.setOnClickListener {
                if(!check) {
                    Log.d("test", "설정 완료")
                    text_expand_title.text = daily.name
                    text_location.visibility = View.VISIBLE
                    edit_daily_location.visibility = View.GONE
                    text_time.visibility = View.VISIBLE
                    time_set.visibility = View.GONE
                    if(daily.name == "Running"){
                        text_temperature.visibility = View.VISIBLE
                        img_temperature.visibility = View.VISIBLE
                    }
                    check = ! check
                }else {
                    if(daily.name == "Running"){
                        Log.d("test", "달리기")
                    }
                }
            }
        }

        private fun toggleLayout(isExpanded: Boolean, view: View, layoutTitle: ConstraintLayout, layoutBody: ConstraintLayout): Boolean {
            ToggleAnimation.toggleArrow(view, isExpanded)
            if (isExpanded) {
                ToggleAnimation.expand(layoutBody)
                ToggleAnimation.collapse(layoutTitle)
            } else {
                ToggleAnimation.expand(layoutTitle)
                ToggleAnimation.collapse(layoutBody)
            }
            return isExpanded
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyList[position])
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }
}