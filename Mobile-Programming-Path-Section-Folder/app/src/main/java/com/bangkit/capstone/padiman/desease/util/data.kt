package com.bangkit.capstone.padiman.desease.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data(
    var poster:  Int? = 0,
    var id: String? = null,
    var res: String? = null
)  : Parcelable
