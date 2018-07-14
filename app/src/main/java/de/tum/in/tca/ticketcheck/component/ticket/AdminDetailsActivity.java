package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.utils.Utils;

public class AdminDetailsActivity extends BaseActivity {

    private SearchView searchView;
    private ListView listView;
    private TicketListAdapter ticketListAdapter;
    private List<AdminTicket> tickets;

    public AdminDetailsActivity() {
        super(R.layout.activity_admin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchView = findViewById(R.id.search_ticket);
        TextView totalSaleView = findViewById(R.id.sold_tickets);
        listView = findViewById(R.id.ticket_list);
        FloatingActionButton floatingScanner = findViewById(R.id.fab_scanner);

        int clickedEventId = getIntent().getIntExtra("event_id", 0);
        //TODO:send event_id to backend and receive really ticket data,following just use dummy ticketdata
        tickets = TicketsController.getTickets();

        ticketListAdapter = new TicketListAdapter(tickets, this);
        listView.setAdapter(ticketListAdapter);

        //Set total ticket and total sale
        //TODO:send event_id to backend and receive really sum of ticket,following just use dummy ticketdata
        int totalNumberOfTickets = 100;
        int numberOfSoldTickets = tickets.size();
        totalSaleView.setText(getString(R.string.sold_tickets,
                numberOfSoldTickets, totalNumberOfTickets));

        //Set FAB to scanner
        floatingScanner.setOnClickListener(view -> openTicketScanActivity());
        // Set search function
        setupSearchView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_refresh) {
            //TODO:send event_id to backend and receive really ticket data,following just use dummy ticketdata
            tickets = TicketsController.getrefreshTickets();
            ticketListAdapter = new TicketListAdapter(tickets, this);
            listView.setAdapter(ticketListAdapter);
            ticketListAdapter.notifyDataSetChanged();
        }
        return true;
    }

    private void openTicketScanActivity() {
        startActivity(new Intent(this, TicketScanActivity.class));
        //TODO:send current eventId to TicketScanActivity
    }

    private void setupSearchView() {
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String queryText) {
                boolean searchSuccessful = search(queryText);

                if (!searchSuccessful){
                    Utils.showToast(AdminDetailsActivity.this, getString(R.string.not_in_list));
                }

                return true;
            }

            public boolean onQueryTextChange(String queryText) {
                search(queryText);
                return true;
            }

            private boolean search(String queryText){
                List<AdminTicket> foundTicketList = new ArrayList<>();
                for (AdminTicket foundTicket : tickets) {
                    if (foundTicket.getName().toLowerCase().contains(queryText.toLowerCase()) ||
                            foundTicket.getLrzId().toLowerCase().contains(queryText.toLowerCase())) {
                        foundTicketList.add(foundTicket);
                    }
                }
                // for an empty string all tickets are matched
                ticketListAdapter = new TicketListAdapter(foundTicketList, AdminDetailsActivity.this);
                ticketListAdapter.notifyDataSetChanged();
                listView.setAdapter(ticketListAdapter);

                return !foundTicketList.isEmpty();
            }
        });
    }
}
