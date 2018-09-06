package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType
import kotlinx.android.synthetic.main.ticket_list_item.view.*
import org.jetbrains.anko.textColor

class TicketsAdapter(
        context: Context
) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    private val listener = context as OnTicketSelectedListener
    private val ticketsController: TicketsController by lazy { TicketsController(context) }

    private val tickets = mutableListOf<AdminTicket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ticket_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets[position]
        val ticketType = ticketsController.getTicketTypeById(ticket.ticketType)
        holder.bind(ticket, ticketType, listener)
    }

    override fun getItemCount() = tickets.size

    fun update(newItems: List<AdminTicket>) {
        val diffResult = DiffUtil.calculateDiff(TicketsDiffUtil(this.tickets, newItems))
        tickets.clear()
        tickets.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ticket: AdminTicket,
                 ticketType: TicketType, listener: OnTicketSelectedListener) = with(itemView) {
            nameTextView.text = ticket.name
            lrzIdTextView.text = ticket.lrzId
            ticketTypeTextView.text = ticketType.description

            val purchaseColor = if (ticket.isPurchased) {
                R.color.text_dark_gray
            } else {
                R.color.grade_4_7
            }
            purchaseDateTextView.textColor = ContextCompat.getColor(context, purchaseColor)

            ticket.purchaseDate?.let {
                val formattedDate = Event.getFormattedDateTime(context, it)
                purchaseDateTextView.text = context.getString(R.string.purchased_format_string, formattedDate)
            }

            val redeemColor = if (ticket.isRedeemed) {
                R.color.text_dark_gray
            } else {
                R.color.grade_3_3
            }
            purchaseDateTextView.textColor = ContextCompat.getColor(context, redeemColor)

            ticket.redeemDate?.let {
                val formattedDate = Event.getFormattedDateTime(context, it)
                redemptionDateTextView.text = context.getString(R.string.redeemed_format_string, formattedDate)
            }

            setOnClickListener { listener.onTicketSelected(ticket, adapterPosition) }
        }

    }

    interface OnTicketSelectedListener {
        fun onTicketSelected(ticket: AdminTicket, position: Int)
    }

}