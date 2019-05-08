package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_main.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.viewmodel.MainViewModel
import androidx.appcompat.app.AlertDialog

class MainFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.loadingIndicator.hide()

        activity?.let { mViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java) }

        view.webView.settings.javaScriptEnabled = true

        if (savedInstanceState != null) view.webView.restoreState(savedInstanceState)
        else checkNetworkConnection()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.webView?.saveState(outState)
    }

    fun onBackPressed() {
        if (view!!.webView.canGoBack()) view!!.webView.goBack()
        else activity!!.finish()
    }

    private fun checkNetworkConnection() {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo

        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected)) showNoInternetDialog()
        else mViewModel.loadMainSite(view!!, (activity as MainActivity), this@MainFragment::showNoInternetDialog)
    }

    private fun showNoInternetDialog() =
        AlertDialog.Builder(context!!)
            .setTitle(R.string.no_internet_title)
            .setMessage(R.string.no_internet_reconnect_message)
            .setCancelable(false)
            .setPositiveButton(R.string.try_again) { dialog, _ ->
                dialog.dismiss()
                checkNetworkConnection()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                activity!!.finish()
            }
            .create()
            .show()

    private fun showPrivacyPolicy() =
        AlertDialog.Builder(context!!)
            .setTitle(R.string.privacy_policy)
            .setMessage("Zgodnie z art. 8 ust. 1 Dekretu KEP z 13.03.2018 oraz Rozporządzeniem Parlamentu Europejskiego i Rady (UE) 2016/679 z dnia 27 kwietnia 2016 r. w sprawie ochrony osób fizycznych w związku z przetwarzaniem danych osobowych i w sprawie swobodnego przepływu takich danych oraz uchylenia dyrektywy 95/46/WE (ogólne rozporządzenie o ochronie danych) [RODO] informuję, że: \n\n" +
                    "1. Administratorem Pani/Pana danych osobowych jest Zakon Braci Mniejszych Kapucynów – Prowincja Krakowska, ul.Grzegorza Korzeniaka 16, 30-298 Kraków.\n" +
                    "2. Administrator danych posiada Inspektora Ochrony Danych (ul. Grzegorza Korzeniaka 16, 30-298 Kraków, mail: piod@kapucyni.pl tel.: 12 62 383 79).\n" +
                    "3. Pani/Pana dane osobowe przetwarzane będą w celu świadczenia usług elektronicznych w formie zarządzania kontem internetowym, zapisów i informowania o wydarzeniach i akcjach prowadzonych przez Zakon Braci Mniejszych Kapucynów – Prowincja Krakowska.\n" +
                    "4. Pani/Pana dane osobowe nie będą przekazywane do jednostki kościelnej poza terytorium Rzeczpospolitej Polskiej.\n" +
                    "5. Podstawą przetwarzania tych danych jest Pani/Pana zgoda.\n" +
                    "6. Pani/Pana dane osobowe będą przechowywane przez okres przekraczający 12 miesięcy.\n" +
                    "7. Posiada Pani/Pan prawo dostępu do treści swoich danych oraz prawo ich sprostowania, usunięcia lub ograniczenia przetwarzania.\n" +
                    "8. Państwa zgoda może zostać cofnięta w dowolnym momencie przez wysłanie wiadomości e-mail na adres piod@kapucyni.pl lub dzwoniąc pod numer telefonu 12 62 383 79.\n" +
                    "9. Ma Pani/Pan prawo wniesienia skargi do Kościelnego Inspektora Ochrony jeśli uzna Pani/Pan, iż przetwarzanie danych osobowych pani/pana dotyczących narusza przepisy dekretu (Skwer kard. Stefana Wyszyńskiego 6, 01-015 Warszawa, kiod@episkopat.pl).\n")
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
}
