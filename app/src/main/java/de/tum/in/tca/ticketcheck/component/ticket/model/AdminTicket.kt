package de.tum.`in`.tca.ticketcheck.component.ticket.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

/**
 * AdminTicket
 *
 * @param name  Name
 * @param lrzId  lrzId
 * @param ticketType   ticketType
 * @param purchaseDate   purchaseDate
 * @param redeemDate   redeemDate
 * @param checked   checked
 */
@Entity
data class AdminTicket(
        @PrimaryKey
        @SerializedName("name")
        var name: String = "",
        @SerializedName("lrz_id")
        var lrzId: String = "",
        @SerializedName("ticket_id")
        var ticketId: String = "",
        @SerializedName("ticket_type")
        var ticketType: Int = 0,
        @SerializedName("purchase")
        var purchaseDate: DateTime = DateTime(),
        @SerializedName("redemption")
        var redeemDate: DateTime = DateTime(),
        @SerializedName("checked")
        var checked: Boolean = false

)