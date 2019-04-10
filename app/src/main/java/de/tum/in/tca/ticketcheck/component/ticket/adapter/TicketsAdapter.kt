package de.tum.`in`.tca.ticketcheck.component.ticket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.R.id.*
import de.tum.`in`.tca.ticketcheck.R.string.ticket
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Customer
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType
import kotlinx.android.synthetic.main.fragment_ticket_details.view.*
import kotlinx.android.synthetic.main.ticket_list_item.view.*
import org.jetbrains.anko.textColor

class TicketsAdapter(
        context: Context
) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    private val listener = context as OnTicketSelectedListener
    private val ticketsController: TicketsController by lazy { TicketsController(context) }

    private val allCustomers = mutableListOf<Customer>()
    private var eventId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ticket_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = allCustomers[position]
        holder.bind(customer, eventId, ticketsController, listener)
    }

    override fun getItemCount() = allCustomers.size

    fun update(newItems: List<Customer>, eventId: Int) {
        this.eventId = eventId
        val diffResult = DiffUtil.calculateDiff(TicketsDiffUtil(this.allCustomers, newItems))
        allCustomers.clear()
        allCustomers.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(customer: Customer, eventId: Int, ticketsController: TicketsController, listener: OnTicketSelectedListener) = with(itemView) {
            customerNameTextView.text = customer.name
            customerLrzIdTextView.text = customer.lrzId

            val tickets = ticketsController.getByEventAndCustomer(eventId, customer.lrzId)
            val ticketIds = tickets.map { it.id }
            val ticketTypes = ticketsController.getTicketTypesByTicketIds(ticketIds)

            tickets[0].purchaseDate?.let {
                val formattedDate = Event.getFormattedDateTime(context, it)
                ticketPurchaseDate.text = context.getString(R.string.purchased_format_string, formattedDate)
            }

            val allRedeemed = customer.nrOfTickets == customer.nrOfTicketsRedeemed
            val redeemColor = if (allRedeemed) {
                R.color.text_dark_gray
            } else {
                R.color.grade_3_3
            }
            redemptionDateTextView.textColor = ContextCompat.getColor(context, redeemColor)

            if (allRedeemed) {
                tickets[0].redeemDate?.let {
                    val formattedDate = Event.getFormattedDateTime(context, it)
                    redemptionDateTextView.text = context.getString(R.string.redeemed_format_string, formattedDate)
                }
            } else {
                redemptionDateTextView.text = context.getString(R.string.not_redeemed)
            }

            ticketTypesRecyclerView.layoutManager = LinearLayoutManager(context)
            ticketTypesRecyclerView.adapter = TicketTypeAdapter(ticketTypes)
            ticketTypesRecyclerView.setHasFixedSize(true)
            ticketTypesRecyclerView.isNestedScrollingEnabled = false
            clickSurface.setOnClickListener { listener.onTicketSelected(ticketIds, adapterPosition) }
        }

    }

    interface OnTicketSelectedListener {
        fun onTicketSelected(ticketIds: List<Int>, position: Int)
    }

}