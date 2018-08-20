package de.tum.`in`.tca.ticketcheck.component.ticket

import android.content.Context

import de.tum.`in`.tca.ticketcheck.api.TUMCabeClient
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.database.TcaDb
import de.tum.`in`.tca.ticketcheck.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsController(private val context: Context) {

    private val eventDao = TcaDb.getInstance(context).eventDao()

    val events: List<Event>
        get() = eventDao.getAll()

    fun refreshEvents(listener: OnEventsLoadedListener) {
        val cb = object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                val events = response.body() ?: emptyList()
                eventDao.insert(events)
                listener.onEventsLoaded(events)
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Utils.log(t)
                listener.onEventLoadingError()
            }
        }

        TUMCabeClient.getInstance(context).getEvents(cb)
    }

    fun getEventById(id: Int): Event = eventDao.getEventById(id)

    interface OnEventsLoadedListener {
        fun onEventsLoaded(events: List<Event>)
        fun onEventLoadingError()
    }

}

