package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;

public class AdminDetailsActivity extends BaseActivity {

    private SearchView searchView;
    private ListView listView;
    private TextView totalSaleView;
    private FloatingActionButton floatingScanner;
    private ticketListAdapter mAdapter;
    private ticketListAdapter findAdapter;
    private List<AdminTicket> tickets;
    private List<AdminTicket> findList;
    private MenuItem menuItemScanView;

    public AdminDetailsActivity() {
        super(R.layout.activity_admin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchView = findViewById(R.id.searchTicket);
        totalSaleView = findViewById(R.id.total_sale);
        listView = findViewById(R.id.listTicket);
        floatingScanner = findViewById(R.id.fab_scanner);

        int clickedEventId = getIntent().getIntExtra("event_id", 0);
        //TODO:send event_id to backend and receive really ticket data,following just use dummy ticketdata
        tickets = TicketsController.getTickets();

        findList = new ArrayList<>();
        mAdapter = new ticketListAdapter(tickets, this);
        listView.setAdapter(mAdapter);

        //Set total ticket and total sale
        //TODO:send event_id to backend and receive really sum of ticket,following just use dummy ticketdata
        int totalTicketNumber = 100;
        int totalSaleNumber = tickets.size();
        String totalSaleString = "Total Sale : " + totalSaleNumber + "/" + totalTicketNumber;
        totalSaleView.setText(totalSaleString);

        //Set FAB to scanner
        floatingScanner.setOnClickListener(view -> scanning());
        // Set search function
        setupSearchView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_admin, menu);
        menuItemScanView = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_refresh) {
            //TODO:send event_id to backend and receive really ticket data,following just use dummy ticketdata
            tickets = TicketsController.getrefreshTickets();
            mAdapter = new ticketListAdapter(tickets, this);
            listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    private void scanning() {
        startActivity(new Intent(this, TicketScanActivity.class));
        //TODO:send current eventId to TicketScanActivity
    }

    private void setupSearchView() {
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(AdminDetailsActivity.this, "please text something", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(mAdapter);
                } else {
                    findList.clear();
                    for (int i = 0; i < tickets.size(); i++) {
                        AdminTicket findticket = tickets.get(i);
                        if (findticket.getName().toLowerCase().equals(query.toLowerCase())) {
                            findList.add(findticket);
                            break;
                        }
                    }
                    if (findList.size() == 0) {
                        Toast.makeText(AdminDetailsActivity.this, "The name is not in the list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminDetailsActivity.this, "Search successfully", Toast.LENGTH_SHORT).show();
                        findAdapter = new ticketListAdapter(findList, AdminDetailsActivity.this);
                        listView.setAdapter(findAdapter);
                    }
                }
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    listView.setAdapter(mAdapter);
                } else {
                    findList.clear();
                    for (int i = 0; i < tickets.size(); i++) {
                        AdminTicket findticket = tickets.get(i);
                        if (findticket.getName().toLowerCase().contains(newText.toLowerCase())) {
                            findList.add(findticket);
                        }
                    }
                    findAdapter = new ticketListAdapter(findList, AdminDetailsActivity.this);
                    findAdapter.notifyDataSetChanged();
                    listView.setAdapter(findAdapter);
                }
                return true;
            }
        });
    }
}
