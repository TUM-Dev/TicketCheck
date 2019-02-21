package de.tum.`in`.tca.ticketcheck.component.ticket

import androidx.lifecycle.LiveData
import androidx.room.*

import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket

@Dao
interface AdminTicketDao {

    @Query("SELECT * FROM adminticket")
    fun getAll(): LiveData<List<AdminTicket>>

    @Query("SELECT * FROM adminticket WHERE event = :eventId")
    fun getByEventId(eventId: Int): LiveData<List<AdminTicket>>

    @Query("SELECT * FROM adminticket WHERE id IN (:ticketIds)" +
            "ORDER BY ticketType")
    fun getByTicketIds(ticketIds: List<Int>): List<AdminTicket>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(adminTickets: List<AdminTicket>)

    @Query("UPDATE adminticket SET redeemDate = datetime() WHERE id IN (:ticketIDs)")
    fun setTicketsRedeemed(ticketIDs: List<Int>)

    @Query("UPDATE adminticket SET redeemDate = :redemptionDate WHERE id = :ticketID")
    fun updateRedemptionDate(ticketID: Int, redemptionDate: String)

    @Query("DELETE FROM adminticket WHERE event IN (SELECT id FROM event WHERE date < date('now'))")
    fun removeTicketsOfPastEvents()

    @Query("DELETE FROM adminticket")
    fun removeAll()

}
