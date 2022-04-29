package com.example.android.finalyearproject

open class SingleEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun didHandle(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SingleEvent<*>
        if (content != other.content) return false
        if (hasBeenHandled != other.hasBeenHandled) return false
        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + hasBeenHandled.hashCode()
        return result
    }
}