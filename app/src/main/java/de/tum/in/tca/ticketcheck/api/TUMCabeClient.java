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
    private static final String API_CHAT = "chat/";
    static final String API_CHAT_ROOMS = API_CHAT + "rooms/";
    static final String API_CHAT_MEMBERS = API_CHAT + "members/";

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

    // Getting ticket information
    public void getTickets(Context context, Callback<List<Ticket>> cb) throws IOException {
        ChatVerification chatVerification = ChatVerification.Companion.createChatVerification(context, null);
        service.getTickets(chatVerification).enqueue(cb);
    }

    public List<TicketType> getTicketTypes(int eventID) throws IOException {
        return service.getTicketTypes(eventID).execute().body();
    }

    // Ticket reservation
    public void reserveTicket(Context context, int ticketType, Callback<TicketReservationResponse> cb) throws IOException {
        ChatVerification chatVerification = ChatVerification.Companion.createChatVerification(context, new TicketReservation(ticketType));
        service.reserveTicket(chatVerification).enqueue(cb);
    }

    public void cancelTicketReservation(Context context, int ticketHistory, Callback<TicketSuccessResponse> cb) throws IOException {
        ChatVerification chatVerification = ChatVerification.Companion.createChatVerification(context, new TicketReservationCancelation(ticketHistory));
        service.cancelTicketReservation(chatVerification).enqueue(cb);
    }

    // Ticket purchase
    public void purchaseTicketStripe(Context context, int ticketHistory, String token, String customerMail,
                                       String customerName, Callback<Ticket> cb) throws IOException {
        HashMap<String, Object> argsMap = new HashMap<>();
        argsMap.put("ticket_history", ticketHistory);
        argsMap.put("token", token);
        argsMap.put("customer_mail", customerMail);
        argsMap.put("customer_name", customerName);

        ChatVerification chatVerification = ChatVerification.Companion.createChatVerification(context, argsMap);
        service.purchaseTicketStripe(chatVerification).enqueue(cb);
    }

    public void retrieveEphemeralKey(Context context, String apiVersion, String customerMail, Callback<HashMap<String, Object>> cb) throws IOException {
        ChatVerification chatVerification = ChatVerification.Companion.createChatVerification(context, new EphimeralKey(customerMail, apiVersion));
        service.retrieveEphemeralKey(chatVerification).enqueue(cb);
    }

    public void getTicketValidity(String eventId, String code, Callback<TicketValidityResponse> callback) {
        TicketValidityRequest request = new TicketValidityRequest(eventId, code);
        service.getNameForTicket(request)
                .enqueue(callback);
    }

}
