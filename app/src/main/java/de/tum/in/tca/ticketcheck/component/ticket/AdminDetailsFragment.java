package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.utils.Const;
import io.reactivex.disposables.CompositeDisposable;

import static java.text.DateFormat.getDateInstance;

/**
 * Fragment for EventDetails. Manages content that gets shown on the pagerView
 */
public class AdminDetailsFragment extends Fragment {

    private Context context;
    private Event event;
    private LayoutInflater inflater;
    private EventsController eventsController;
    private SearchView searchView;
    private ListView listView;
    private ticketListAdapter mAdapter;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_admin_detail, container, false);
        searchView = fragmentView.findViewById(R.id.searchTicket);
        listView = fragmentView.findViewById(R.id.listTicket);
        List<AdminTicket> tickets;
        tickets = TicketsController.getTickets();
        mAdapter = new ticketListAdapter(tickets, fragmentView.getContext());
        listView.setAdapter(mAdapter);

        //searchView.setOnClickListener(view -> showMap());
        eventsController = new EventsController(this.getContext());
        // position in database
        int position = getArguments().getInt(Const.POSITION);
        event = eventsController.getEvents().get(position);
        //TODO:get all ticket according to current
        return fragmentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
