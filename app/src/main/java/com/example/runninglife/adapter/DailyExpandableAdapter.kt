package com.example.runninglife.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.runninglife.R
import com.example.runninglife.dao.Daily

class DailyExpandableAdapter (private val dailyList: List<Daily>) : RecyclerView.Adapter<DailyExpandableAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(daily: Daily) {
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

            text_daily_name.text = daily.name
            text_title.text = "${daily.start.hour}:${daily.start.minute} ~ ${daily.end.hour}:${daily.end.minute}"

            text_temperature.text = "${daily.temperature}°"
            text_location.text = daily.location
            text_time.text = "${daily.start.hour}:${daily.start.minute} ~ ${daily.end.hour}:${daily.end.minute}"

            if (daily.name != "Running") {
                text_temperature.visibility = View.GONE
                img_temperature.visibility = View.GONE
                text_expand_title.text = daily.name
            } else {
                text_expand_title.isClickable = true
                text_expand_title.setOnClickListener {
                    Log.d("test", "test")
                }
            }

            btn_expand.setOnClickListener {
                val show = toggleLayout(!daily.isExpanded, it, layout_title, layout_body)
                daily.isExpanded = show
            }

            layout_title.setOnClickListener {
                val show = toggleLayout(!daily.isExpanded, btn_expand, layout_title, layout_body)
                daily.isExpanded = show
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