package de.tum.in.tca.ticketcheck.component.ticket.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.api.TUMCabeClient;
import de.tum.in.tca.ticketcheck.component.ticket.payload.TicketValidityResponse;
import de.tum.in.tca.ticketcheck.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmCheckInFragment extends BottomSheetDialogFragment {

    public static ConfirmCheckInFragment newInstance(String eventId, String code) {
        ConfirmCheckInFragment fragment = new ConfirmCheckInFragment();
        Bundle args = new Bundle();
        args.putString("eventId", eventId);
        args.putString("code", code);
        fragment.setArguments(args);
        return fragment;
    }

    private ProgressBar progressBar;
    private TextView nameTextView;
    private AppCompatButton confirmButton;
    private AppCompatButton denyButton;

    private String eventId;
    private String code;
    private TicketValidityResponse ticketValidityResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
            code = getArguments().getString("code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_check_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.name_text_view);
        progressBar = view.findViewById(R.id.progress_bar);

        confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> confirmTicket());

        denyButton = view.findViewById(R.id.deny_button);
        denyButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            TUMCabeClient
                    .getInstance(getContext())
                    .getTicketValidity(getActivity().getApplicationContext(), eventId, code, new Callback<TicketValidityResponse>() {
                        @Override
                        public void onResponse(Call<TicketValidityResponse> call, Response<TicketValidityResponse> response) {
                            ticketValidityResponse = response.body();

                            if (ticketValidityResponse == null) {
                                closeWithErrorMessage();
                                return;
                            }

                            nameTextView.setText(ticketValidityResponse.getTicketInfo());
                            nameTextView.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);

                            confirmButton.setVisibility(View.VISIBLE);

                            denyButton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<TicketValidityResponse> call, Throwable t) {
                            closeWithErrorMessage();
                        }
                    });
        } catch (IOException e) {
            Utils.log(e);
        }
    }

    public void confirmTicket() {

    }

    private void closeWithErrorMessage() {
        Utils.showToast(getContext(), "Server war Schuld");
        dismiss();
    }

}
