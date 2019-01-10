package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName

data class TicketValidityRequest(@SerializedName("event_id")
                                 val eventId: Int,
                                 val codes: Array<String>)
