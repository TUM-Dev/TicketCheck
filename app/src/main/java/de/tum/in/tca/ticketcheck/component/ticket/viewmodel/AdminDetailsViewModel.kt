package de.tum.`in`.tca.ticketcheck.component.ticket.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.callbacks.ApiResponseCallback
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketContingent
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketStatus
import java.util.*
import kotlin.concurrent.schedule

class AdminDetailsViewModel(
        application: Application,
        private val eventId: Int
) : AndroidViewModel(application) {

    private val ticketsController: TicketsController by lazy {
        TicketsController(application.baseContext)
    }

    private val tickets = mutableListOf<AdminTicket>()

    val adminTickets = MediatorLiveData<List<AdminTicket>>()

    val filteredTickets = MediatorLiveData<List<AdminTicket>>()

    val ticketContingent = MutableLiveData<TicketContingent>()

    val error = MutableLiveData<Boolean>()

    val query = MutableLiveData<String>()

    init {
        val ticketsLiveData = ticketsController.getTicketsForEvent(eventId)

        adminTickets.addSource(ticketsLiveData) { values ->
            val newTickets = values ?: return@addSource
            tickets.clear()
            tickets.addAll(newTickets)
            val query = query.value
            adminTickets.value = tickets.filter { it.filter(query) }
        }

        adminTickets.addSource(query) { value ->
            val newQuery = value ?: return@addSource
            adminTickets.value = tickets.filter { it.filter(newQuery) }
        }
    }

    fun filter(value: String) {
        query.value = value
    }

    fun fetchTickets() {
        ticketsController.refreshTickets(eventId, object : ApiResponseCallback<List<AdminTicket>> {
            override fun onResult(result: List<AdminTicket>) {
                insert(result)
                fetchTicketStats()
            }

            override fun onFailure() {
                showError()
            }
        })
    }

    fun fetchTicketStats() {
        ticketsController.refreshTicketStats(eventId, object : ApiResponseCallback<List<TicketStatus>> {
            override fun onResult(result: List<TicketStatus>) {
                ticketContingent.value = TicketContingent.fromTicketStatuses(result)
            }

            override fun onFailure() {
                showError()
            }
        })
    }

    private fun showError() {
        error.value = true
        Timer().schedule(5000) {
            error.postValue(false)
        }
    }

    fun insert(tickets: List<AdminTicket>) {
        ticketsController.insertTickets(tickets)
    }

    class Factory(
            private val application: Application,
            private val eventId: Int
    ) : ViewModelProvider.AndroidViewModelFactory(application) {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AdminDetailsViewModel(application, eventId) as T
        }

    }

}