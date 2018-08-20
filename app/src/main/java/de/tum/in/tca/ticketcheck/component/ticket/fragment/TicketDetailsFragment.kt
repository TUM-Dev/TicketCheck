package de.tum.`in`.tca.ticketcheck.component.ticket.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse
import de.tum.`in`.tca.ticketcheck.database.TcaDb
import de.tum.`in`.tca.ticketcheck.utils.Const
import de.tum.`in`.tca.ticketcheck.utils.Utils
import kotlinx.android.synthetic.main.fragment_ticket_details.*
import kotlinx.android.synthetic.main.fragment_ticket_details.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var ticket: AdminTicket
    private lateinit var ticketsController: TicketsController

    private var listener: InteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { ticketsController = TicketsController(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  args ->
            ticket = args.getParcelable(Const.TICKET)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            ticket_name.text = ticket.name
            ticket_lrzid.text = ticket.lrzId
            ticket_no.text = ticket.id.toString()

            val purchaseDate = ticket.purchaseDate
            val purchaseText = if (purchaseDate != null) {
                Event.getFormattedDateTime(context, purchaseDate)
            } else {
                context.getString(R.string.none)
            }
            ticket_purchase.text = purchaseText

            check_in_button.setOnClickListener { openCheckInConfirmationDialog() }
            updateCheckInButton(ticket.isRedeemed)
        }
    }

    private fun openCheckInConfirmationDialog() {
        AlertDialog.Builder(context)
                .setMessage(getString(R.string.check_in_name, ticket.name))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.check_in) { _, _ -> redeemTicket() }
                .show()
    }

    private fun redeemTicket() {
        val cb = object : Callback<TicketSuccessResponse> {
            override fun onResponse(call: Call<TicketSuccessResponse>,
                                    response: Response<TicketSuccessResponse>) {
                val ticketSuccessResponse = response.body() ?: return
                if (ticketSuccessResponse.success) {
                    handleCheckInSuccess()
                } else {
                    Utils.showToast(this@TicketDetailsFragment.context,
                            R.string.check_in_failed)
                }
            }

            override fun onFailure(call: Call<TicketSuccessResponse>, t: Throwable) {
                Utils.showToast(this@TicketDetailsFragment.context,
                        getString(R.string.error_something_wrong))
            }
        }

        ticketsController.redeemTicket(ticket.id, cb)
    }

    private fun handleCheckInSuccess() {
        updateCheckInButton(true)
        TcaDb.getInstance(context).adminTicketDao().setTicketRedeemed(ticket.id)
    }

    private fun updateCheckInButton(isCheckedIn: Boolean) {
        check_in_button.isEnabled = !isCheckedIn
        check_in_button.setText(if (isCheckedIn) R.string.checked_in else R.string.check_in)

        val context = context ?: return
        if (isCheckedIn) {
            val confirmedColor = ContextCompat.getColor(context, R.color.sections_green)
            check_in_button.backgroundTintList = ColorStateList.valueOf(confirmedColor)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        listener?.onTicketDetailsClosed()
    }

    interface InteractionListener {
        fun onTicketDetailsClosed()
    }

    companion object {

        @JvmStatic
        fun newInstance(ticket: AdminTicket,
                        listener: InteractionListener? = null): TicketDetailsFragment {
            return TicketDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Const.TICKET, ticket)
                }
                this.listener = listener
            }
        }

    }

}
