package de.tum.in.tca.ticketcheck.component.ticket;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.generic.adapter.EqualSpacingItemDecoration;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.database.TcaDb;

public class EventsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private EventsController eventsController;

    public EventsActivity() {
        super(R.layout.activity_events);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TcaDb.getInstance(this);

        eventsController = new EventsController(getApplicationContext());

        ActionBar actionBar = getSupportActionBar(); // getActionBar() seems to return null...
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
        setRecyclerView();
        mSwipeLayout = findViewById(R.id.event_refresh);
        mSwipeLayout.setOnRefreshListener(this);
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.activity_events_list_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacing = Math.round(getResources().getDimension(R.dimen.material_card_view_padding));
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(spacing));
        loadEvents();
    }

    private void loadEvents(){
        List<Event> events = eventsController.refreshEvents();
        EventsAdapter adapter = new EventsAdapter(events);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadEvents();
        mSwipeLayout.setRefreshing(false);
    }
}


