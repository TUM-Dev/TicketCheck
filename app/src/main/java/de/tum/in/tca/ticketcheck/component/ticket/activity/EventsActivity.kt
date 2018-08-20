package de.tum.`in`.tca.ticketcheck.component.ticket.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.api.AuthenticationManager
import de.tum.`in`.tca.ticketcheck.component.generic.activity.BaseActivity
import de.tum.`in`.tca.ticketcheck.component.generic.adapter.EqualSpacingItemDecoration
import de.tum.`in`.tca.ticketcheck.component.ticket.EventsController
import de.tum.`in`.tca.ticketcheck.component.ticket.adapter.EventsAdapter
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.utils.Utils
import kotlinx.android.synthetic.main.activity_events.*

class EventsActivity : BaseActivity(R.layout.activity_events), SwipeRefreshLayout.OnRefreshListener {

    private var adapter = EventsAdapter()
    private lateinit var eventsController: EventsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        AuthenticationManager(this).generatePrivateKey()
        eventsController = EventsController(this)

        setupRecyclerView()
        loadEvents()
    }

    override fun onRefresh() {
        loadEvents()
    }

    private fun setupRecyclerView() {
        eventsRecyclerView.setHasFixedSize(true)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)

        val spacing = Math.round(resources.getDimension(R.dimen.material_card_view_padding))
        eventsRecyclerView.addItemDecoration(EqualSpacingItemDecoration(spacing))

        eventsRecyclerView.adapter = adapter

        eventsSwipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun loadEvents() {
        eventsController.refreshEvents(object : EventsController.OnEventsLoadedListener {
            override fun onEventsLoaded(events: List<Event>) {
                adapter.update(events)
                eventsSwipeRefreshLayout.isRefreshing = false
            }

            override fun onEventLoadingError() {
                Utils.showToast(this@EventsActivity, R.string.error_something_wrong)
                eventsSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

}


