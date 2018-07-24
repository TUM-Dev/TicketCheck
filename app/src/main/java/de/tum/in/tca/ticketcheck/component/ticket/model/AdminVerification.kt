package de.tum.`in`.tca.ticketcheck.component.ticket.model

import android.content.Context
import de.tum.`in`.tca.ticketcheck.api.AuthenticationManager
import de.tum.`in`.tca.ticketcheck.api.exception.NoPrivateKey
import java.io.IOException
import java.math.BigInteger
import java.security.SecureRandom
import java.util.*

data class AdminVerification(var signature: String = "",
                             var date: String = "",
                             var rand: String = "",
                             var data: Any? = null) {
    companion object {
        @Throws(NoPrivateKey::class)
        fun getAdminVerification(c: Context): AdminVerification {
            //Create some data
            val date = Date().toString()
            val rand = BigInteger(130, SecureRandom()).toString(32)

            //Sign this data for verification
            val am = AuthenticationManager(c)
            val signature = am.sign(date + rand)

            return AdminVerification(
                    signature = signature,
                    date = date,
                    rand = rand,
                    data = null
            )
        }

        @Throws(NoPrivateKey::class)
        fun getAdminVerification(c: Context, data: Any?): AdminVerification {
            val ret = this.getAdminVerification(c)
            ret.data = data
            return ret
        }

        @Throws(IOException::class)
        fun createAdminVerification(context: Context, data: Any?): AdminVerification {
            var adminVerification: AdminVerification? = null
            try {
                adminVerification = AdminVerification.getAdminVerification(context, data)
            } catch (noPrivateKey: NoPrivateKey) {
                noPrivateKey.printStackTrace()
                throw IOException()
            }

            return adminVerification
        }

    }
}
