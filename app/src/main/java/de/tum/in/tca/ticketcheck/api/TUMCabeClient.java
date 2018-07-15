package de.tum.in.tca.ticketcheck.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.AdminVerification;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.payload.AdminTicketRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketRedemptionRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketStatus;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.utils.Const;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
                .baseUrl("https://" + API_HOSTNAME + API_BASEURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

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

    public void getTicketValidity(Context context, String eventId, String code, Callback<TicketValidityResponse> callback) throws IOException {
        TicketValidityRequest request = new TicketValidityRequest(eventId, code);
        service.getNameForTicket(AdminVerification.Companion.createAdminVerification(context, request))
                .enqueue(callback);
    }

    public void redeemTicket(int ticketHistory, Callback<TicketSuccessResponse> callback) {
        service.redeemTicket(new TicketRedemptionRequest(ticketHistory))
                .enqueue(callback);
    }

    public void getAdminTicketData(int eventId, Callback<List<AdminTicket>> callback) {
        service.getAdminTicketData(new AdminTicketRequest(eventId))
                .enqueue(callback);
    }

    public void getTicketStats(int event, Callback<List<TicketStatus>> cb) {
        service.getTicketStats(event).enqueue(cb);
    }
}
