package com.example.runninglife.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.runninglife.DAO.runRecord
import com.example.runninglife.R


class FragmentMonthly : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_monthly, container, false)
        val date = view.findViewById<TextView>(R.id.select_date)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView2)
        var distance = view.findViewById<TextView>(R.id.distance)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date.text = ""+year+"년 "+ month+"월 "+dayOfMonth+"일"
            val data211122 = runRecord(2021, 11, 22, 4)

            distance.text = data211122.dist.toString() + "km"
        }
        return view
    }



}