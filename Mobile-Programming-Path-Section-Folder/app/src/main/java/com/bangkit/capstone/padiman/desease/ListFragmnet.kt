package com.bangkit.capstone.padiman.desease

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.desease.util.DataAdapter
import com.bangkit.capstone.padiman.desease.util.data
import kotlinx.android.synthetic.main.activity_list_fragmnet.*

class ListFragmnet  : AppCompatActivity() {

    private lateinit var adapter: DataAdapter
    private lateinit var dataAvatar: TypedArray
    private lateinit var dataid: Array<String>
    private lateinit var datares: Array<String>
    private var users = ArrayList<data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_fragmnet)

        adapter = DataAdapter(this)
        lv_user.adapter = adapter

        prepare()
        addItem()

        lv_user.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(this@ListFragmnet, users[position].id, Toast.LENGTH_SHORT).show()

            val userDetail = data()
            userDetail.poster = dataAvatar.getResourceId(position, -1)
            userDetail.id = dataid[position]
            userDetail.res = datares[position]

            val detailIntent = Intent(this@ListFragmnet, DetailList::class.java)
            detailIntent.putExtra(DetailList.EXTRA_USER, userDetail)
            startActivity(detailIntent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun prepare() {
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
        dataid = resources.getStringArray(R.array.id)
        datares = resources.getStringArray(R.array.res)
}

    private fun addItem() {
        for (position in dataid.indices){
            val user = data(
                dataAvatar.getResourceId(position, -1),
                dataid[position],
                datares[position]
            )
            users.add(user)
        }
        adapter.users = users
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

