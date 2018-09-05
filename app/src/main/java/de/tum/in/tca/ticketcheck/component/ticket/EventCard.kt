package de.tum.`in`.tca.ticketcheck.component.ticket

import android.content.Context
import android.content.Intent

import de.tum.`in`.tca.ticketcheck.component.generic.Card
import de.tum.`in`.tca.ticketcheck.component.ticket.activity.AdminDetailsActivity
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.utils.Const

class EventCard(
        context: Context
) : Card(CARD_TYPE, context, "events", false) {

    var event: Event? = null

    override fun getIntent(): Intent? {
        val eventId = event?.id ?: return null
        return Intent(context, AdminDetailsActivity::class.java).apply {
            putExtra(Const.EVENT_ID, eventId)
        }
    }

    companion object {
        private const val CARD_TYPE = 17
    }

}
