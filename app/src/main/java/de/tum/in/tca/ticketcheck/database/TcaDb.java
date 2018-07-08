package de.tum.in.tca.ticketcheck.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import de.tum.in.tca.ticketcheck.component.ticket.EventDao;
import de.tum.in.tca.ticketcheck.component.ticket.TicketDao;
import de.tum.in.tca.ticketcheck.component.ticket.TicketTypeDao;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.model.Ticket;
import de.tum.in.tca.ticketcheck.component.ticket.model.TicketType;
import de.tum.in.tca.ticketcheck.utils.Const;

@Database(version = 1, entities = {
        Event.class,
        Ticket.class,
        TicketType.class,
})
@TypeConverters(Converters.class)
public abstract class TcaDb extends RoomDatabase {
    private static final Migration[] migrations = {
    };

    private static TcaDb instance;

    public static synchronized TcaDb getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TcaDb.class, Const.DATABASE_NAME)
                           .allowMainThreadQueries()
                           .addMigrations(migrations)
                           .fallbackToDestructiveMigration()
                           .build();
        }
        return instance;
    }

    public abstract EventDao eventDao();

    public abstract TicketDao ticketDao();

    public abstract TicketTypeDao ticketTypeDao();
}
