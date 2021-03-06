package de.tum.in.tca.ticketcheck.api;

import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminVerification;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ticket.model.TicketType;
import de.tum.in.tca.ticketcheck.component.ticket.payload.AdminKeyUploadRequest;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketStatus;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static de.tum.in.tca.ticketcheck.api.TUMCabeClient.API_EVENTS;
import static de.tum.in.tca.ticketcheck.api.TUMCabeClient.API_TICKET;

public interface TUMCabeAPIService {

    @GET(API_EVENTS + "list/admin/")
    Call<List<Event>> getEvents();

    @POST(API_EVENTS + "ticket/validate/multiple")
    Call<TicketValidityResponse> getTicketValidity(@Body AdminVerification adminVerification);

    @POST(API_EVENTS + "ticket/redeem/multiple")
    Call<TicketSuccessResponse> redeemTickets(@Body AdminVerification adminVerification);

    @POST(API_EVENTS + "ticket/sold")
    Call<List<AdminTicket>> getAdminTicketData(@Body AdminVerification adminVerification);

    @GET(API_EVENTS + API_TICKET + "status/{event}")
    Call<List<TicketStatus>> getTicketStats(@Path("event") int event);

    @GET(API_EVENTS + API_TICKET + "type/{eventID}")
    Call<List<TicketType>> getTicketTypes(@Path("eventID") int eventID);

    @POST(API_EVENTS + "ticket/admin/key/upload")
    Call<TicketSuccessResponse> uploadAdminKey(@Body AdminKeyUploadRequest adminKeyUploadRequest);
}
