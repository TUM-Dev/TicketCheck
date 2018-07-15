package de.tum.in.tca.ticketcheck.component.ticket;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;

@Dao
public interface AdminTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AdminTicket> adminTickets);

    @Query("SELECT * FROM adminticket WHERE event = :eventId")
    List<AdminTicket> getByEventId(int eventId);

    @Query("SELECT * FROM adminticket WHERE id = :ticketId")
    AdminTicket getByTicketId(int ticketId);

    @Query("DELETE FROM adminticket")
    void removeAll();
}
