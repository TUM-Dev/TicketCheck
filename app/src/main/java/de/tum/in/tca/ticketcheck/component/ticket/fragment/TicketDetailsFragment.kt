package de.tum.`in`.tca.ticketcheck.component.ticket.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.adapter.EqualSpacingItemDecoration
import de.tum.`in`.tca.ticketcheck.component.ticket.adapter.TicketTypeAdapter
import de.tum.`in`.tca.ticketcheck.component.ticket.model.AdminTicket
import de.tum.`in`.tca.ticketcheck.component.ticket.model.TicketTypeCount
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketSuccessResponse
import de.tum.`in`.tca.ticketcheck.database.TcaDb
import de.tum.`in`.tca.ticketcheck.utils.Const
import de.tum.`in`.tca.ticketcheck.utils.Utils
import de.tum.`in`.tca.ticketcheck.component.ticket.model.Event
import kotlinx.android.synthetic.main.fragment_ticket_details.*
import kotlinx.android.synthetic.main.fragment_ticket_details.view.*
import kotlinx.android.synthetic.main.ticket_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketDetailsFragment : BottomSheetDialogFragment() {

    private val ticketIds: List<Int> by lazy {
        arguments?.getIntegerArrayList(Const.TICKET)
                ?: throw IllegalStateException("No ticket provided")
    }

    private var ticketTypes: List<TicketTypeCount> = emptyList()
    private var tickets: List<AdminTicket> = emptyList()

    private val ticketsController: TicketsController by lazy {
        TicketsController(requireContext())
    }

    private var listener: InteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketTypes = ticketsController.getTicketTypesByTicketIds(ticketIds)
        tickets = ticketsController.getTicketsById(ticketIds)

        with(view) {
            nameTextView.text = tickets[0].name
            lrzIdTextView.text = tickets[0].lrzId

            val purchaseDate = tickets[0].purchaseDate
            val purchaseText = if (purchaseDate != null) {
                val formattedDate = Event.getFormattedDateTime(context, purchaseDate)
                context.getString(R.string.redeemed_format_string, formattedDate)
            } else {
                context.getString(R.string.none)
            }
            purchaseDateTextView.text = purchaseText

            checkInButton.setOnClickListener { openCheckInConfirmationDialog() }
            cancelButton.setOnClickListener { dismiss() }

            var allRedeemed = true
            tickets.forEach { if (it.isRedeemed.not()) allRedeemed = false }
            updateCheckInButton(allRedeemed)

            purchaseInfoContainer.layoutManager = LinearLayoutManager(context)
            purchaseInfoContainer.setHasFixedSize(true)
            purchaseInfoContainer.isNestedScrollingEnabled = false
            purchaseInfoContainer.adapter = TicketTypeAdapter(ticketTypes)
            val spacing = Math.round(resources.getDimension(R.dimen.material_card_view_padding))
            purchaseInfoContainer.addItemDecoration(EqualSpacingItemDecoration(spacing))
        }
    }

    private fun openCheckInConfirmationDialog() {
        AlertDialog.Builder(context)
                .setMessage(getString(R.string.check_in_name, tickets[0].name))
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

        ticketsController.redeemTickets(ticketIds, cb)
    }

    private fun handleCheckInSuccess() {
        TcaDb.getInstance(context).adminTicketDao().setTicketsRedeemed(ticketIds)
        Utils.showToast(context, getString(R.string.checked_in_format_string, tickets[0].name))
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onTicketDetailsClosed()
    }

    interface InteractionListener {
        fun onTicketDetailsClosed()
    }

    companion object {

        @JvmStatic
        fun newInstance(ticketIds: List<Int>,
                        listener: InteractionListener? = null): TicketDetailsFragment {
            val idList = ArrayList<Int>()
            idList.addAll(ticketIds)

            return TicketDetailsFragment().apply {
                arguments = Bundle().apply {
                    putIntegerArrayList(Const.TICKET, idList)
                }
                this.listener = listener
            }
        }

    }

}
