package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import de.tum.in.tca.ticketcheck.component.ticket.activity.AdminDetailsActivity;
import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ui.overview.card.Card;

public class EventCard extends Card {

    private Event event;

    public void setEvent(Event event){
        this.event = event;
    }

    public EventCard(Context context) {
        super(CardManager.CARD_EVENTS, context, "events", false);
    }

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(getContext(), AdminDetailsActivity.class);
        intent.putExtra("event_id", event.getId());
        return intent;
    }

    @Override
    protected void discard(@NotNull SharedPreferences.Editor editor) {

    }
}
