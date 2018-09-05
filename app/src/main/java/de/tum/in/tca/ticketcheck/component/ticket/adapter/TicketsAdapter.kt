package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import kotlinx.android.synthetic.main.ticket_list_item.view.*
import org.jetbrains.anko.textColor

class TicketsAdapter(
        private val listener: OnTicketSelectedListener
) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    private var tickets = mutableListOf<AdminTicket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ticket_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tickets[position], listener)
    }

    override fun getItemCount() = tickets.size

    fun update(newItems: List<AdminTicket>) {
        val diffResult = DiffUtil.calculateDiff(TicketsDiffUtil(this.tickets, newItems))
        tickets.clear()
        tickets.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ticket: AdminTicket, listener: OnTicketSelectedListener) = with(itemView) {
            name.text = ticket.name
            lrz_id.text = ticket.lrzId
            ticket_type.text = ticket.ticketType.toString()

            val purchaseColor = if (ticket.isPurchased) {
                R.color.text_dark_gray
            } else {
                R.color.grade_4_7
            }
            purchase.textColor = ContextCompat.getColor(context, purchaseColor)

            ticket.purchaseDate?.let {
                val formattedDate = Event.getFormattedDateTime(context, it)
                purchase.text = context.getString(R.string.purchased_format_string, formattedDate)
            }

            val redeemColor = if (ticket.isRedeemed) {
                R.color.text_dark_gray
            } else {
                R.color.grade_3_3
            }
            purchase.textColor = ContextCompat.getColor(context, redeemColor)

            ticket.redeemDate?.let {
                val formattedDate = Event.getFormattedDateTime(context, it)
                redemption.text = context.getString(R.string.redeemed_format_string, formattedDate)
            }

            setOnClickListener { listener.onTicketSelected(ticket, adapterPosition) }
        }

    }

    interface OnTicketSelectedListener {
        fun onTicketSelected(ticket: AdminTicket, position: Int)
    }

}