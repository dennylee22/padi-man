package com.bangkit.capstone.padiman.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.adapter.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs_layout.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f

    }
}