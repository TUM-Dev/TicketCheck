package de.tum.`in`.tca.ticketcheck.component.ticket.callbacks

interface ApiResponseCallback<T> {

    fun onResult(result: T)
    fun onFailure()

}