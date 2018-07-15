package de.tum.in.tca.ticketcheck.api;

import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.payload.AdminTicketRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketRedemptionRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketStatus;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static de.tum.in.tca.ticketcheck.api.TUMCabeClient.API_EVENTS;
import static de.tum.in.tca.ticketcheck.api.TUMCabeClient.API_TICKET;

public interface TUMCabeAPIService {

    @GET(API_EVENTS + "list")
    Call<List<Event>> getEvents();

    @POST(API_EVENTS + "ticket/validate")
    Call<TicketValidityResponse> getNameForTicket(@Body TicketValidityRequest request);

    @POST(API_EVENTS + "ticket/redeem")
    Call<TicketSuccessResponse> redeemTicket(@Body TicketRedemptionRequest request);

    @POST(API_EVENTS + "ticket/sold")
    Call<List<AdminTicket>> getAdminTicketData(@Body AdminTicketRequest adminTicketRequest);

    @GET(API_EVENTS + API_TICKET + "status/{event}")
    Call<List<TicketStatus>> getTicketStats(@Path("event") int event);

}