package com.example.bfaasubmission3.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataUserItems(
    var id: Int = 0,
    var username: String? = null,
    var name: String? = null,
    var repository: String? = null,
    var company: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var avatar: String? = null
):Parcelable