package com.example.runninglife.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.runninglife.dao.WeatherResponse
import com.example.runninglife.R
import com.example.runninglife.RunningLifeApplication
import com.example.runninglife.adapter.DailyExpandableAdapter
import com.example.runninglife.adapter.HorizontalRecyclerCalendarAdapter
import com.example.runninglife.dao.Daily
import com.example.runninglife.dao.Day
import com.example.runninglife.popup.PopupActivity
import com.example.runninglife.retrofit.DataService
import com.example.runninglife.retrofit.WeatherService
import com.google.android.gms.location.*
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import com.tejpratapsingh.recyclercalendar.utilities.CalendarUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class FragmentDaily : Fragment() {

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    val client: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity)
    }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    lateinit var img_weather: ImageView
    lateinit var text_weather : TextView
    lateinit var text_location : TextView

    lateinit var geocoder: Geocoder

    private lateinit var adapter: DailyExpandableAdapter
    lateinit var item_recycler_view : RecyclerView
    lateinit var nickname: String
    lateinit var selectedDate:Date


    @SuppressLint("NewApi", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)

        nickname = RunningLifeApplication.prefs.getString("nickname", "")

        selectedDate = Date()
        val btn_edit_schedule = view.findViewById<Button>(R.id.btn_edit_schedule)
        btn_edit_schedule.setOnClickListener {
            //데이터 담아서 팝업(액티비티) 호출
            val popup = Intent(activity, PopupActivity::class.java)
            popup.putExtra("lat", lat)
            popup.putExtra("lon", lon)
            popup.putExtra("date", selectedDate)
            startActivityForResult(popup, 1)

        }

        item_recycler_view = view.findViewById(R.id.item_recycler)
        loadData(Date())

        img_weather = view.findViewById(R.id.img_weather)
        text_weather = view.findViewById(R.id.text_weather)
        text_location = view.findViewById(R.id.text_location)
        val weather = view.findViewById<LinearLayout>(R.id.weather)

        geocoder = Geocoder(activity)

        //check location condition
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // when permission is granted
            // Initialize location manager
            getCurrentLocation()
        } else {
            // when permission is not granted
            // request permission
            requestPermissions(
                listOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).toTypedArray(), 100
            )
        }


        val calendarRecyclerView: RecyclerView = view.findViewById(R.id.calendarRecyclerView)
        val textViewSelectedDate: TextView = view.findViewById(R.id.textViewSelectedDate)

        val date = Date()
        date.time = System.currentTimeMillis()

        val startCal = Calendar.getInstance()
        startCal.time = date
        startCal.add(Calendar.MONTH, -2)

        val endCal = Calendar.getInstance()
        endCal.time = date
        endCal.add(Calendar.MONTH, 3)

        val configuration: RecyclerCalendarConfiguration =
            RecyclerCalendarConfiguration(
                calenderViewType = RecyclerCalendarConfiguration.CalenderViewType.HORIZONTAL,
                calendarLocale = Locale.getDefault(),
                includeMonthHeader = true
            )

        configuration.weekStartOffset = RecyclerCalendarConfiguration.START_DAY_OF_WEEK.MONDAY

        textViewSelectedDate.text =
            CalendarUtils.dateStringFromFormat(
                locale = configuration.calendarLocale,
                date = date,
                format = "MMMM-YYYY"
            ) ?: ""

        val calendarAdapterHorizontal: HorizontalRecyclerCalendarAdapter =
            HorizontalRecyclerCalendarAdapter(
                startDate = startCal.time,
                endDate = endCal.time,
                configuration = configuration,
                selectedDate = date,
                dateSelectListener = object : HorizontalRecyclerCalendarAdapter.OnDateSelected {
                    override fun onDateSelected(date: Date) {
                        textViewSelectedDate.text =
                            CalendarUtils.dateStringFromFormat(
                                locale = configuration.calendarLocale,
                                date = date,
                                format = "MMMM-YYYY"
                            )
                                ?: ""

                        // 날짜 선택
                        getWeather(lat, lon)


                        if(date.date != Date().date){
                            weather.visibility = View.GONE
                        }else {
                            weather.visibility = View.VISIBLE
                        }

                        selectedDate = date
                        loadData(date)

                    }

                }
            )
        calendarRecyclerView.adapter = calendarAdapterHorizontal
        calendarRecyclerView.layoutManager?.scrollToPosition(60)

        val snapHelper = PagerSnapHelper() // Or LinearSnapHelper
        snapHelper.attachToRecyclerView(calendarRecyclerView)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1){
            if(resultCode==Activity.RESULT_OK){
                //데이터 받기
                val date = data?.getSerializableExtra("date") as LocalDate

                loadData(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))

            }
        }

    }

    private fun loadData(date: Date){

        val dailyList = ArrayList<Daily>()

        item_recycler_view.setHasFixedSize(true)
        item_recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = DailyExpandableAdapter(dailyList)
        item_recycler_view.adapter = adapter

        DataService.dayService.find(nickname, dateFormatter.format(date)).enqueue(object : Callback<List<Day>> {
            override fun onResponse(call: Call<List<Day>>, response: Response<List<Day>>) {

                    for (day in response.body()!!) {
                        dailyList.add(
                            Daily(
                                day.title,
                                day.date,
                                LocalTime.parse(day.start, DateTimeFormatter.ofPattern("HH:mm")),
                                LocalTime.parse(day.end, DateTimeFormatter.ofPattern("HH:mm")),
                                day.location,
                                day.temperature,
                                day.complete
                            )
                        )
                    }

                item_recycler_view.setHasFixedSize(true)
                item_recycler_view.layoutManager = LinearLayoutManager(context)
                adapter = DailyExpandableAdapter(dailyList)
                item_recycler_view.adapter = adapter

            }

            override fun onFailure(call: Call<List<Day>>, t: Throwable) {
            }

        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // check condition
        if (requestCode == 100 && (grantResults.isNotEmpty()) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            // when permission is granted
            // call method
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        // when permission is granted
        // Initialize location manager
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            // when location service is enabled
            // get last location
            client.lastLocation.addOnCompleteListener {
                if (it.result != null) {
                val location: Location = it.result
                // check condition
                if (location.latitude != 0.0 && location.longitude != 0.0) {
                    // when location result is not null
                    lat = location.latitude
                    lon = location.longitude
                    getWeather(lat, lon)
                }
                } else {
                    // when location result is null
                    // initialize location request

                    var locationRequest = LocationRequest.create()
                    locationRequest.run {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        interval = 10000
                        fastestInterval = 1000
                        numUpdates = 1
                    }

                    // initialize location call back
                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            val location1 = locationResult.lastLocation
                            lat = location1.latitude
                            lon = location1.longitude
                            getWeather(lat, lon)
                        }
                    }

                    // request location updates
                    client.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
            }
        } else {
            // when location service is not enabled
            // open location setting
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

    }


    // get current weather
    fun getWeather(lat: Double, lon: Double) {
        val APPID = "216f63e517f56ad4f20ba181f5bb04f5"
        val url = "https://api.openweathermap.org/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService = retrofit.create(WeatherService::class.java)
        weatherService.getCurrentWeatherData(lat.toString(), lon.toString(),APPID).enqueue(object: Callback<WeatherResponse>{
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                //todo 실패처리
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                //todo 성공처리

                if(response.isSuccessful.not()){
                    return
                }
                response.body()?.let{
                    val result_geo = geocoder.getFromLocation(lat, lon, 1)
                    val result_loc = result_geo[0].getAddressLine(0).split(",")
                    text_location.text = result_loc[result_loc.size-3] + ", " + result_loc[result_loc.size-2]

                    val weather = it.weather.get(index = 0).main
                    val cheering = view?.findViewById<TextView>(R.id.daily_hello)

                    when (weather.toString()) {
                        "Clouds" -> {
                            img_weather.setImageResource(R.drawable.clouds)
                            text_weather.text = "Cloudy"
                            cheering?.text ="Running in spare time!"
                        }
                        //현영이가 할일은 날씨가 맑을 때 러닝하라고 알려주는 메세지 띄우기!

                        "Clear" -> {
                            img_weather.setImageResource(R.drawable.clear)
                            text_weather.text = "Clear"
                            cheering?.text = "Nice weather for running!!!"
                        }
                        "Haze", "Mist", "Smoke", "Dust", "Fog", "Sand", "Ash", "Squall", "Tornado" ,"Atmosphere" -> {
                            img_weather.setImageResource(R.drawable.atmosphere)
                            text_weather.text = "Atmosphere"
                            cheering?.text = "Check the air condition!"
                        }
                        "Drizzle" -> {
                            img_weather.setImageResource(R.drawable.drizzle)
                            text_weather.text = "Drizzle"
                            cheering?.text = "Cheer up today!"
                        }
                        "Rain" -> {
                            img_weather.setImageResource(R.drawable.rain)
                            text_weather.text = "Rain"
                            cheering?.text = "Staying home with hardworking!"
                        }
                        "Snow" -> {
                            img_weather.setImageResource(R.drawable.snow)
                            text_weather.text = "Snow"
                            cheering?.text = "White day, Nice day"
                        }
                        "Thunderstorm" -> {
                            img_weather.setImageResource(R.drawable.thunderstrom)
                            text_weather.text = "Thunderstorm"
                            cheering?.text = "Passionate as thunder!"
                        }
                        else -> {
                            img_weather.setImageResource(R.drawable.loading)
                        }
                    }
                }
            }

        })


    }

}
