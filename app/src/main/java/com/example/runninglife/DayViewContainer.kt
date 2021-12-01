package com.example.runninglife

import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)
}

class MonthViewContainer(view: View) : ViewContainer(view){
    val textView = view.findViewById<TextView>(R.id.calendarMonthText)
}

class MonthFooterContainer(view: View) : ViewContainer(view){
    val spinner = view.findViewById<Spinner>(R.id.progress_spinner)
}
