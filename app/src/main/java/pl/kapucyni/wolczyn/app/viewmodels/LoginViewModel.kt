package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.model.User
import pl.kapucyni.wolczyn.app.utils.saveTokenAndReturnBody
import pl.kapucyni.wolczyn.app.view.activities.LoginActivity
import kotlin.coroutines.CoroutineContext

class LoginViewModel(val app: Application) : AndroidViewModel(app) {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: KapucyniApiRepository = KapucyniApiRepository(RetrofitClient.kapucyniApi)
    private val authorizedRepository: KapucyniApiRepository =
        KapucyniApiRepository(RetrofitClient.authorizedKapucyniApi)

    val bearerToken = MutableLiveData<String>()
    val loggedUser = MutableLiveData<User>()

    fun signInWithEmail(login: String, password: String, activity: LoginActivity) {
        scope.launch {
            try {
                bearerToken.postValue(repository.loginToSystemWithEmail(login, password))
            } catch (exc: IllegalStateException) {
                activity.runOnUiThread { activity.showAccountNotFoundDialog() }
            }
        }
    }

    fun signInWithSocial(email: String, identifier: String, media: String, activity: LoginActivity) {
        scope.launch {
            try {
                bearerToken.postValue(repository.loginToSystemWithSocial(email, identifier, media))
            } catch (exc: IllegalStateException) {
                activity.runOnUiThread { activity.showAccountNotFoundDialog() }
            }
        }
    }

    fun fetchUser() {
        scope.launch {
            loggedUser.postValue(authorizedRepository.getUserInfo().saveTokenAndReturnBody())
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}