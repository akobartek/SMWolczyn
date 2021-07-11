package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.databinding.FragmentSigningsBinding
import pl.kapucyni.wolczyn.app.model.Group
import pl.kapucyni.wolczyn.app.model.User
import pl.kapucyni.wolczyn.app.model.WeatherRecord
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.utils.saveTokenAndReturnBody
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogWithTryAgain
import pl.kapucyni.wolczyn.app.view.fragments.ViewPagerFragment

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    var weatherList: List<WeatherRecord>? = null

    // region User
    private val authorizedRepository: KapucyniApiRepository =
        KapucyniApiRepository(RetrofitClient.authorizedKapucyniApi)

    val currentUser = MutableLiveData<User>()
    val isUserFetched = MutableLiveData<Boolean>()
    val userGroup = MutableLiveData<Group>()

    init {
        currentUser.postValue(null)
        isUserFetched.postValue(false)
    }

    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.postValue(authorizedRepository.getUserInfo().saveTokenAndReturnBody())
            isUserFetched.postValue(true)
        }
    }

    fun fetchGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            userGroup.postValue(authorizedRepository.getGroupInfo().saveTokenAndReturnBody())
        }
    }
    // endregion User

    // region Signings
    fun loadSignings(binding: FragmentSigningsBinding, activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                binding.loadingIndicator.show()
                val jsoup = Jsoup.connect("https://wolczyn.kapucyni.pl/zapisy/")
                    .timeout(30000)
                    .get()
                jsoup.body()
                    .selectFirst("#outer-wrap")
                    .replaceWith(jsoup.selectFirst("main"))

                activity.runOnUiThread {
                    binding.loadingIndicator.hide()
                    binding.webView.loadDataWithBaseURL(
                        null,
                        jsoup.outerHtml(),
                        "text/html",
                        "UTF-8",
                        null
                    )
                    binding.webView.visibility = View.VISIBLE
                    binding.webView.scrollTo(0, 0)
                    binding.webView.animate().alpha(1f).duration = 444L
                }
            } catch (exc: Exception) {
                activity.runOnUiThread {
                    activity.showNoInternetDialogWithTryAgain { loadSignings(binding, activity) }
                }
            }
        }
    }
    // endregion Signings

    // region Breviary
    private var breviaryHtml: Array<String?> = arrayOf(null, null, null)

    fun wasBreviaryLoaded(): Boolean = !breviaryHtml.contains(null)

    fun loadBreviaryHtml(
        loadingDialog: AlertDialog, viewPagerFragment: ViewPagerFragment, activity: Activity
    ) {
        Thread {
            try {
                if (!wasBreviaryLoaded()) {
                    val document =
                        Jsoup.connect("http://skrzynkaintencji.pl/brewiarz/").timeout(30000).get()
                    breviaryHtml[0] = document.select("h2")
                        .first { it.outerHtml().contains("<a name=\"jutrznia\">") }
                        .nextElementSibling()
                        .outerHtml()
                    breviaryHtml[1] = document.select("h2")
                        .first { it.outerHtml().contains("<a name=\"nieszpory\">") }
                        .nextElementSibling()
                        .outerHtml()
                    breviaryHtml[2] = document.select("h2")
                        .first { it.outerHtml().contains("<a name=\"kompleta\">") }
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
                    activity.showNoInternetDialogWithTryAgain {
                        loadBreviaryHtml(loadingDialog, viewPagerFragment, activity)
                    }
                }
            }
        }.start()
    }

    fun getBreviaryHtml(type: Int): String? = checkBreviaryNightMode(type)

    private fun updateBreviaryHtml() {
        breviaryHtml =
            breviaryHtml.map { it?.replace("color=\"#e88b40\"", "color=\"brown\"") }.toTypedArray()
        breviaryHtml =
            breviaryHtml.map { it?.replace("color=\"#1982d1\"", "color=\"black\"") }.toTypedArray()
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