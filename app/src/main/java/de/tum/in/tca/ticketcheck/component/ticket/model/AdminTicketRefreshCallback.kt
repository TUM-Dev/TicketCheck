package de.tum.`in`.tca.ticketcheck.component.ticket.model

interface AdminTicketRefreshCallback {
    fun onResult(results: List<AdminTicket>)
    fun onFailure()
}
