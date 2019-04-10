package de.tum.`in`.tca.ticketcheck.component.ticket

import androidx.lifecycle.LiveData
import androidx.room.*

import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Customer

@Dao
interface AdminTicketDao {

    @Query("SELECT * FROM adminticket")
    fun getAll(): LiveData<List<AdminTicket>>

    @Query("SELECT * FROM adminticket WHERE event = :eventId AND lrzId = :lrzId")
    fun getByEventAndCustomer(eventId: Int, lrzId: String): List<AdminTicket>

    @Query("SELECT ticket.name, ticket.lrzId, count(*) as nrOfTickets, redeemed.nrOfTicketsRedeemed  FROM adminticket ticket LEFT JOIN (SELECT lrzId, count(*) as nrOfTicketsRedeemed FROM adminticket WHERE event = :eventId AND redeemDate not null GROUP BY lrzId, name) redeemed ON ticket.lrzId = redeemed.lrzId WHERE event = :eventId  GROUP BY ticket.lrzId, ticket.name")
    fun getCustomersByEventId(eventId: Int): LiveData<List<Customer>>

    @Query("SELECT * FROM adminticket WHERE id IN (:ticketIds)")
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
