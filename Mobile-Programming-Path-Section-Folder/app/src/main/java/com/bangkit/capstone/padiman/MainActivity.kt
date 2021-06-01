package com.bangkit.capstone.padiman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bangkit.capstone.padiman.view.AboutActivity
import com.bangkit.capstone.padiman.view.DetectorActivity
import com.bangkit.capstone.padiman.view.DiseaseListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClick()
    }

    private fun onClick(){
        btn_detector.setOnClickListener {
            val intent = Intent(this@MainActivity, DetectorActivity::class.java)
            startActivity(intent)
        }

        btn_disease.setOnClickListener {
            val intent = Intent(this@MainActivity, DiseaseListActivity::class.java)
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
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}