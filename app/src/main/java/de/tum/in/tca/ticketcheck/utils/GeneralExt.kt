package de.tum.`in`.tca.ticketcheck.utils

inline fun <T> tryOrNull(f: () -> T): T? {
    return try {
        f()
    } catch (_: Exception) {
        null
    }
}