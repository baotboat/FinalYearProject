package com.example.android.finalyearproject

import androidx.lifecycle.Observer

class SingleEventObserver<T>(private val eventHandle: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(event: SingleEvent<T>?) {
        event?.didHandle()?.run(eventHandle)
    }
}