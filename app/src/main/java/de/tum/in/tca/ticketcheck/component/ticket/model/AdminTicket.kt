package de.tum.`in`.tca.ticketcheck.component.ticket.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

/**
 * AdminTicket
 *
 * @param id ticket_history
 * @param name  Name
 * @param event event ID
 * @param lrzId  lrzId
 * @param ticketType   ticketType
 * @param purchaseDate   purchaseDate
 * @param redeemDate   redeemDate
 */
@Entity
data class AdminTicket(
        @PrimaryKey
        @SerializedName("ticket_history")
        var id: Int = 0,
        var event: Int = 0,
        var name: String = "",
        @SerializedName("lrz_id")
        var lrzId: String = "",
        @SerializedName("ticket_type")
        var ticketType: Int = 0,
        @SerializedName("purchase")
        var purchaseDate: DateTime = DateTime(),
        @SerializedName("redemption")
        var redeemDate: DateTime = DateTime())