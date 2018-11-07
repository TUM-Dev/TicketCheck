package de.tum.`in`.tca.ticketcheck.component.ticket.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
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

class TicketScanActivity : AppCompatActivity(),
        ZXingScannerView.ResultHandler, TicketDetailsFragment.InteractionListener {

    private lateinit var ticketsController: TicketsController
    private var eventId: Int = 0

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_ticket_scan)

        ticketsController = TicketsController(this)
        eventId = intent.getIntExtra(Const.EVENT_ID, -1)

        val formats = listOf(BarcodeFormat.QR_CODE)
        scanner_view.setFormats(formats)
        scanner_view.setResultHandler(this)
    }

    public override fun onResume() {
        super.onResume()

        doIfHasPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA) {
            scanner_view.startCamera()
        }
    }

    override fun handleResult(rawResult: Result) {
        val cb = object : Callback<TicketValidityResponse> {
            override fun onResponse(call: Call<TicketValidityResponse>,
                                    response: Response<TicketValidityResponse>) {
                val ticketValidityResponse = response.body()
                if (ticketValidityResponse  != null
                        && ticketValidityResponse.valid && response.isSuccessful) {
                    openTicketDetails(ticketValidityResponse)
                } else {
                    showErrorDialog(R.string.not_valid)
                }
            }

            override fun onFailure(call: Call<TicketValidityResponse>, t: Throwable) {
                Utils.log(t)
                showErrorDialog(R.string.error_something_wrong)
            }
        }

        ticketsController.checkTicketValidity(eventId, rawResult.text, cb)
    }

    private fun showErrorDialog(messageResId: Int) {
        AlertDialog.Builder(this)
                .setMessage(messageResId)
                .setPositiveButton(R.string.ok) { _, _ ->
                    scanner_view.resumeCameraPreview(this)
                }
                .show()
    }

    private fun openTicketDetails(ticketValidityResponse: TicketValidityResponse) {
        val ticket = ticketsController.getTicketById(ticketValidityResponse.ticketHistory)

        TicketDetailsFragment
                .newInstance(ticket, this)
                .show(supportFragmentManager, "ticket_details_fragment")
    }

    override fun onTicketDetailsClosed() {
        scanner_view.resumeCameraPreview(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_CAMERA -> handlePermissionResult(grantResults)
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun handlePermissionResult(grantResults: IntArray) {
        if (grantResults.isNotEmpty() || grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            scanner_view.startCamera()
        }
    }

    override fun onStop() {
        super.onStop()
        scanner_view.stopCamera()
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 123
    }

}

fun Activity.doIfHasPermission(permission: String, requestCode: Int, block: () -> Unit) {
    val result = ContextCompat.checkSelfPermission(this, permission)
    if (result == PackageManager.PERMISSION_GRANTED) {
        block()
    } else {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}
