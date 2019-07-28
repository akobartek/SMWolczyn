package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.model.User
import pl.kapucyni.wolczyn.app.utils.saveTokenAndReturnBody
import pl.kapucyni.wolczyn.app.view.activities.LoginActivity

class LoginViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository: KapucyniApiRepository = KapucyniApiRepository(RetrofitClient.kapucyniApi)
    private val authorizedRepository: KapucyniApiRepository =
        KapucyniApiRepository(RetrofitClient.authorizedKapucyniApi)

    val bearerToken = MutableLiveData<String>()
    val loggedUser = MutableLiveData<User>()

    fun signInWithEmail(login: String, password: String, activity: LoginActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bearerToken.postValue(repository.loginToSystemWithEmail(login, password))
            } catch (exc: IllegalStateException) {
                activity.runOnUiThread { activity.showAccountNotFoundDialog() }
            }
        }
    }

    fun signInWithSocial(email: String, identifier: String, media: String, activity: LoginActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bearerToken.postValue(repository.loginToSystemWithSocial(email, identifier, media))
            } catch (exc: IllegalStateException) {
                activity.runOnUiThread { activity.showAccountNotFoundDialog() }
            }
        }
    }

    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
            loggedUser.postValue(authorizedRepository.getUserInfo().saveTokenAndReturnBody())
        }
    }
}