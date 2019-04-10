package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketTypeCount
import de.tum.`in`.tca.ticketcheck.component.ticket.viewmodel.TicketTypeViewHolder

class TicketTypeAdapter(private val ticketInfos: List<TicketTypeCount>) : RecyclerView.Adapter<TicketTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): TicketTypeViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.ticket_type_item, parent, false)
        return TicketTypeViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: TicketTypeViewHolder, i: Int) {
        viewHolder.bind(ticketInfos[i])
    }

    override fun getItemCount(): Int {
        return ticketInfos.size
    }
}