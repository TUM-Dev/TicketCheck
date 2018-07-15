package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.EventsController;

public class TicketDetailsActivity extends BaseActivity {
    private TextView ticketNameTextView;
    private TextView ticketlrzidTextView;
    private TextView ticketNoTextView;
    private TextView ticketPurchaseDateTextView;
    private TextView ticketStatustView;
    private Button checkInButton;

    private EventsController eventsController;

    public TicketDetailsActivity() {

        super(R.layout.activity_ticket_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketNameTextView = findViewById(R.id.ticket_name);
        ticketlrzidTextView = findViewById(R.id.ticket_lrzid);
        ticketNoTextView = findViewById(R.id.ticket_no);
        ticketPurchaseDateTextView = findViewById(R.id.ticket_purchase);
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

        Boolean ticketStatus = getIntent().getBooleanExtra("checked", false);
        if (!ticketStatus) {
            String statusString = "Status:Not checked in";
            ticketStatustView.setText(statusString);
            String checkInString = "Checked In";
            checkInButton.setText(checkInString);
            checkInButton.setOnClickListener(view -> checkin());
        } else {
            String statusString = "Status:";
            ticketStatustView.setText(statusString);
            String checkInString = "Checked";
            checkInButton.setText(checkInString);
            checkInButton.setBackground(getDrawable(R.drawable.buttonshape_checked));
        }
    }

    private void checkin() {
        String statusString = "Status:";
        ticketStatustView.setText(statusString);
        String checkInString = "Checked";
        checkInButton.setText(checkInString);
        checkInButton.setBackground(getDrawable(R.drawable.buttonshape_checked));
        //TODO:call endpoint of API checkin(), to change status of ticket at backend
    }
}