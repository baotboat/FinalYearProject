package com.example.android.finalyearproject.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Task (
    var taskId: String? = null,
    var creatorUId: String? = null,
    var creatorName: String? = null,
    var type: String? = null,
    var status: String? = null,
    var brand: String? = null,
    var spec: String? = null,
    var desc: String? = null,
    var chipSymptom: String? = null,
    var symptom: String? = null,
    var date: List<Date>? = null,
    var address: String? = null,
    var confirmPrice: String? = null,
    var alreadyPaid: Boolean? = null,
    var finishPayout: Boolean? = null,
    var listOffer: List<Offer>? = null,
    var listWhoSentOffer: List<Decision>? = null
): Parcelable {
    @Parcelize
    data class Offer (
        var mechUId: String? = null,
        var mechName: String? = null,
        var mechRating: Float? = null,
        var confirmDate: String? = null,
        var minPrice: Int? = null,
        var maxPrice: Int? = null,
        var customerSelected: Boolean? = null,
        var mechDidSimilarTask: Boolean? = null
    ): Parcelable
    @Parcelize
    data class Decision (
        var mechUId: String? = null,
        var decistion : String? = null
    ): Parcelable
}

data class Response (
    var taskList: List<Task>? = null,
    var exception: Exception? = null,
)
