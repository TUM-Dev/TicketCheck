package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.generic.CardViewHolder
import de.tum.`in`.tca.ticketcheck.component.ticket.EventCard
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import kotlinx.android.synthetic.main.card_events_item.view.*
import java.util.regex.Pattern

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var events = listOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_events_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventCard = EventCard(holder.itemView.context)
        val event = events[position]

        eventCard.event = event
        holder.currentCard = eventCard
        holder.bind(event)
    }

    override fun getItemCount() = events.size

    fun update(newEvents: List<Event>) {
        val diffResult = DiffUtil.calculateDiff(EventsDiffUtil(events, newEvents))
        events = newEvents
        diffResult.dispatchUpdatesTo(this)
    }

    class EventViewHolder(view: View) : CardViewHolder(view) {

        fun bind(event: Event) = with(itemView) {
            val title = COMPILE.matcher(event.title).replaceAll("")
            events_title.text = title
        }

    }

    companion object {
        private val COMPILE = Pattern.compile("^[0-9]+\\. [0-9]+\\. [0-9]+:[ ]*")
    }

}
