package com.example.android.finalyearproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object DownloadImageUtils {

    val storage = Firebase.storage
    var bmp: Bitmap? = null

    fun getImageFromStorage(imageName: String, downloadImageListener: DownloadImageListener) {
        val storageReference = storage.reference.child("images/${imageName}")
        val IMAGE_SIZE: Long = 4096 * 4096
        storageReference.getBytes(IMAGE_SIZE)
            .addOnSuccessListener {
                bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                downloadImageListener.loadImageComplete(bmp)
            }
            .addOnFailureListener {
                downloadImageListener.loadImageComplete(null)
            }
    }
}