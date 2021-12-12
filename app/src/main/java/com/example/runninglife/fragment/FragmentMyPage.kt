package com.example.runninglife.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.runninglife.MainActivity
import com.example.runninglife.R
import com.example.runninglife.RunningLifeApplication
import com.example.runninglife.dao.User
import com.example.runninglife.retrofit.DataService
import retrofit2.Call
import retrofit2.Response

class FragmentMyPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_page, container, false)



        val realName = view.findViewById<TextView>(R.id.realName) // 실제 이름
        val editName = view.findViewById<TextView>(R.id.editName) // 수정 이름
        val edit_btn = view.findViewById<ImageView>(R.id.edit_btn) // 수정 버튼
        var check = false

        val nickname = RunningLifeApplication.prefs.getString("nickname", "")
        val dist1 = RunningLifeApplication.prefs.getString("distance", "")
        val time1 = RunningLifeApplication.prefs.getString("time", "")

        realName.text = nickname
        editName.text = nickname
        edit_btn.setOnClickListener {
            if (!check) {
                check = true
                editName.visibility = View.VISIBLE;
                realName.visibility = View.INVISIBLE


            } else {
                check = false

            }

        }

        val dist_neg = view.findViewById<ImageView>(R.id.dist_neg)
        val dist_pos = view.findViewById<ImageView>(R.id.dist_pos)
        val time_neg = view.findViewById<ImageView>(R.id.time_neg)
        val time_pos = view.findViewById<ImageView>(R.id.time_pos)
        var distance = view.findViewById<TextView>(R.id.distance)
        var time = view.findViewById<TextView>(R.id.time)
        var dist: Int = Integer.parseInt(distance.text.toString())
        var t : Int = Integer.parseInt(time.text.toString())

        distance.text = dist1
        time.text = time1
        dist_neg.setOnClickListener {
            if (dist >= 1) {
                dist -= 1
                distance.text = dist.toString()

            }}
        dist_pos.setOnClickListener {
            dist += 1
            distance.text = dist.toString()
            }



        time_neg.setOnClickListener {
            if (t >= 1) {
                t -= 1
                time.text = t.toString()
            }
            }
        time_pos.setOnClickListener {
            t += 1
            time.text = t.toString()
            }




        val complete = view.findViewById<Button>(R.id.edit_complete)

        complete.setOnClickListener {
            var user = User(editName.text.toString(), dist, t)

            Toast.makeText(context, "Nickname editted : ${editName.text} \n Distance editted : $dist \n Time editted : $t ", Toast.LENGTH_SHORT).show()
            DataService.userService.update(user, nickname).enqueue(object : retrofit2.Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (! response.isSuccessful) {
                        // 실패 - 이미 존재하는 아이디
                        editName.text = ""

                    }else {
                        RunningLifeApplication.prefs.setString("nickname", editName.text.toString())
                        RunningLifeApplication.prefs.setString("distance", dist.toString())
                        RunningLifeApplication.prefs.setString("time", t.toString())

                        realName.text = editName.text
                        realName.visibility = View.VISIBLE
                        editName.visibility = View.INVISIBLE;

                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("test", "failure")
                }

            })
        }

        return view
    }

}