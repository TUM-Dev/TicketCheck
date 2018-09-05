package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import android.support.v7.util.DiffUtil
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket

class TicketsDiffUtil(
        private val oldItems: List<AdminTicket>,
        private val newItems: List<AdminTicket>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

}