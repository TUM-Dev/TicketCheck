package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.common.collect.ImmutableList;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.ticket.TicketsController;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.utils.Utils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private TicketsController ticketsController;
    private int eventId;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_ticket_scan);

        mScannerView = findViewById(R.id.scanner_view);

        ticketsController = new TicketsController(this);

        eventId = getIntent().getIntExtra("eventId", -1);

        List<BarcodeFormat> formats = ImmutableList.of(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (!granted) { //
                        finish();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Callback<TicketValidityResponse> cb = new Callback<TicketValidityResponse>() {
            @Override
            public void onResponse(@NonNull Call<TicketValidityResponse> call,
                                   @NonNull Response<TicketValidityResponse> response) {
                TicketValidityResponse ticketValidityResponse = response.body();
                if(ticketValidityResponse.valid){
                    Intent intent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
                    intent.putExtra("ticketId", ticketValidityResponse.ticketHistory);
                    startActivity(intent);
                } else {
                    Utils.showToast(getApplicationContext(), R.string.not_valid);
                    mScannerView.stopCamera();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TicketValidityResponse> call,
                                  @NonNull Throwable t) {
                Utils.log(t);
                Utils.showToast(getApplicationContext(), R.string.no_internet_connection);
                mScannerView.stopCamera();
                finish();
            }
        };
        ticketsController.checkTicketValidity(eventId, rawResult.getText(), cb);
    }

}
