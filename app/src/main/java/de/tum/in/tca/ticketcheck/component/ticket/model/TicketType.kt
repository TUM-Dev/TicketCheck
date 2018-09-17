package de.tum.`in`.tca.ticketcheck.component.ticket.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ticket_types")
data class TicketType(
        @PrimaryKey
        @SerializedName("ticket_type")
        var id: Int = 0,
        var price: Int = 0,
        var description: String = ""
)
