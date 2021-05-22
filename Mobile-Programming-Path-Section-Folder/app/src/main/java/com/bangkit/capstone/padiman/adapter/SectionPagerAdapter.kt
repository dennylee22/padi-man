package com.bangkit.capstone.padiman.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bangkit.capstone.padiman.view.fragment.DiseasesListFragment
import com.bangkit.capstone.padiman.view.fragment.UploadFragment
import com.bangkit.capstone.padiman.R

class SectionPagerAdapter(private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.daftar_penyakit, R.string.upload_gambar)
    }

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> DiseasesListFragment()
        1 -> UploadFragment()
        else -> Fragment()
    }

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = TAB_TITLES.size
}