package de.tum.`in`.tca.ticketcheck.component.ticket.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketType
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

    private val ticket: AdminTicket by lazy {
        arguments?.getParcelable<AdminTicket>(Const.TICKET)
                ?: throw IllegalStateException("No ticket provided")
    }

    private val ticketsController: TicketsController by lazy {
        TicketsController(requireContext())
    }

    private val ticketType: TicketType by lazy {
        ticketsController.getTicketTypeById(ticket.ticketType)
    }

    private var listener: InteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            nameTextView.text = ticket.name
            lrzIdTextView.text = ticket.lrzId
            ticketNumberTextView.text = ticketType.description

            val purchaseDate = ticket.purchaseDate
            val purchaseText = if (purchaseDate != null) {
                Event.getFormattedDateTime(context, purchaseDate)
            } else {
                context.getString(R.string.none)
            }
            purchaseDateTextView.text = purchaseText

            checkInButton.setOnClickListener { openCheckInConfirmationDialog() }
            cancelButton.setOnClickListener { dismiss() }
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
                val ticketSuccessResponse = response.body()
                ticketSuccessResponse?.let {
                    if (response.isSuccessful && it.success) {
                        handleCheckInSuccess()
                        return
                    }
                }

                Utils.showToast(this@TicketDetailsFragment.context, R.string.check_in_failed)
            }

            override fun onFailure(call: Call<TicketSuccessResponse>, t: Throwable) {
                Utils.showToast(this@TicketDetailsFragment.context, R.string.error_something_wrong)
            }
        }

        ticketsController.redeemTicket(ticket.id, cb)
    }

    private fun handleCheckInSuccess() {
        TcaDb.getInstance(context).adminTicketDao().setTicketRedeemed(ticket.id)
        Utils.showToast(context, getString(R.string.checked_in_format_string, ticket.name))
        dismiss()
    }

    private fun updateCheckInButton(isCheckedIn: Boolean) {
        checkInButton.isEnabled = isCheckedIn.not()
        checkInButton.setText(if (isCheckedIn) R.string.checked_in else R.string.check_in)

        if (isCheckedIn) {
            val confirmedColor = ContextCompat.getColor(requireContext(), R.color.error)
            checkInButton.backgroundTintList = ColorStateList.valueOf(confirmedColor)
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
