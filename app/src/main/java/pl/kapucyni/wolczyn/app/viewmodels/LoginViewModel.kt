package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.apicalls.ApiFactory
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.model.User
import kotlin.coroutines.CoroutineContext

class LoginViewModel(val app: Application) : AndroidViewModel(app) {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: KapucyniApiRepository = KapucyniApiRepository(ApiFactory.kapucyniApi)
    private val authorizedRepository: KapucyniApiRepository = KapucyniApiRepository(ApiFactory.authorizedKapucyniApi)

    val tokenLiveData = MutableLiveData<String>()
    val userLiveData = MutableLiveData<User>()

    fun signInWithEmail(login: String, password: String) {
        scope.launch {
            tokenLiveData.postValue(repository.loginToSystemWithEmail(login, password))
        }
    }

    fun signInWithSocial(email: String, identifier: String, media: String) {
        scope.launch {
            tokenLiveData.postValue(repository.loginToSystemWithSocial(email, identifier, media))
        }
    }

    fun fetchUser() {
        scope.launch {
            userLiveData.postValue(authorizedRepository.getUserInfo())
        }
    }

//    fun cancelAllRequests() = coroutineContext.cancel()
}