package de.tum.`in`.tca.ticketcheck.api

import de.tum.`in`.tca.ticketcheck.api.app.exception.ChaosMonkeyException
import de.tum.`in`.tca.ticketcheck.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.ThreadLocalRandom

/**
 * This is a OkHttp Interceptor designed to remind developers, that network requests can and *will*
 * spontaneously fail.
 * If this Interceptor is in the stacktrace you're looking at, you probably didn't gracefully
 * onResult failing network conditions
 */
class ChaosMonkeyInterceptor : Interceptor {

    private val failProbability = 0.03

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (ThreadLocalRandom.current().nextDouble() < failProbability) {
            Utils.log("Chaos Monkey is resilience testing your network code")
            throw ChaosMonkeyException()
        }
        return chain.proceed(chain.request())
    }
}
