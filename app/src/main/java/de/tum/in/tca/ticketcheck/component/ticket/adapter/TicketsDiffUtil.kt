package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import androidx.recyclerview.widget.DiffUtil
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Customer

class TicketsDiffUtil(
        private val oldItems: List<Customer>,
        private val newItems: List<Customer>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].lrzId == newItems[newItemPosition].lrzId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

}