package com.example.runninglife

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)
}

class MonthViewContainer(view: View) : ViewContainer(view){
    val textView = view.findViewById<TextView>(R.id.calendarMonthText)
}
