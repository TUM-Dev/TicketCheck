package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import androidx.recyclerview.widget.DiffUtil
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event

class EventsDiffUtil(
        private val oldItems: List<Event>,
        private val newItems: List<Event>
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