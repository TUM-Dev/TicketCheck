package de.tum.in.tca.ticketcheck.api;

import java.util.HashMap;
import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.model.Ticket;
import de.tum.in.tca.ticketcheck.component.ticket.model.TicketType;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketReservationResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.component.ui.chat.model.ChatVerification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static de.tum.in.tca.ticketcheck.api.TUMCabeClient.API_EVENTS;
import static de.tum.in.tca.ticketcheck.api.TUMCabeClient.API_TICKET;

public interface TUMCabeAPIService {


    // TICKET SALE

    // Getting Event information
    @GET(API_EVENTS + "list")
    Call<List<Event>> getEvents();

    @GET(API_EVENTS + "list/{eventID}")
    Call<Event> getEvent(@Path("eventID") int eventID);

    @GET(API_EVENTS + "list/search/{searchTerm}")
    Call<List<Event>> searchEvents(@Path("searchTerm") String searchTerm);

    @GET(API_EVENTS + API_TICKET + "type/{eventID}")
    Call<List<TicketType>> getTicketTypes(@Path("eventID") int eventID);

    @POST(API_EVENTS + "ticket/validate")
    Call<TicketValidityResponse> getNameForTicket(@Body TicketValidityRequest request);

}