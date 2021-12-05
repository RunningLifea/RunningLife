package com.example.runninglife.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.runninglife.R

class FragmentMyPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        val editName = view.findViewById<TextView>(R.id.editName)
        val edit_btn = view.findViewById<ImageView>(R.id.edit_btn)
        val realName = view.findViewById<TextView>(R.id.realName)
        var check = false

        edit_btn.setOnClickListener{

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

    return view
    }
}