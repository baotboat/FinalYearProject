package com.example.android.finalyearproject.repository

import com.example.android.finalyearproject.model.Task

interface TaskFirebaseCallback {
    fun onResponse(taskData: Task) {

    }
}