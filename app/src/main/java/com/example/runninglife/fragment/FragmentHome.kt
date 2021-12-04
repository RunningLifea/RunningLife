package com.example.runninglife.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.runninglife.R
import com.example.runninglife.WarmUpActivity

class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val warmUp = view.findViewById<ImageButton>(R.id.imageButton3)

        warmUp.setOnClickListener {
            val warmUpIntent = Intent(activity, WarmUpActivity::class.java)
            startActivity(warmUpIntent)

        }

        return view
    }
}