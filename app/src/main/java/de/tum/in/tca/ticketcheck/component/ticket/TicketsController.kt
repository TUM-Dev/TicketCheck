package de.tum.`in`.tca.ticketcheck.component.ticket

import android.content.Context
import de.tum.`in`.tca.ticketcheck.api.TUMCabeClient
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicketRefreshCallback
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType
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

    fun getTicketsForEvent(event: Int): List<AdminTicket> = adminTicketDao.getByEventId(event)

    /**
     * This method refreshes the tickets from server
     * @param eventID of the event for which the tickets are needed
     * @param cb this callback is used to process the refreshed ticket list
     */
    fun refreshTickets(eventID: Int, cb: AdminTicketRefreshCallback) {
        downloadFromService(eventID, cb)
    }

    private fun downloadFromService(eventID: Int, cb: AdminTicketRefreshCallback) {
        val callback = object : Callback<List<AdminTicket>> {
            override fun onResponse(call: Call<List<AdminTicket>>, response: Response<List<AdminTicket>>) {
                val tickets = response.body() ?: getTicketsForEvent(eventID)
                adminTicketDao.insert(tickets)
                cb.onResult(tickets)
            }

            override fun onFailure(call: Call<List<AdminTicket>>, t: Throwable) {
                Utils.log(t)
            }
        }

        try {
            TUMCabeClient.getInstance(context).getAdminTicketData(context, eventID, callback)
        } catch (e: IOException) {
            Utils.log(e)
        }
    }

    fun fetchTicketTypes(eventID: Int) {
        val database = TcaDb.getInstance(context)
        TUMCabeClient.getInstance(context)
                .fetchTicketTypes(eventID, object : Callback<List<TicketType>> {
                    override fun onResponse(call: Call<List<TicketType>>, response: Response<List<TicketType>>) {
                        val ticketTypes = response.body() ?: return
                        database.ticketTypeDao().insert(ticketTypes)
                    }

                    override fun onFailure(call: Call<List<TicketType>>, t: Throwable) {
                        Utils.log(t)
                    }
                })
    }

    fun getTicketTypeById(ticketTypeId: Int): TicketType = ticketTypeDao.getById(ticketTypeId)

    fun getTicketById(ticketId: Int): AdminTicket = adminTicketDao.getByTicketId(ticketId)

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


