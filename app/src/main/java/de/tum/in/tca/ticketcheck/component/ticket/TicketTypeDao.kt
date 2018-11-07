package de.tum.`in`.tca.ticketcheck.component.ticket

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType

@Dao
interface TicketTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ticketTypes: List<TicketType>)

    @Query("SELECT * FROM ticket_types WHERE id = :id")
    fun getById(id: Int): TicketType

}
