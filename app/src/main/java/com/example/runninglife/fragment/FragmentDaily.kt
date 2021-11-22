package com.example.runninglife.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.runninglife.DAO.WeatherResponse
import com.example.runninglife.R
import com.example.runninglife.retrofit.WeatherService
import com.google.android.gms.location.*
import com.tejpratapsingh.recyclercalendar.model.RecyclerCalendarConfiguration
import com.tejpratapsingh.recyclercalendar.utilities.CalendarUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class FragmentDaily : Fragment() {

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    val client: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity)
    }

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)

        Log.d("test", "t")

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
                        Log.d("test", date.toString())
                        getWeather(lat, lon)
                    }

                }
            )
        calendarRecyclerView.adapter = calendarAdapterHorizontal
        calendarRecyclerView.layoutManager?.scrollToPosition(60)

        val snapHelper = PagerSnapHelper() // Or LinearSnapHelper
        snapHelper.attachToRecyclerView(calendarRecyclerView)


        return view
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
                val location: Location = it.result
                // check condition
                Log.d("test", location.latitude.toString())
                if (location.latitude != 0.0 && location.longitude != 0.0) {
                    // when location result is not null
                    lat = location.latitude
                    lon = location.longitude
                    Log.d("test", "loc1")
                } else {
                    // when location result is null
                    // initialize location request
                    var locationRequest = LocationRequest.create()
                    locationRequest.run {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        interval = 10
                        fastestInterval = 1000
                        numUpdates = 1
                    }

                    // initialize location call back
                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            val location1 = locationResult?.lastLocation

                            lat = location1?.latitude!!
                            lon = location1.longitude
                            Log.d("test", "loc2")
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
            Log.d("test", "weather")
            getWeather(lat, lon)
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
                Log.d("test",t.toString())
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                //todo 성공처리

                if(response.isSuccessful.not()){
                    return
                }
                response.body()?.let{

                    Log.d("test", lat.toString())
                    Log.d("test", lon.toString())
                    Log.d("test", it.name.toString())
                    Log.d("test",it.toString())
                    Log.d("test", it.weather.toString())
                }
            }

        })


    }

}
