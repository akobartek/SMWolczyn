package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Activity
import android.app.Application
import android.view.View
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
import pl.kapucyni.wolczyn.app.utils.saveTokenAndReturnBody
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogWithTryAgain

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    var weatherList: List<WeatherRecord>? = null

    // region User
    private val authorizedRepository: KapucyniApiRepository =
        KapucyniApiRepository(RetrofitClient.authorizedKapucyniApi)

    val currentUser = MutableLiveData<User?>()
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
                jsoup.selectFirst("main")?.let {
                    jsoup.body().selectFirst("#outer-wrap")?.replaceWith(it)
                }

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
}