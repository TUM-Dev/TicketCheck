package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.EventsController;

public class TicketDetailsActivity extends BaseActivity {
    private TextView ticketStatustView;
    private Button checkInButton;

    private EventsController eventsController;

    public TicketDetailsActivity() {

        super(R.layout.activity_ticket_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView ticketNameTextView = findViewById(R.id.ticket_name);
        TextView ticketlrzidTextView = findViewById(R.id.ticket_lrzid);
        TextView ticketNoTextView = findViewById(R.id.ticket_no);
        TextView ticketPurchaseDateTextView = findViewById(R.id.ticket_purchase);
        ticketStatustView = findViewById(R.id.ticket_status);
        checkInButton = findViewById(R.id.check_in);

        String nameString = getIntent().getStringExtra("name");
        ticketNameTextView.setText(nameString);

        String lrzidString = "TUM-ID:\n" + getIntent().getStringExtra("lrzid");
        ticketlrzidTextView.setText(lrzidString);

        String ticketnoString = "Ticket No:\n" + getIntent().getStringExtra("ticketId");
        ticketNoTextView.setText(ticketnoString);

        String purchaseDateString = "Purchase Date:\n" + getIntent().getStringExtra("purchasedate");
        ticketPurchaseDateTextView.setText(purchaseDateString);

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