package de.tum.`in`.tca.ticketcheck.component.ticket.viewmodel

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketTypeCount

class TicketTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val amountTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.nrOfTickets) }
    private val ticketTypeNameTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.ticketTypeTextView) }

    fun bind(ticketTypeCount: TicketTypeCount) {
        amountTextView.text = itemView.context.getString(R.string.amount_x, ticketTypeCount.count)
        ticketTypeNameTextView.text = ticketTypeCount.description
    }
}