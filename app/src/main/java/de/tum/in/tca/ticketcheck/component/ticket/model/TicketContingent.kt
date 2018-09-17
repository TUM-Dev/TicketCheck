package de.tum.`in`.tca.ticketcheck.component.ticket.model

import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketStatus

data class TicketContingent(val sold: Int, val contingent: Int) {

    companion object {

        fun fromTicketStatuses(ticketStatuses: List<TicketStatus>): TicketContingent {
            val sold = ticketStatuses.sumBy { it.sold }
            val contingent = ticketStatuses.sumBy { it.contingent }
            return TicketContingent(sold, contingent)
        }

    }

}