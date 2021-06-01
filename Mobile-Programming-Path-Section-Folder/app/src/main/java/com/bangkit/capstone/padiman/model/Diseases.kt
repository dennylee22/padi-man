package com.bangkit.capstone.padiman.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Diseases(
        var id: Int = 0,
        var penyakit: String? = null,
        var solusi: String? = null
): Parcelable