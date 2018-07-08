package de.tum.`in`.tca.ticketcheck.api.app.exception

import java.io.IOException

class NoNetworkConnectionException : IOException() {

    override val message: String?
        get() = "Device is not connected to the Internet"

}