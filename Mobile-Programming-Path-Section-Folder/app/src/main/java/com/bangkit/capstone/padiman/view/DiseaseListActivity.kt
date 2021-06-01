package com.bangkit.capstone.padiman.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.adapter.ListDiseasesAdapter
import com.bangkit.capstone.padiman.model.Diseases
import com.bangkit.capstone.padiman.viewModel.DiseaseListViewModel
import kotlinx.android.synthetic.main.activity_disease_list.*

class DiseaseListActivity : AppCompatActivity() {

    private lateinit var adapter: ListDiseasesAdapter
    private lateinit var listViewModel: DiseaseListViewModel

    private lateinit var listDiseases: Diseases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_list)

        supportActionBar?.setTitle(R.string.daftar_penyakit)

        adapter = ListDiseasesAdapter()
        adapter.notifyDataSetChanged()

        listViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DiseaseListViewModel::class.java)



        listViewModel.getList().observe(this, Observer { listItem ->
            if(listItem != null){
                Log.d("listItem", listItem.toString())
                adapter.setData(listItem)
            }
        })


        listViewModel.navigateToDetails.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
        showRecyclerList()

        //fungsi back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showRecyclerList(){
        view_penyakit.layoutManager = LinearLayoutManager(this)
        view_penyakit.adapter = adapter

        adapter.setOnItemClickCallback(object : ListDiseasesAdapter.OnItemClickCallback{
            override fun onItemClicker(data: Diseases) {
                showSelectedList(data)
                val moveDetail = Intent(this@DiseaseListActivity, DetailActivity::class.java)
                moveDetail.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(moveDetail)
            }
        })
    }

    private fun showSelectedList(list: Diseases){
        Toast.makeText(this, list.penyakit, Toast.LENGTH_SHORT).show()
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