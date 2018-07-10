package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.api.TUMCabeClient;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.model.Ticket;
import de.tum.in.tca.ticketcheck.component.ticket.model.TicketType;
import de.tum.in.tca.ticketcheck.component.ui.chat.model.ChatMember;
import de.tum.in.tca.ticketcheck.database.TcaDb;
import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsController {

    private final Context context;

    private final EventDao eventDao;

    /**
     * Constructor, open/create database, create table if necessary
     *
     * @param context Context
     */
    public EventsController(Context context) {
        this.context = context;
        eventDao = TcaDb.getInstance(context).eventDao();
        downloadFromService();
    }


    public void downloadFromService() {
        // get ticket type information from API
        Thread thread = new Thread(){
            public void run(){
                TUMCabeClient api = TUMCabeClient.getInstance(context);

                // Delete all too old items
                eventDao.cleanUp();

                // Load all events since the last sync
                try {
                    List<Event> events = api.getEvents();
                    eventDao.insert(events);
                } catch (IOException e) {
                    Utils.log(e);
                }
            }
        };
        thread.start();
    }

    // Event methods

    public List<Event> getEvents() {
        return eventDao.getAll();
    }

    public Event getEventById(int id) {
        return eventDao.getEventById(id);
    }
}

