package de.tum.in.tca.ticketcheck.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminVerification;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.model.TicketType;
import de.tum.in.tca.ticketcheck.component.ticket.payload.AdminKeyUploadRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.AdminTicketRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketRedemptionRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketStatus;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.utils.Const;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Proxy class for Retrofit client to our API hosted @app.tum.de
 */
public final class TUMCabeClient {

    static final String API_EVENTS = "event/";
    static final String API_TICKET = "ticket/";

    private static final String API_HOSTNAME = Const.API_HOSTNAME;
    private static final String API_BASEURL = "/Api/";

    private static TUMCabeClient instance;
    private final TUMCabeAPIService service;

    private TUMCabeClient(final Context c) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://" + API_HOSTNAME + API_BASEURL);

        Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateSerializer())
                                     .create();
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.client(Helper.getOkHttpClient(c));
        service = builder.build()
                .create(TUMCabeAPIService.class);
    }

    public static synchronized TUMCabeClient getInstance(Context c) {
        if (instance == null) {
            instance = new TUMCabeClient(c.getApplicationContext());
        }
        return instance;
    }

    // Get event information

    public void getEvents(Callback<List<Event>> callback) {
        service.getEvents().enqueue(callback);
    }

    public void getTicketValidity(Context context, int eventId, String[] codes, Callback<TicketValidityResponse> callback) throws IOException {
        TicketValidityRequest request = new TicketValidityRequest(eventId, codes);
        service.getNameForTicket(AdminVerification.Companion.createAdminVerification(context, request))
                .enqueue(callback);
    }

    public void redeemTicket(Context context, int ticketId, Callback<TicketSuccessResponse> callback) throws IOException {
        service.redeemTicket(AdminVerification.Companion.createAdminVerification(context, new TicketRedemptionRequest(ticketId)))
                .enqueue(callback);
    }

    public void getAdminTicketData(Context context, int eventId, Callback<List<AdminTicket>> callback) throws IOException {
        service.getAdminTicketData(AdminVerification.Companion.createAdminVerification(context, new AdminTicketRequest(eventId)))
                .enqueue(callback);
    }

    public void fetchTicketTypes(int eventID, Callback<List<TicketType>> cb) {
        service.getTicketTypes(eventID).enqueue(cb);
    }

    public void getTicketStats(int event, Callback<List<TicketStatus>> cb) {
        service.getTicketStats(event).enqueue(cb);
    }

    public void uploadAdminKey(String key, Callback<TicketSuccessResponse> cb) {
        service.uploadAdminKey(new AdminKeyUploadRequest(key)).enqueue(cb);
    }
}
