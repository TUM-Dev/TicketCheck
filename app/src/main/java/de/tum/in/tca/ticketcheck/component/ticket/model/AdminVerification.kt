package de.tum.`in`.tca.ticketcheck.component.ticket.model

import android.content.Context
import de.tum.`in`.tca.ticketcheck.api.AuthenticationManager
import de.tum.`in`.tca.ticketcheck.api.exception.NoPrivateKey
import de.tum.`in`.tca.ticketcheck.utils.Const
import de.tum.`in`.tca.ticketcheck.utils.Utils
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
            setKeys(c)
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

        fun setKeys(context: Context) {
            Utils.setSetting(context, Const.PUBLIC_KEY, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnQmMnWxKL3kMd6a5MWC7sY8DBG7YkLVdfgaxr\n" +
                    "XIE9Y25bbsSmq/1zfKLlph3j3wXm94mmxeH9rG92BcBe1MCeT4rAdYiQLjvJltk6yekZz9O4XDnR\n" +
                    "8rvVu+9HyJVHm/ysipOeKHJreFzOxlQ2m1FWZ1djtiCEzHKEdWTQjzpc4QIDAQAB\n")
            Utils.setSetting(context, Const.PRIVATE_KEY, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOdCYydbEoveQx3prkxYLuxjwMEb\n" +
                    "tiQtV1+BrGtcgT1jbltuxKar/XN8ouWmHePfBeb3iabF4f2sb3YFwF7UwJ5PisB1iJAuO8mW2TrJ\n" +
                    "6RnP07hcOdHyu9W770fIlUeb/KyKk54ocmt4XM7GVDabUVZnV2O2IITMcoR1ZNCPOlzhAgMBAAEC\n" +
                    "gYBXbiDgeyz61i/XukYcNOaglmIEX/6vlLjIsdTGzjVKSTIqvSj5vmJW7BMF8ZSxVtr7ZDCVbZCw\n" +
                    "ACYvn6MH9zVOrJ0oa7pKaiZhDq9o3BZ0RZ6sn8yOTm1PswJWDiqHsIWIop/Vrhh0KE50VXv6KNsi\n" +
                    "3nzkBBOB8X7OjLObTWyaJQJBAP+yBmsAZcVK0ghLcQoREouka5tQLAdW+5P/bhqwVVGrG5z25zqm\n" +
                    "XSxt6rGrm0lnXEvZoRIm2vSFHoBOcGEg0xcCQQDniOkQYgSRqZcdo5/oyXd2aPNlaMDYZaV9WkAQ\n" +
                    "2Yfo6j4tHn5kW3ax/OpXtg1zZb+xdw9TgMGbf0HHesibX6rHAkA2yRQq9QjZPvDFqjRsLcBFf1M3\n" +
                    "EfR9FhwNV9tliIafWwQtm5FrtZ2dGWgB7Xz2O1lBPz4Nv5mGbsdcQnI9vWS9AkEAmfL2xOXnk9Ln\n" +
                    "n9WEJgyPWjQ+YldcooYzmz2/C3UYYU4fOXDlKWWHbqF0UA3Es84bv6wijKs4EGR1IGYLrQPXUQJB\n" +
                    "AKkGvXfzI1Xkkw4vqdh5LY5vMMR/+vBHZbD1dqLK6c9bu+yhuxcVPnpZ/xVW/4aTEFrUvVSeGEVH\n" +
                    "qWPHprLBVdE=\n")
        }

    }
}
