package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;

public class TicketDetailsActivity extends BaseActivity {
    private TextView ticketStatustView;
    private Button checkInButton;

    public TicketDetailsActivity() {

        super(R.layout.activity_ticket_details);
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

        ticketNameTextView.setText(getIntent().getStringExtra("name"));
        ticketLrzIdTextView.setText(getIntent().getStringExtra("lrzid"));
        ticketNoTextView.setText(
                String.valueOf(getIntent().getIntExtra("ticketId", -1)));
        ticketPurchaseDateTextView.setText(getIntent().getStringExtra("purchasedate"));

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