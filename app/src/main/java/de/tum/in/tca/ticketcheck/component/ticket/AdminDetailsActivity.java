package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import io.reactivex.disposables.CompositeDisposable;


public class AdminDetailsActivity extends BaseActivity {

    public AdminDetailsActivity() {
        super(R.layout.activity_admin);
    }

    private final CompositeDisposable disposable = new CompositeDisposable();
    private EventsController eventsController;
    private MenuItem menuItemScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsController = new EventsController(this);

        // set up ViewPager and adapter
        ViewPager mPager = findViewById(R.id.pager);

        // TODO: extract key to const class
        int clickedEventId = getIntent().getIntExtra("event_id", 0);

        List<Event> events = eventsController.getEvents();

        AdminDetailsAdapter eventDetailsAdapter = new AdminDetailsAdapter(getSupportFragmentManager(),
                events);

        mPager.setAdapter(eventDetailsAdapter);

        // Use clickedEventId to show the clicked event in the EventDetailsView
        int startPosition = 0;
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if (event.getId() == clickedEventId) {
                startPosition = i;
                break;
            }
        }

        mPager.setCurrentItem(startPosition);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_admin, menu);
        menuItemScanView = menu.findItem(R.id.action_scanner);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_scanner) {
            startActivity(new Intent(this, TicketScanActivity.class));
            //TODO:send current eventId to TicketScanActivity
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
