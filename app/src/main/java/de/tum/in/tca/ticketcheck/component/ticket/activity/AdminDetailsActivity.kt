package de.tum.`in`.tca.ticketcheck.component.ticket.activity

import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.R.string.ticket
import de.tum.`in`.tca.ticketcheck.component.generic.activity.BaseActivity
import de.tum.`in`.tca.ticketcheck.component.ticket.EventsController
import de.tum.`in`.tca.ticketcheck.component.ticket.adapter.TicketsAdapter
import de.tum.`in`.tca.ticketcheck.component.ticket.fragment.TicketDetailsFragment
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Customer
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketContingent
import de.tum.`in`.tca.ticketcheck.component.ticket.viewmodel.AdminDetailsViewModel
import de.tum.`in`.tca.ticketcheck.utils.Const
import de.tum.`in`.tca.ticketcheck.utils.observeNonNull
import kotlinx.android.synthetic.main.activity_admin.*

class AdminDetailsActivity : BaseActivity(
        R.layout.activity_admin
), TicketsAdapter.OnTicketSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private val ticketsAdapter: TicketsAdapter by lazy { TicketsAdapter(this) }

    private val snackbar: Snackbar by lazy {
        val view = findViewById<ViewGroup>(android.R.id.content)
        Snackbar.make(view.getChildAt(0), R.string.something_wrong, Snackbar.LENGTH_INDEFINITE)
    }

    private lateinit var searchItem: MenuItem

    private val eventID: Int by lazy { intent.getIntExtra(Const.EVENT_ID, 0) }

    private val viewModel: AdminDetailsViewModel by lazy {
        val factory = AdminDetailsViewModel.Factory(application, eventID)
        ViewModelProviders.of(this, factory).get(AdminDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adminSwipeRefreshLayout.apply {
            setOnRefreshListener(this@AdminDetailsActivity)
            setColorSchemeResources(
                    R.color.color_primary,
                    R.color.tum_A100,
                    R.color.tum_A200)
        }

        val title = EventsController(this).getEventById(eventID).title
        supportActionBar?.title = title
        supportActionBar?.setSubtitle(R.string.loading)

        ticketsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminDetailsActivity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = ticketsAdapter
        }

        viewModel.customers.observeNonNull(this) { updateTickets(it) }
        viewModel.ticketContingent.observeNonNull(this) { updateTicketCounter(it) }
        viewModel.error.observeNonNull(this) { showError(it) }

        refreshTicketsAndStats()

        scannerFab.setOnClickListener { openTicketScanActivity() }
    }

    override fun onTicketSelected(ticketIds: List<Int>, position: Int) {
        val fragment = TicketDetailsFragment.newInstance(ticketIds)
        fragment.show(supportFragmentManager, fragment.tag)
    }

    private fun openTicketScanActivity() {
        val intent = Intent(this, TicketScanActivity::class.java).apply {
            putExtra(Const.EVENT_ID, eventID)
        }
        startActivity(intent)
    }

    override fun onRefresh() {
        refreshTicketsAndStats()
    }

    private fun refreshTicketsAndStats() {
        adminSwipeRefreshLayout.isRefreshing = true
        viewModel.fetchTickets()
    }

    private fun updateTickets(customers: List<Customer>) {
        adminSwipeRefreshLayout.isRefreshing = false

        if (customers.isNotEmpty()) {
            ticketsRecyclerView.visibility = View.VISIBLE
            placeholderTextView.visibility = View.GONE
            ticketsAdapter.update(customers, eventID)
        } else {
            ticketsRecyclerView.visibility = View.GONE
            placeholderTextView.visibility = View.VISIBLE
        }
    }

    private fun updateTicketCounter(contingent: TicketContingent) {
        supportActionBar?.subtitle =
                getString(R.string.sold_tickets, contingent.sold, contingent.contingent)
    }

    private fun showError(show: Boolean) {
        adminSwipeRefreshLayout.isRefreshing = false
        if (show) {
            snackbar.show()
        } else {
            snackbar.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_admin_details, menu)

        searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        setupSearch(searchItem, searchView)

        return true
    }

    private fun setupSearch(searchItem: MenuItem, searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String) = true

            override fun onQueryTextChange(query: String): Boolean {
                performSearch(query)
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
        viewModel.filter(query)
    }

    private fun clearSearch() {
        viewModel.filter(null)
    }

}
