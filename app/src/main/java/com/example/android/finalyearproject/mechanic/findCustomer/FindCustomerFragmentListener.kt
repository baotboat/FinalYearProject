package com.example.android.finalyearproject.mechanic.findCustomer

import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng

interface FindCustomerFragmentListener {

    fun getSelectedFilter(filterTypeSelected: List<String>, filterDistanceSelected:Float? = null)

    fun computeDistanceFromLatLng(view: View, tvDistance: TextView, myLatLng: LatLng?, customerLatLng: LatLng?)
}