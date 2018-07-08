package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class TicketReservationCancelation(@PrimaryKey
                                        @SerializedName("ticket_history")
                                        var ticketHistory: Int = 0)