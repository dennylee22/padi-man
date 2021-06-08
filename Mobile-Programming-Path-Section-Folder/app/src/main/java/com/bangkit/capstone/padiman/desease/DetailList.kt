package com.bangkit.capstone.padiman.desease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.desease.util.data
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_list3.*

class DetailList : AppCompatActivity(){
    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list3)

        val user= intent.getParcelableExtra<data>(EXTRA_USER)
        Glide.with(this).load(user?.poster).into(iv_ava )
        tv_title.text = user?.id
        tv_overview.text = user?.res


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //back
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    //fungsi back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

