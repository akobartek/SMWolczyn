package pl.kapucyni.wolczyn.app.viewmodel

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jsoup.Jsoup
import pl.kapucyni.wolczyn.app.view.fragments.MainFragment
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    // region General values
    var isNightMode = false

    fun getDateFormatted(date: Date): String = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
    // endregion General values

    // region Zapisy
    fun loadMainSite(view: View, activity: Activity, showNoInternetDialog: () -> Unit) {
        Thread(Runnable {
            try {
                view.loadingIndicator.show()
                val jsoup = Jsoup.connect("https://wolczyn.kapucyni.pl/zapisy/")
                    .timeout(30000)
                    .get()
                jsoup.body()
                    .selectFirst("#outer-wrap")
                    .replaceWith(jsoup.selectFirst("main"))

                activity.runOnUiThread {
                    view.loadingIndicator.hide()
                    view.webView.loadDataWithBaseURL(null, jsoup.outerHtml(), "text/html", "UTF-8", null)
                    view.webView.visibility = View.VISIBLE
                    view.webView.scrollTo(0, 0)
                    view.webView.animate().alpha(1f).duration = 444L
                }
            } catch (exc: Exception) {
                showNoInternetDialog()
            }
        }).start()
    }
    //endregion Zapisy
}