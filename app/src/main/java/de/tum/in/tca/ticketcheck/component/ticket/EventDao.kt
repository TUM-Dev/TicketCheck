package de.tum.`in`.tca.ticketcheck.component.ticket

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

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
