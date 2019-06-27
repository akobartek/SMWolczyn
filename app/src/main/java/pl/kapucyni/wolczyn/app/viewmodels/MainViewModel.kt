package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.fragment_signings.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import pl.kapucyni.wolczyn.app.apicalls.ApiFactory
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.model.User
import pl.kapucyni.wolczyn.app.model.WeatherRecord
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialog
import pl.kapucyni.wolczyn.app.view.fragments.ViewPagerFragment
import kotlin.coroutines.CoroutineContext

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    var weatherList: List<WeatherRecord>? = null

    // region User
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: KapucyniApiRepository = KapucyniApiRepository(ApiFactory.authorizedKapucyniApi)

    val userLiveData = MutableLiveData<User>()

    init {
        userLiveData.postValue(null)
    }

    fun fetchUser() {
        scope.launch {
            userLiveData.postValue(repository.getUserInfo())
        }
    }
    // endregion User

    // region Signings
    fun loadMainSite(view: View, activity: Activity) {
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
                activity.showNoInternetDialog { loadMainSite(view, activity) }
            }
        }).start()
    }
    // endregion Signings

    // region Breviary
    private var breviaryHtml: Array<String?> = arrayOf(null, null, null)

    fun wasBreviaryLoaded(): Boolean = !breviaryHtml.contains(null)

    fun loadBreviaryHtml(loadingDialog: AlertDialog, viewPagerFragment: ViewPagerFragment, activity: Activity) {
        Thread(Runnable {
            try {
                if (!wasBreviaryLoaded()) {
                    val document = Jsoup.connect("http://skrzynkaintencji.pl/brewiarz/").timeout(30000).get()
                    breviaryHtml[0] = document.select("h2").first { it.outerHtml().contains("<a name=\"jutrznia\">") }
                        .nextElementSibling()
                        .outerHtml()
                    breviaryHtml[1] = document.select("h2").first { it.outerHtml().contains("<a name=\"nieszpory\">") }
                        .nextElementSibling()
                        .outerHtml()
                    breviaryHtml[2] = document.select("h2").first { it.outerHtml().contains("<a name=\"kompleta\">") }
                        .nextElementSibling()
                        .outerHtml()
                    updateBreviaryHtml()
                }

                activity.runOnUiThread {
                    loadingDialog.dismiss()
                    viewPagerFragment.setupViewPager()
                }
            } catch (exc: Exception) {
                Log.e("loadBreviary", exc.toString())
                activity.runOnUiThread {
                    loadingDialog.dismiss()
                    activity.showNoInternetDialog { loadBreviaryHtml(loadingDialog, viewPagerFragment, activity) }
                }
            }
        }).start()
    }

    fun getBreviaryHtml(type: Int): String? = checkBreviaryNightMode(type)

    private fun updateBreviaryHtml() {
        breviaryHtml = breviaryHtml.map { it?.replace("color=\"#e88b40\"", "color=\"brown\"") }.toTypedArray()
        breviaryHtml = breviaryHtml.map { it?.replace("color=\"#1982d1\"", "color=\"black\"") }.toTypedArray()
    }

    private fun checkBreviaryNightMode(type: Int): String? {
        return if (PreferencesManager.getNightMode()) {
            val result = "<html><head>" +
                    "<style type=\"text/css\">body{color: #fff; background-color: #28292e;}" +
                    "</style></head>" +
                    "<body>" +
                    breviaryHtml[type] +
                    "</body></html>"
            result.replace("black", "white")
        } else {
            breviaryHtml[type]
        }
    }
    // endregion Breviary
}