package com.example.runninglife.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.fragment.app.Fragment
import com.example.runninglife.*
import com.example.runninglife.dao.Record
import com.example.runninglife.dao.User
import com.example.runninglife.retrofit.DataService
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.security.auth.callback.Callback


class FragmentMonthly : Fragment() {

    private lateinit var datalist: List<Record>
    private lateinit var nickname : String
    private lateinit var datelist : ArrayList<LocalDate>
//Running으로 일정 등록 하면 온도 뜨게 함.
    private val df = DecimalFormat("00")

    private val distance = RunningLifeApplication.prefs.getString("distance","0").toInt()
    private val time = RunningLifeApplication.prefs.getString("time", "0").toInt()

    private lateinit var progress_bar : ProgressBar
    private lateinit var progress_text : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_monthly, container, false)

        nickname = RunningLifeApplication.prefs.getString("nickname", "")

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView2)



        datalist = loadData()

        datelist = ArrayList<LocalDate>()

        for (data in datalist){
            val d = LocalDate.parse(data.date, DateTimeFormatter.ISO_DATE)
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
                    container.textView.text = day.date.dayOfMonth.toString() + "\n${datalist[datelist.indexOf(day.date)].distance}km"
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

                progress_bar = container.view.findViewById(R.id.progress_bar)
                progress_text = container.view.findViewById(R.id.progress_text)

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
                                setProgress(month.weekDays[0])
                            }
                            1 ->{
                                setProgress(month.weekDays[1])
                            }
                            2 ->{
                                setProgress(month.weekDays[2])
                            }
                            3 ->{
                                setProgress(month.weekDays[3])
                            }
                            4 ->{
                                setProgress(month.weekDays[4])
                            }
                            else -> {
                                setProgress(month.weekDays[5])
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.d("test", "nothing")
                    }
                }
                spinner.setSelection(0)

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

    @SuppressLint("SetTextI18n")
    private fun setProgress(list: List<CalendarDay>) {
        var total_dist = 0
        for (day in list) {
            if (datelist.contains(day.date)) {
                for (r in datalist) {
                    if (r.date == day.date.toString()) {
                        total_dist += r.distance
                    }
                }
            }

        }


        progress_bar.progress = df.format((total_dist.toDouble()/distance)*100).toInt()
        progress_text.text = "${df.format((total_dist.toDouble()/distance)*100)} %"
    }

    private fun loadData() : ArrayList<Record> {
        var data = ArrayList<Record>()

        Thread(Runnable {
            kotlin.run () {
                    data = DataService.recordService.read(nickname, "2021-01-01", "2022-12-01").execute().body() as ArrayList<Record>
            }
        }).start()

        Thread.sleep(1000)

        
        return data
    }


}