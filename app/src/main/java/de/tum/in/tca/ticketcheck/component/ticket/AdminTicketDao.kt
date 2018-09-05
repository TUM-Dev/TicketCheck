package de.tum.`in`.tca.ticketcheck.component.ticket

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket

@Dao
interface AdminTicketDao {

    @Query("SELECT * FROM adminticket WHERE event = :eventId")
    fun getByEventId(eventId: Int): List<AdminTicket>

    @Query("SELECT * FROM adminticket WHERE id = :ticketId")
    fun getByTicketId(ticketId: Int): AdminTicket

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(adminTickets: List<AdminTicket>)

    @Query("UPDATE adminticket SET redeemDate = date() WHERE id = :ticketID")
    fun setTicketRedeemed(ticketID: Int)

    @Query("DELETE FROM adminticket WHERE event IN (SELECT id FROM event WHERE date < date('now'))")
    fun removeTicketsOfPastEvents()

    @Query("DELETE FROM adminticket")
    fun removeAll()

}
