package com.example.android.finalyearproject.customer.profile.changeProfile

import android.net.Uri

interface ProfileFragmentListener {

    fun changeUserData(desc: String, address: String, phone:String,  type: List<String>? = null)

    fun changeProfileImage(uri: Uri)
}