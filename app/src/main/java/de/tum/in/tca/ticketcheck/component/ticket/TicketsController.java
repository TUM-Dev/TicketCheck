package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.api.TUMCabeClient;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicketRefreshCallback;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.database.TcaDb;
import de.tum.in.tca.ticketcheck.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        adminTicketDao = TcaDb.getInstance(context).adminTicketDao();
    }

    public List<AdminTicket> getTicketsForEvent(int event) {
        return adminTicketDao.getByEventId(event);
    }

    /**
     * This method refreshes the tickets from server
     * @param eventID of the event for which the tickets are needed
     * @param cb this callback is used to process the refreshed ticket list
     */
    public void refreshTickets(int eventID, AdminTicketRefreshCallback cb) {
        downloadFromService(eventID, cb);
    }

    private void downloadFromService(int eventID, AdminTicketRefreshCallback cb) {
        Callback<List<AdminTicket>> callback = new Callback<List<AdminTicket>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdminTicket>> call, Response<List<AdminTicket>> response) {
                List<AdminTicket> tickets = response.body();
                if (tickets == null) {
                    tickets = new ArrayList<>();
                }
                adminTicketDao.insert(tickets);
                cb.handle(tickets);
            }

            @Override
            public void onFailure(@NonNull Call<List<AdminTicket>> call, @NonNull Throwable t) {
                Utils.log(t);
            }
        };
        try {
            TUMCabeClient.getInstance(context).getAdminTicketData(context, eventID, callback);
        } catch (IOException e) {
            Utils.log(e);
        }
    }

    public AdminTicket getTicketById(int ticketId) {
        return adminTicketDao.getByTicketId(ticketId);
    }

    public void redeemTicket(int ticketId, Callback<TicketSuccessResponse> cb){
        try {
            TUMCabeClient.getInstance(context).redeemTicket(context, ticketId, cb);
        } catch (IOException e) {
            Utils.log(e);
        }
    }

    public void checkTicketValidity(int eventId, String code, Callback<TicketValidityResponse> cb) {
        try {
            TUMCabeClient.getInstance(context).getTicketValidity(context, eventId, code, cb);
        } catch (IOException e) {
            Utils.log(e);
        }
    }
}


