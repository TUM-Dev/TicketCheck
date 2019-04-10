package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName

data class TicketRedemptionRequest(@SerializedName("ticket_ids")
                                   var ticketIds: List<Int> = emptyList())
