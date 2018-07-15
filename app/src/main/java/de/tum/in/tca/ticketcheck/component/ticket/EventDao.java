package de.tum.in.tca.ticketcheck.component.ticket;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.Event;

@Dao
public interface EventDao {

    /**
     * Removes all old items
     */
    @Query("DELETE FROM event WHERE date < date('now')")
    void removePastEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Event> event);

    @Query("SELECT * FROM event ORDER BY date")
    List<Event> getAll();

    @Query("SELECT * FROM event where id = :id")
    Event getEventById(int id);

    @Query("DELETE FROM event")
    void removeAll();

}
