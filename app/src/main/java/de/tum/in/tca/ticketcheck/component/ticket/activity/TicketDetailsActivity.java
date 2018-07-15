package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.TicketsController;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;

public class TicketDetailsActivity extends BaseActivity {
    private TextView ticketStatustView;
    private Button checkInButton;

    private AdminTicket ticket;

    TicketsController ticketsController;

    public TicketDetailsActivity() {
        super(R.layout.activity_ticket_details);
        ticketsController = new TicketsController(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView ticketNameTextView = findViewById(R.id.ticket_name);
        TextView ticketLrzIdTextView = findViewById(R.id.ticket_lrzid);
        TextView ticketNoTextView = findViewById(R.id.ticket_no);
        TextView ticketPurchaseDateTextView = findViewById(R.id.ticket_purchase);
        ticketStatustView = findViewById(R.id.ticket_status);
        checkInButton = findViewById(R.id.check_in);

        ticket = ticketsController.
                getTicketById(getIntent().getIntExtra("ticketId", -1));

        ticketNameTextView.setText(ticket.getName());
        ticketLrzIdTextView.setText(ticket.getLrzId());
        ticketNoTextView.setText(String.valueOf(ticket.getId()));
        ticketPurchaseDateTextView.setText(DateTimeFormat.mediumDateTime().
                print(ticket.getPurchaseDate()));

        boolean ticketStatus = getIntent().getBooleanExtra("checked", false);
        if (!ticketStatus) {
            ticketStatustView.setText(R.string.check_in_status);
            checkInButton.setText(R.string.check_in);
            checkInButton.setOnClickListener(view -> checkIn());
        } else {
            ticketStatustView.setText(R.string.status);
            checkInButton.setText(R.string.checked_in);
            checkInButton.setBackground(getDrawable(R.drawable.buttonshape_checked));
        }
    }

    private void checkIn() {
        ticketStatustView.setText(R.string.status);
        checkInButton.setText(R.string.checked_in);
        checkInButton.setBackground(getDrawable(R.drawable.buttonshape_checked));
        //TODO:call endpoint of API checkIn(), to change status of ticket at backend
    }
}