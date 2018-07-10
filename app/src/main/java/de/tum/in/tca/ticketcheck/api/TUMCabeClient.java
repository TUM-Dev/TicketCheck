package de.tum.in.tca.ticketcheck.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.model.Ticket;
import de.tum.in.tca.ticketcheck.component.ticket.model.TicketType;
import de.tum.in.tca.ticketcheck.component.ticket.payload.EphimeralKey;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketReservation;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketReservationCancelation;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketReservationResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.component.ui.chat.model.ChatVerification;
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


    // TICKET SALE
    // Getting event information
    public List<Event> getEvents() throws IOException {
        List<Event> list = service.getEvents().execute().body();

        return list;
    }

    public Event getEvent(int eventID) throws IOException {
        Event event = service.getEvent(eventID).execute().body();
        return event;
    }

    public List<Event> searchEvents(String searchTerm) throws IOException {
        return service.searchEvents(searchTerm).execute().body();
    }

    public List<TicketType> getTicketTypes(int eventID) throws IOException {
        return service.getTicketTypes(eventID).execute().body();
    }

    public void getTicketValidity(String eventId, String code, Callback<TicketValidityResponse> callback) {
        TicketValidityRequest request = new TicketValidityRequest(eventId, code);
        service.getNameForTicket(request)
                .enqueue(callback);
    }

    // TODO: redeem ticket endpoint

}
