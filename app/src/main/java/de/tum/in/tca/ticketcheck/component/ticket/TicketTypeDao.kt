package de.tum.`in`.tca.ticketcheck.component.ticket

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketTypeCount

@Dao
interface TicketTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ticketTypes: List<TicketType>)

    @Query("SELECT * FROM ticket_types WHERE id = :id")
    fun getById(id: Int): TicketType

    @Query("SELECT count(*) as count, tt.* " +
            "FROM ticket_types tt, adminticket at " +
            "WHERE at.ticketType = tt.id AND at.id IN (:ids) " +
            "GROUP BY tt.id, tt.description")
    fun getByIds(ids: List<Int>): List<TicketTypeCount>

}
