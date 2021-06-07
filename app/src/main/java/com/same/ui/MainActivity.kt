package com.same.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.timecat.demo.element.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DefaultExampleFragment())
            .addToBackStack("")
            .commit()
    }
}