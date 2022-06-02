package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.SheetFragmentDepartureDetailsBinding
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService

class DepartureDetailsFragment : BindingFragment<SheetFragmentDepartureDetailsBinding>() {

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        SheetFragmentDepartureDetailsBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        binding.hideBottomSheet.setOnClickListener { (parentFragment as DepartureListFragment).hideBottomSheet() }
        binding.departurePhone.setOnClickListener { makeCall(binding.departurePhone.text.toString()) }
        binding.departureEmail.setOnClickListener { sendMail(binding.departureEmail.text.toString()) }
        binding.departureSigningUrl.setOnClickListener {
            requireContext().openWebsiteInCustomTabsService(binding.departureSigningUrl.text.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    fun setViewsValues(departure: Departure, imageResource: Int) {
        departure.let {
            binding.departureTransportType.setImageResource(imageResource)
            binding.departureCity.text = it.city
            binding.departurePatron.text = getString(R.string.organizer, it.patron)
            it.direction?.let { direction ->
                if (direction.isNotEmpty()) {
                    binding.departureDirection.text = "($direction)"
                    binding.departureDirection.visibility = View.VISIBLE
                }
            }
            it.notes?.let { notes ->
                if (notes.isNotEmpty()) {
                    binding.departureNotes.text = notes
                    binding.departureNotes.visibility = View.VISIBLE
                }
            }
            it.contact_phone?.let { phone ->
                if (phone.isNotEmpty()) {
                    binding.departurePhone.text = phone
                    binding.departurePhone.visibility = View.VISIBLE
                }
            }
            it.contact_email?.let { email ->
                if (email.isNotEmpty()) {
                    binding.departureEmail.text = email
                    binding.departureEmail.visibility = View.VISIBLE
                }
            }
            it.signin_url?.let { url ->
                if (url.isNotEmpty()) {
                    binding.departureSigningUrl.text = url
                    binding.departureSigningUrl.visibility = View.VISIBLE
                }
            }
        }
    }

    fun hideViews() {
        binding.departureTransportType.setImageResource(android.R.color.transparent)
        binding.departureCity.text = ""
        binding.departurePatron.text = ""
        binding.departureDirection.visibility = View.GONE
        binding.departureNotes.visibility = View.GONE
        binding.departurePhone.visibility = View.GONE
        binding.departureEmail.visibility = View.GONE
        binding.departureSigningUrl.visibility = View.GONE
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendMail(emailAddress: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.setDataAndType(Uri.parse("mailto:"), "message/rfc822")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wyjazd na Wołczyn")

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail)))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, getString(R.string.send_mail_error), Toast.LENGTH_SHORT).show()
        }
    }
}
