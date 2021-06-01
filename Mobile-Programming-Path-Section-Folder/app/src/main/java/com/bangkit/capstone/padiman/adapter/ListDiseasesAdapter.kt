package com.bangkit.capstone.padiman.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.padiman.model.Diseases
import com.bangkit.capstone.padiman.R
import kotlinx.android.synthetic.main.item_row.view.*

class ListDiseasesAdapter : RecyclerView.Adapter <ListDiseasesAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback : OnItemClickCallback
    private val mData = ArrayList<Diseases>()

    inner class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(listDiseases: Diseases){
            with(itemView){
                text_penyakit.text = listDiseases.penyakit

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicker(listDiseases)
                }
            }
        }
    }

    override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            viewType: Int
    ): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(listViewHolder: ListViewHolder, position: Int) {
        listViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    interface OnItemClickCallback {
        fun onItemClicker(data: Diseases)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<Diseases>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
}