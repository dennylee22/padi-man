package com.bangkit.capstone.padiman.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.desease.ListFragmnet
import com.bangkit.capstone.padiman.detector.DetectorrrActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClick()
    }

    private fun onClick(){
        btn_detector.setOnClickListener {
            Toast.makeText(this,"Deteksi Penyakit", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, DetectorrrActivity::class.java)
            startActivity(intent)
        }

        btn_disease.setOnClickListener {
            Toast.makeText(this,"Daftar Penyakit", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, ListFragmnet::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_about -> {
                Toast.makeText(this,"About Apk", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}