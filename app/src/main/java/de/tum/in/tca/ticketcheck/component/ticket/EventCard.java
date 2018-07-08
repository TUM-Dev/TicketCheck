package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.component.ui.overview.card.NotificationAwareCard;
import de.tum.in.tumcampusapp.component.ui.overview.CardManager;

public class EventCard extends NotificationAwareCard {

    private Event event;

    public void setEvent(Event event){
        this.event = event;
    }

    public EventCard(Context context) {
        super(CardManager.CARD_EVENTS, context, "events");
    }

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(getContext(), EventDetailsActivity.class);
        intent.putExtra("event_id", event.getId());
        return intent;
    }

    @NotNull
    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected void discard(@NotNull SharedPreferences.Editor editor) {

    }
}
