package pl.kapucyni.wolczyn.app.view.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.android.synthetic.main.sheet_fragment_departure_details.view.*

import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.utils.isChromeCustomTabsSupported

class DepartureDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.sheet_fragment_departure_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.hideBottomSheet.setOnClickListener { (parentFragment as DepartureListFragment).hideBottomSheet() }
        view.departurePhone.setOnClickListener { makeCall(view.departurePhone.text.toString()) }
        view.departureEmail.setOnClickListener { sendMail(view.departureEmail.text.toString()) }
        view.departureSigningUrl.setOnClickListener { openWebsite(view.departureSigningUrl.text.toString()) }
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendMail(emailAddress: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wyjazd na Wołczyn")

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail)))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, getString(R.string.send_mail_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWebsite(url: String) {
        if (view!!.context.isChromeCustomTabsSupported()) {
            CustomTabsIntent.Builder().apply {
                val color = if (PreferencesManager.getNightMode()) Color.parseColor("#28292e") else Color.WHITE
                setToolbarColor(color)
                setSecondaryToolbarColor(color)
            }.build().launchUrl(context, Uri.parse(url))
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}
