package com.bangkit.capstone.padiman.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.capstone.padiman.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //fungsi back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    //back
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    //fungsi klik back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}