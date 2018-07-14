package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.api.TUMCabeClient;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ui.chat.model.ChatMember;
import de.tum.in.tca.ticketcheck.database.TcaDb;
import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Mock class, only used to provide static event data for testing purposes
 * TODO: replace this when the actual data is available
 */
public class TicketsController {

    private final Context context;

    private final AdminTicketDao adminTicketDao;

    /**
     * Constructor, open/create database, create table if necessary
     *
     * @param context Context
     */
    public TicketsController(Context context) {
        this.context = context;

        Utils.setSetting(context, Const.CHAT_MEMBER, new ChatMember("ga38fir"));
        adminTicketDao = TcaDb.getInstance(context).adminTicketDao();
    }

    public List<AdminTicket> getTickets() {
        return adminTicketDao.getAll();
    }

    public List<AdminTicket> getTicketsForEvent(int event) {
        return adminTicketDao.getByEventId(event);
    }

    public List<AdminTicket> refreshTickets(int eventID) {
        downloadFromService(eventID);
        return getTicketsForEvent(eventID);
    }

    private void downloadFromService(int eventID) {
        // get event information from API
        Callback<List<AdminTicket>> cb = new Callback<List<AdminTicket>>() {
            @Override
            public void onResponse(Call<List<AdminTicket>> call, Response<List<AdminTicket>> response) {
                List<AdminTicket> tickets = response.body();
                if (tickets == null) {
                    tickets = new ArrayList<>();
                }
                adminTicketDao.insert(tickets);
            }

            @Override
            public void onFailure(Call<List<AdminTicket>> call, Throwable t) {
                Utils.log(t);
            }
        };
        try {
            TUMCabeClient.getInstance(context).getAdminTicketData(context, eventID, cb);
        } catch (IOException e) {
            Utils.log(e);
        }
    }
}


