package de.tum.`in`.tca.ticketcheck.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> tryOrNull(f: () -> T): T? {
    return try {
        f()
    } catch (_: Exception) {
        null
    }
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, Observer<T> { value ->
        value?.let { observer(it) }
    })
}
