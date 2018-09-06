package de.tum.`in`.tca.ticketcheck.component.ticket.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.api.TUMCabeClient
import de.tum.`in`.tca.ticketcheck.component.generic.activity.BaseActivity
import de.tum.`in`.tca.ticketcheck.component.ticket.EventsController
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.adapter.TicketsAdapter
import de.tum.`in`.tca.ticketcheck.component.ticket.fragment.TicketDetailsFragment
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicketRefreshCallback
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketStatus
import de.tum.`in`.tca.ticketcheck.utils.Const
import de.tum.`in`.tca.ticketcheck.utils.Utils
import kotlinx.android.synthetic.main.activity_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminDetailsActivity : BaseActivity(R.layout.activity_admin),
        TicketsAdapter.OnTicketSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private val ticketsAdapter: TicketsAdapter by lazy { TicketsAdapter(this) }
    private var tickets = listOf<AdminTicket>()

    private val eventID: Int by lazy { intent.getIntExtra(Const.EVENT_ID, 0) }
    private val ticketsController: TicketsController by lazy { TicketsController(this) }

    private lateinit var searchItem: MenuItem

    private var lastSelectedIndex: Int = 0
    private var totalTicketContingent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adminSwipeRefreshLayout.apply {
            isRefreshing = true
            setOnRefreshListener(this@AdminDetailsActivity)
            setColorSchemeResources(
                    R.color.color_primary,
                    R.color.tum_A100,
                    R.color.tum_A200
            )
        }

        val title = EventsController(this).getEventById(eventID).title

        supportActionBar?.title = title
        supportActionBar?.setSubtitle(R.string.loading)

        tickets = ticketsController.getTicketsForEvent(eventID)

        ticketsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminDetailsActivity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = ticketsAdapter
        }

        refreshTickets()
        refreshTicketCount()

        val floatingScanner = findViewById<FloatingActionButton>(R.id.scannerFab)
        floatingScanner.setOnClickListener { view -> openTicketScanActivity() }
    }

    public override fun onResume() {
        super.onResume()

        // Update the ticket that was being shown in the Ticket Detail View, in case the redemption state changed
        // TODO: Fix crash
        /*
        if (lastSelectedIndex < tickets.size()) {
            int ticketId = tickets.get(lastSelectedIndex).getId();
            AdminTicket ticket = TcaDb.getInstance(this).adminTicketDao().getByTicketId(ticketId);
            tickets.set(lastSelectedIndex, ticket);
            recyclerView.getAdapter().notifyItemChanged(lastSelectedIndex);
        }
        */
    }

    override fun onTicketSelected(ticket: AdminTicket, position: Int) {
        lastSelectedIndex = position
        TicketDetailsFragment
                .newInstance(ticket)
                .show(supportFragmentManager, "ticket_details_fragment")
    }

    private fun openTicketScanActivity() {
        val intent = Intent(this, TicketScanActivity::class.java).apply {
            putExtra(Const.EVENT_ID, eventID)
        }
        startActivity(intent)
    }

    override fun onRefresh() {
        refreshTickets()
        refreshTicketCount()
    }

    private fun refreshTickets() {
        ticketsController.refreshTickets(eventID, object : AdminTicketRefreshCallback {
            override fun onResult(results: List<AdminTicket>) {
                handleTicketRefreshSuccess(results)
            }

            override fun onFailure() {
                Utils.showToast(this@AdminDetailsActivity, R.string.error_something_wrong)
                adminSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun handleTicketRefreshSuccess(results: List<AdminTicket>) {
        tickets = results
        ticketsAdapter.update(tickets)

        updateTicketCounter()
        adminSwipeRefreshLayout.isRefreshing = false
    }

    private fun refreshTicketCount() {
        TUMCabeClient.getInstance(this).getTicketStats(eventID, object : Callback<List<TicketStatus>> {
            override fun onResponse(call: Call<List<TicketStatus>>,
                                    response: Response<List<TicketStatus>>) {
                val ticketStatuses = response.body() ?: return
                totalTicketContingent = ticketStatuses.sumBy{ it.contingent }
                updateTicketCounter()
            }

            override fun onFailure(call: Call<List<TicketStatus>>, t: Throwable) {
                Utils.log(t)
            }
        })
    }

    private fun updateTicketCounter() {
        supportActionBar?.let { actionBar ->
            val subtitle = getString(R.string.sold_tickets, tickets.size, totalTicketContingent)
            actionBar.subtitle = subtitle
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_admin_details, menu)

        searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        setupSearchView(searchView)

        return true
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String) = true

            override fun onQueryTextChange(queryText: String): Boolean {
                performSearch(queryText)
                return true
            }
        })

        searchView.setOnCloseListener {
            searchItem.collapseActionView()
            clearSearch()
            true
        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?) = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                clearSearch()
                return true
            }

        })
    }

    private fun performSearch(query: String) {
        val filtered = tickets.filter {
            it.name.contains(query, true) || it.lrzId.contains(query, true)
        }

        ticketsAdapter.update(filtered)
    }

    private fun clearSearch() {
        ticketsAdapter.update(tickets)
    }

}
