package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName


data class TicketReservationResponse(
        @SerializedName("ticket_history")
        var ticketHistory: Int = 0)