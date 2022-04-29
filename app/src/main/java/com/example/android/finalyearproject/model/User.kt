package com.example.android.finalyearproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var uId: String? = null,
    var name:String? = null,
    var surname:String? = null,
    var phone:String? = null,
    var address:String? = null,
    var desc:String? = null,
    var email:String? = null,
    var password:String? = null,
    var confirmPassword:String? = null,
    var role:String? = null,
    var rating:Float? = null,
    var countTimeRating: Int? = null,
    var type:List<String>? = null,
    var brand:List<String>? = null,
    var succeedTask: Int? = null,
    var failedTask: Int? = null
): Parcelable