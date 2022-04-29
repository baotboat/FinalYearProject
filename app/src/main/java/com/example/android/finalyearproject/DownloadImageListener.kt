package com.example.android.finalyearproject

import android.graphics.Bitmap

interface DownloadImageListener {

    fun loadImageComplete(bmp: Bitmap?)
}