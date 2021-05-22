package com.bangkit.capstone.padiman.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.capstone.padiman.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setTitle(R.string.detail)
    }
}