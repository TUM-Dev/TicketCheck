package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName


data class TicketStatus(@SerializedName("ticket_type")
                        var ticketType: Int = 0,
                        var contingent: Int = 0,
                        var sold: Int = 0)