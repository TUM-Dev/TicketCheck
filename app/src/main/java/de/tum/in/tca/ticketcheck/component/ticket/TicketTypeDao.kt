package de.tum.`in`.tca.ticketcheck.component.ticket

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType

@Dao
interface TicketTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ticketTypes: List<TicketType>)

    @Query("SELECT * FROM ticket_types WHERE id = :id")
    fun getById(id: Int): TicketType

}
