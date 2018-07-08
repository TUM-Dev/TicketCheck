package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName


data class TicketReservation(@SerializedName("ticket_type")
                             var id: Int = 0)
