package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName

data class TicketValidityResponse(
        @SerializedName("ticket_history") var ticketHistory: Int = 0,
        @SerializedName("tumid") var tumID: String? = null,
        @SerializedName("firstname") var firstName: String? = null,
        @SerializedName("name") var lastName: String? = null,
        @SerializedName("valid") var valid: Boolean = false,
        @SerializedName("purchasedate") var purchaseDate: String? = null,
        @SerializedName("redeemdate") var redeemDate: String? = null,
        @SerializedName("status") var status: String? = null
)
