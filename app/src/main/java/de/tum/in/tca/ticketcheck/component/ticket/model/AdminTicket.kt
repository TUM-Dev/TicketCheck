package de.tum.`in`.tca.ticketcheck.component.ticket.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
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
        var purchaseDate: DateTime? = null,
        @SerializedName("redemption")
        var redeemDate: DateTime? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            if (parcel.readInt() == 1) DateTime(parcel.readLong()) else null,
            if (parcel.readInt() == 1) DateTime(parcel.readLong()) else null
    )

    val isPurchased: Boolean
        get() = purchaseDate != null

    val isRedeemed: Boolean
        get() = redeemDate != null

    fun filter(query: String?): Boolean {
        query?.let {
            return name.contains(it, true) || lrzId.contains(it, true)
        }

        return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(event)
        parcel.writeString(name)
        parcel.writeString(lrzId)
        parcel.writeInt(ticketType)

        parcel.writeInt(if (isPurchased) 1 else 0)
        purchaseDate?.let {
            parcel.writeLong(it.millis)
        }

        parcel.writeInt(if (isRedeemed) 1 else 0)
        redeemDate?.let {
            parcel.writeLong(it.millis)
        }
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<AdminTicket> {
        override fun createFromParcel(parcel: Parcel): AdminTicket {
            return AdminTicket(parcel)
        }

        override fun newArray(size: Int): Array<AdminTicket?> {
            return arrayOfNulls(size)
        }
    }

}