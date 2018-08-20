package de.tum.`in`.tca.ticketcheck.component.ticket.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.common.collect.ImmutableList
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.tbruyelle.rxpermissions2.RxPermissions
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.ticket.TicketsController
import de.tum.`in`.tca.ticketcheck.component.ticket.fragment.TicketDetailsFragment
import de.tum.`in`.tca.ticketcheck.component.ticket.payload.TicketValidityResponse
import de.tum.`in`.tca.ticketcheck.utils.Const
import de.tum.`in`.tca.ticketcheck.utils.Utils
import kotlinx.android.synthetic.main.activity_ticket_scan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var ticketsController: TicketsController
    private var eventId: Int = 0

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_ticket_scan)

        ticketsController = TicketsController(this)
        eventId = intent.getIntExtra(Const.EVENT_ID, -1)

        val formats = ImmutableList.of(BarcodeFormat.QR_CODE)
        scanner_view.setFormats(formats)
    }

    @SuppressLint("CheckResult")
    override fun onStart() {
        super.onStart()
        RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (!granted) {
                        finish()
                    }
                }
    }

    public override fun onResume() {
        super.onResume()
        scanner_view.setResultHandler(this)
        scanner_view.startCamera()
    }

    override fun handleResult(rawResult: Result) {
        val cb = object : Callback<TicketValidityResponse> {
            override fun onResponse(call: Call<TicketValidityResponse>,
                                    response: Response<TicketValidityResponse>) {
                val ticketValidityResponse = response.body() ?: return
                if (ticketValidityResponse.valid) {
                    openTicketDetails(ticketValidityResponse)
                } else {
                    Utils.showToast(this@TicketScanActivity, R.string.not_valid)
                }
            }

            override fun onFailure(call: Call<TicketValidityResponse>, t: Throwable) {
                Utils.log(t)
                Utils.showToast(this@TicketScanActivity, R.string.error_something_wrong)
            }
        }

        ticketsController.checkTicketValidity(eventId, rawResult.text, cb)
    }

    private fun openTicketDetails(ticketValidityResponse: TicketValidityResponse) {
        val ticket = ticketsController.getTicketById(ticketValidityResponse.ticketHistory)

        TicketDetailsFragment
                .newInstance(ticket)
                .show(supportFragmentManager, "ticket_details_fragment")
    }

    public override fun onStop() {
        super.onStop()
        scanner_view.stopCamera()
    }

}
