package de.tum.`in`.tca.ticketcheck.component.ticket

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM event ORDER BY date")
    fun getAll(): List<Event>

    @Query("SELECT * FROM event where id = :id")
    fun getEventById(id: Int): Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: List<Event>)

    @Query("DELETE FROM event WHERE date < date('now')")
    fun removePastEvents()

}
