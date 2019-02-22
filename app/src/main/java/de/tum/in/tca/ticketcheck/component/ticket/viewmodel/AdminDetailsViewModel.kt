package de.tum.`in`.tca.ticketcheck.component.ticket.viewmodel

import android.app.Application
import androidx.lifecycle.*
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.callbacks.ApiResponseCallback
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Customer
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

    private val allCustomers = mutableListOf<Customer>()

    val customers = MediatorLiveData<List<Customer>>()

    val ticketContingent = MutableLiveData<TicketContingent>()

    val error = MutableLiveData<Boolean>()

    val query = MutableLiveData<String?>()

    init {
        val customersLiveData = ticketsController.getCustomersByEventId(eventId)

        customers.addSource(customersLiveData) { values ->
            val newCustomers = values ?: return@addSource
            allCustomers.clear()
            allCustomers.addAll(newCustomers)
            val query = query.value
            customers.value = allCustomers.filter { it.filter(query) }
        }

        customers.addSource(query) { value ->
            val newQuery = value ?: return@addSource
            customers.value = allCustomers.filter { it.filter(newQuery) }
        }
    }

    fun filter(value: String?) {
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
        ticketsController.replaceTickets(tickets)
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