package de.tum.`in`.tca.ticketcheck.component.ticket.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.api.AuthenticationManager
import de.tum.`in`.tca.ticketcheck.component.generic.activity.BaseActivity
import de.tum.`in`.tca.ticketcheck.component.ticket.EventsController
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.adapter.EventsAdapter
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.utils.Utils
import kotlinx.android.synthetic.main.activity_events.*

class EventsActivity : BaseActivity(R.layout.activity_events), SwipeRefreshLayout.OnRefreshListener {

    private var adapter = EventsAdapter()
    private val eventsController: EventsController by lazy { EventsController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        AuthenticationManager(this).generatePrivateKey()

        setupRecyclerView()
        loadEvents()
    }

    override fun onRefresh() {
        loadEvents()
    }

    private fun setupRecyclerView() {
        eventsRecyclerView.setHasFixedSize(true)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)

        eventsRecyclerView.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        eventsRecyclerView.adapter = adapter

        eventsSwipeRefreshLayout.setOnRefreshListener(this)
        eventsSwipeRefreshLayout.isRefreshing = true
        eventsSwipeRefreshLayout.setColorSchemeResources(
                R.color.color_primary,
                R.color.tum_A100,
                R.color.tum_A200
        )
    }

    private fun loadEvents() {
        eventsController.refreshEvents(object : EventsController.OnEventsLoadedListener {
            override fun onEventsLoaded(events: List<Event>) {
                adapter.update(events)
                eventsSwipeRefreshLayout.isRefreshing = false

                val ticketsController = TicketsController(this@EventsActivity)
                events.forEach { ticketsController.fetchTicketTypes(it.id) }
            }

            override fun onEventLoadingError() {
                Utils.showToast(this@EventsActivity, R.string.error_something_wrong)
                eventsSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

}


