package de.tum.`in`.tca.ticketcheck.component.ticket

import android.content.Context
import de.tum.`in`.tca.ticketcheck.api.TUMCabeClient
import de.tum.`in`.tca.ticketcheck.component.ticket.callbacks.ApiResponseCallback
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketStatus
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketValidityResponse
import de.tum.`in`.tca.ticketcheck.database.TcaDb
import de.tum.`in`.tca.ticketcheck.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class TicketsController(private val context: Context) {

    private val adminTicketDao = TcaDb.getInstance(context).adminTicketDao()
    private val ticketTypeDao = TcaDb.getInstance(context).ticketTypeDao()

    fun getTicketsForEvent(event: Int) = adminTicketDao.getByEventId(event)

    /**
     * This method refreshes the tickets from server
     * @param eventID of the event for which the tickets are needed
     * @param cb this callback is used to process the refreshed ticket list
     */
    fun refreshTickets(eventID: Int, cb: ApiResponseCallback<List<AdminTicket>>) {
        val callback = object : Callback<List<AdminTicket>> {
            override fun onResponse(call: Call<List<AdminTicket>>,
                                    response: Response<List<AdminTicket>>) {
                val tickets = response.body()

                if (response.isSuccessful.not() || tickets == null) {
                    cb.onFailure()
                    return
                }

                adminTicketDao.insert(tickets)
                cb.onResult(tickets)
            }

            override fun onFailure(call: Call<List<AdminTicket>>, t: Throwable) {
                Utils.log(t)
                cb.onFailure()
            }
        }

        try {
            TUMCabeClient.getInstance(context).getAdminTicketData(context, eventID, callback)
        } catch (e: IOException) {
            Utils.log(e)
            cb.onFailure()
        }
    }

    fun refreshTicketStats(eventID: Int, cb: ApiResponseCallback<List<TicketStatus>>) {
        val callback = object : Callback<List<TicketStatus>> {
            override fun onResponse(call: Call<List<TicketStatus>>,
                                    response: Response<List<TicketStatus>>) {
                val ticketStatuses = response.body()

                if (response.isSuccessful.not() || ticketStatuses == null) {
                    cb.onFailure()
                    return
                }

                cb.onResult(ticketStatuses)
            }

            override fun onFailure(call: Call<List<TicketStatus>>, t: Throwable) {
                Utils.log(t)
                cb.onFailure()
            }
        }

        TUMCabeClient.getInstance(context).getTicketStats(eventID, callback)
    }

    fun fetchTicketTypes(eventID: Int) {
        TUMCabeClient.getInstance(context)
                .fetchTicketTypes(eventID, object : Callback<List<TicketType>> {
                    override fun onResponse(call: Call<List<TicketType>>, response: Response<List<TicketType>>) {
                        val ticketTypes = response.body() ?: return
                        ticketTypeDao.insert(ticketTypes)
                    }

                    override fun onFailure(call: Call<List<TicketType>>, t: Throwable) {
                        Utils.log(t)
                    }
                })
    }

    fun getTicketTypeById(ticketTypeId: Int): TicketType = ticketTypeDao.getById(ticketTypeId)

    fun getTicketById(ticketId: Int): AdminTicket = adminTicketDao.getByTicketId(ticketId)

    fun insertTickets(tickets: List<AdminTicket>) {
        adminTicketDao.insert(tickets)
    }

    fun redeemTicket(ticketId: Int, cb: Callback<TicketSuccessResponse>) {
        try {
            TUMCabeClient.getInstance(context).redeemTicket(context, ticketId, cb)
        } catch (e: IOException) {
            Utils.log(e)
        }
    }

    fun checkTicketValidity(eventId: Int, code: String, cb: Callback<TicketValidityResponse>) {
        try {
            TUMCabeClient.getInstance(context).getTicketValidity(context, eventId, code, cb)
        } catch (e: IOException) {
            Utils.log(e)
        }
    }

}


