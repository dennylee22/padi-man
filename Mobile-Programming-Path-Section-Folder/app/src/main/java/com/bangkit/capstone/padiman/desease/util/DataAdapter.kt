package com.bangkit.capstone.padiman.desease.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.desease.DetailList
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_user.view.*

class DataAdapter  internal constructor(private val context: Context): BaseAdapter() {

    internal var users = arrayListOf<data>()

    override fun getItem(position: Int): Any { return users[position] }
    override fun getItemId(position: Int): Long { return position.toLong() }
    override fun getCount(): Int { return users.size }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_list, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as data
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder constructor(private val view: View){
        internal fun bind(user: data){
            with(view){
                tv_title.text = user.id
                iv_poster_list.setImageResource(user.poster!!)
            }
        }
    }
}
