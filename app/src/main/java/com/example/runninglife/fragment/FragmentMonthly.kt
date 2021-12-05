package com.example.runninglife.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.runninglife.DayViewContainer
import com.example.runninglife.MonthFooterContainer
import com.example.runninglife.MonthViewContainer
import com.example.runninglife.R
import com.example.runninglife.dao.RunRecord
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import org.w3c.dom.Text
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


class FragmentMonthly : Fragment() {

    private lateinit var datalist: List<RunRecord>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_monthly, container, false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView2)


        datalist = loadData()
        var datelist = ArrayList<LocalDate>()


        for (data in datalist){
            val d = LocalDate.of(data.date.year, data.date.month, data.date.dayOfMonth)
            datelist.add(d)
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            @SuppressLint("SetTextI18n")
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                // 데이터 불러와서 키로수 뒤에 붙여주기
                if(day.date in datelist){
                    container.textView.text = day.date.dayOfMonth.toString() + "\n${datalist[datelist.indexOf(day.date)].dist}km"
                }else{
                    container.textView.text = day.date.dayOfMonth.toString() + "\n"
                }
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.BLACK)
                } else {
                    container.textView.setTextColor(Color.GRAY)
                }
            }
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer>{

            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"

            }

            override fun create(view: View) =  MonthViewContainer (view)
        }

        calendarView.monthFooterBinder = object  : MonthHeaderFooterBinder<MonthFooterContainer>{
            override fun bind(container: MonthFooterContainer, month: CalendarMonth) {
                val i = month.weekDays.size.toString()
                val items = mutableListOf<String>("Week 1", "Week 2", "Week 3", "Week 4", "Week 5")
                if (i == "6") {
                    items.add("Week 6")
                }
                val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, items)

                val spinner = container.spinner
                spinner.adapter = spinnerAdapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val selectedText : TextView? = parent?.findViewById(R.id.spinner_text)
                        selectedText?.typeface = Typeface.DEFAULT_BOLD
                        selectedText?.setTextSize(Dimension.SP, 20F)
                        selectedText?.setPadding(90,0,0,0)


                        when(position) {
                            0 ->{
                                Log.d("test", "week1")
                            }
                            1 ->{
                                Log.d("test", "week2")
                            }
                            2 ->{
                                Log.d("test", "week3")
                            }
                            3 ->{
                                Log.d("test", "week4")
                            }
                            4 ->{
                                Log.d("test", "week5")
                            }
                            else -> {
                                Log.d("test", "week6")
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.d("test", "nothing")
                    }
                }
                spinner.setSelection(2)

            }

            override fun create(view: View) = MonthFooterContainer(view)

        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val daysOfWeek = arrayOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
        calendarView.setup(firstMonth, lastMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)



        return view
    }

    private fun loadData() : List<RunRecord> {
        val data = ArrayList<RunRecord>()

        val data1 = RunRecord(LocalDate.of(2021,11,28),1)
        val data2 = RunRecord(LocalDate.of(2021, 11, 25),3)

        data.add(data1)
        data.add(data2)

        return data
    }


}