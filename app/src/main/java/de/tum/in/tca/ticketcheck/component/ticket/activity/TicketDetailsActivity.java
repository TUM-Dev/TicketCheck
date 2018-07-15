package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.activity.BaseActivity;
import de.tum.in.tca.ticketcheck.component.ticket.TicketsController;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse;
import de.tum.in.tca.ticketcheck.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            checkInButton.setOnClickListener(view -> openCheckInConfirmationDialog());
        } else {
            ticketStatustView.setText(R.string.status);
            checkInButton.setText(R.string.checked_in);
            checkInButton.setBackground(getDrawable(R.drawable.buttonshape_checked));
        }
    }

    private void openCheckInConfirmationDialog() {
        ContextThemeWrapper ctw =
                new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setTitle(getString(R.string.check_in_confirmation))
                .setMessage(getString(R.string.check_in_name, ticket.getName()))
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    redeemTicket();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });;

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * message to server to redeem ticket
     */
    private void redeemTicket(){
        Callback<TicketSuccessResponse> cb = new Callback<TicketSuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<TicketSuccessResponse> call,
                                   @NonNull Response<TicketSuccessResponse> response) {
                TicketSuccessResponse ticketSuccessResponse = response.body();
                if(ticketSuccessResponse != null && ticketSuccessResponse.getSuccess()){
                    checkInSuccessful();
                } else {
                    Utils.showToast(getApplicationContext(), R.string.check_in_failed);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TicketSuccessResponse> call, @NonNull Throwable t) {
                Utils.showToast(getApplicationContext(),
                        getString(R.string.no_internet_connection));
            }
        };
        ticketsController.redeemTicket(ticket.getId(), cb);
    }

    private void checkInSuccessful(){
        ticketStatustView.setText(R.string.status);
        checkInButton.setText(R.string.checked_in);
        checkInButton.setBackground(getDrawable(R.drawable.buttonshape_checked));
    }
}