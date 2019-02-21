package de.tum.`in`.tca.ticketcheck.component.ticket.model

import com.google.gson.annotations.SerializedName

data class TicketWithRedemption (
        @SerializedName("ticket_id") var ticketId: Int = 0,
        @SerializedName("redeemdate") var redeemDate: String? = null
)