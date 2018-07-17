package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.api.TUMCabeClient;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.TicketsController;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketStatus;
import de.tum.in.tca.ticketcheck.database.TcaDb;
import de.tum.in.tca.ticketcheck.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDetailsActivity extends BaseActivity {

    private SearchView searchView;
    private ListView listView;
    private TextView totalSaleView;
    private TicketListAdapter ticketListAdapter;
    private List<AdminTicket> tickets;

    private int eventID;
    private int totalTicketCount;
    private TicketsController ticketsController;
    private int lastSelectedIndex;

    public AdminDetailsActivity() {
        super(R.layout.activity_admin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchView = findViewById(R.id.search_ticket);
        totalSaleView = findViewById(R.id.sold_tickets);
        listView = findViewById(R.id.ticket_list);
        FloatingActionButton floatingScanner = findViewById(R.id.fab_scanner);

        eventID = getIntent().getIntExtra("event_id", 0);

        ticketsController = new TicketsController(this);
        // Retrieve the saved ticket data from local database
        tickets = ticketsController.getTicketsForEvent(eventID);
        ticketListAdapter = new TicketListAdapter(tickets, AdminDetailsActivity.this);
        listView.setAdapter(ticketListAdapter);
        ticketListAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminTicket ticket = (AdminTicket) parent.getItemAtPosition(position);
                lastSelectedIndex = position;
                Intent intent = new Intent(AdminDetailsActivity.this, TicketDetailsActivity.class);
                intent.putExtra("ticketId", ticket.getId());
                startActivity(intent);
            }
        });

        // Invoke ticket refresh
        refreshTicketList();

        //Set total ticket and total sale
        totalTicketCount = 0;
        updateTicketCounter();
        TUMCabeClient.getInstance(AdminDetailsActivity.this).getTicketStats(eventID, new Callback<List<TicketStatus>>() {
            @Override
            public void onResponse(@NonNull Call<List<TicketStatus>> call, @NonNull Response<List<TicketStatus>> response) {
                List<TicketStatus> list = response.body() != null
                        ? response.body()
                        : new ArrayList<>();
                totalTicketCount = 0;
                for (TicketStatus status : list) {
                    totalTicketCount += status.getContingent();
                }
                updateTicketCounter();
            }

            @Override
            public void onFailure(@NonNull Call<List<TicketStatus>> call, @NonNull Throwable t) {
                Utils.log(t);
            }
        });

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
    public void onResume() {
        super.onResume();
        // Update the ticket that was being shown in the Ticket Detail View, in case the redemption state changed
        if (lastSelectedIndex < tickets.size()) {
            tickets.set(lastSelectedIndex,
                    TcaDb.getInstance(this).adminTicketDao().getByTicketId(tickets.get(lastSelectedIndex).getId()));
            ((TicketListAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_refresh) {
            refreshTicketList();
        }
        return true;
    }

    private void openTicketScanActivity() {
        Intent intent = new Intent(this, TicketScanActivity.class);
        intent.putExtra("eventId", eventID);
        startActivity(intent);
    }

    private void setupSearchView() {
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String queryText) {
                boolean searchSuccessful = search(queryText);

                if (!searchSuccessful) {
                    Utils.showToast(AdminDetailsActivity.this, getString(R.string.not_in_list));
                }

                return true;
            }

            public boolean onQueryTextChange(String queryText) {
                search(queryText);
                return true;
            }

            private boolean search(String queryText) {
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

    private void refreshTicketList() {
        ticketsController.refreshTickets(eventID, list -> {
            tickets = list;
            ticketListAdapter = new TicketListAdapter(tickets, AdminDetailsActivity.this);
            listView.setAdapter(ticketListAdapter);
            ticketListAdapter.notifyDataSetChanged();
            updateTicketCounter();
        });
    }

    private void updateTicketCounter() {
        totalSaleView.setText(getString(R.string.sold_tickets, tickets.size(), totalTicketCount));
    }
}
