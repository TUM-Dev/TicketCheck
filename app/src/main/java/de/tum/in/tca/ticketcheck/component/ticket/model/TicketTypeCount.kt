package de.tum.`in`.tca.ticketcheck.component.ticket.model

import com.google.gson.annotations.SerializedName

data class TicketTypeCount(@SerializedName("ticket_type")
                           var id: Int = 0,
                           var price: Int = 0,
                           var description: String = "",
                           var count: Int)