package com.example.runninglife.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.runninglife.R
import com.example.runninglife.RunningLifeApplication
import com.example.runninglife.retrofit.DataService

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



        realName.text = nickname
        editName.text = nickname
        edit_btn.setOnClickListener {
            if (!check) {
                check = true
                editName.visibility = View.VISIBLE;
                realName.visibility = View.INVISIBLE

            } else {
                check = false
                realName.text = editName.text
                realName.visibility = View.VISIBLE
                editName.visibility = View.INVISIBLE;
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
        dist_neg.setOnClickListener {
            if (dist >= 1) {
                dist -= 1
                distance.text = dist.toString()

            }
            dist_pos.setOnClickListener {
                dist += 1
                distance.text = dist.toString()
            }


        }
        time_neg.setOnClickListener {
            if (t >= 1) {
                t -= 1
                time.text = t.toString()

            }
            time_pos.setOnClickListener {
                t += 1
                time.text = t.toString()
            }


        }
        return view
    }
}