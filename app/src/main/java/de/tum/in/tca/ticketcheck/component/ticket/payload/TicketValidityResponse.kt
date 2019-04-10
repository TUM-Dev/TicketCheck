package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketWithRedemption

data class TicketValidityResponse(
        @SerializedName("valid") var valid: Boolean = false,
        @SerializedName("tumid") var tumID: String? = null,
        @SerializedName("name") var lastName: String? = null,
        var tickets: List<TicketWithRedemption> = emptyList()
)
