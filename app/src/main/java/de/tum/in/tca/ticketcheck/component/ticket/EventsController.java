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


    private void downloadFromService() {
        // get event information from API
        Callback<List<Event>> cb = new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                // set view
                List<Event> events = response.body();
                eventDao.insert(events);
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Utils.log(t);
                // if the download fails the user should refresh manually
                // retrying automatically could lead to endless loop if the device is offline
            }
        };
        try {
            TUMCabeClient.getInstance(context).getEvents(cb);
        } catch (IOException e) {
            Utils.log(e);
        }
    }

    public List<Event> refreshEvents(){
        downloadFromService();
        return getEvents();
    }

    // Event methods

    public List<Event> getEvents() {
        return eventDao.getAll();
    }

    public Event getEventById(int id) {
        return eventDao.getEventById(id);
    }
}

