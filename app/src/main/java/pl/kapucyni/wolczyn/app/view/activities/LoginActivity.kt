package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogWithTryAgain
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_GOOGLE_SIGN_IN = 2137
    }

    private lateinit var mLoginViewModel: LoginViewModel
    private lateinit var mLoadingDialog: AlertDialog
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var isTokenLoaded = false
    private val mFacebookCallbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(loginToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        if (!PreferencesManager.getNightMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.WHITE
            }
            googleSignInBtn.setColorScheme(SignInButton.COLOR_DARK)
        }

        configureGoogleSignIn()
        configureFacebookSignIn()

        mLoginViewModel = ViewModelProviders.of(this@LoginActivity).get(LoginViewModel::class.java)
        setOnClickListeners()

        mLoginViewModel.bearerToken.observe(this@LoginActivity, Observer { token ->
            if (token != null) {
                PreferencesManager.setBearerToken(token)
                isTokenLoaded = true
                tryToRunFunctionOnInternet { mLoginViewModel.fetchUser() }
            } else {
                showAccountNotFoundDialog()
            }
        })

        mLoginViewModel.loggedUser.observe(this@LoginActivity, Observer { user ->
            if (isTokenLoaded) {
                if (user != null) {
                    if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                    Toast.makeText(this@LoginActivity, R.string.signed_in, Toast.LENGTH_SHORT).show()
                    returnActivity(true)
                } else {
                    PreferencesManager.setBearerToken("")
                    showAccountNotFoundDialog()
                }
            }
        })

        mLoadingDialog = AlertDialog.Builder(this@LoginActivity)
            .setView(R.layout.dialog_loading)
            .setCancelable(false)
            .create()

//        generateKeyHash()
    }

    override fun onBackPressed() {
        returnActivity(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                mLoadingDialog.show()
                val account = result.signInAccount!!
                tryToRunFunctionOnInternet { mLoginViewModel.signInWithSocial(account.email!!, account.id!!, "google") }
                if (mGoogleApiClient.isConnected) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                    mGoogleApiClient.disconnect()
                    mGoogleApiClient.connect()
                }
            } else {
                showAccountNotFoundDialog()
            }
        } else {
            mLoadingDialog.show()
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
        if (mGoogleApiClient.isConnected) mGoogleApiClient.disconnect()
        mLoginViewModel.cancelAllRequests()
        super.onStop()
    }

    private fun returnActivity(logged: Boolean) {
        val returnIntent = Intent()
        returnIntent.putExtra("fragment", intent.getIntExtra("fragment", 0))
        returnIntent.putExtra("loginSuccess", logged)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun setOnClickListeners() {
        emailSignInBtn?.setOnClickListener {
            tryToRunFunctionOnInternet { signInWithLogin() }
        }

        googleSignInBtn?.setOnClickListener {
            tryToRunFunctionOnInternet {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), RC_GOOGLE_SIGN_IN)
            }
        }

        loginLayout.setOnClickListener {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        logo.setOnClickListener {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun signInWithLogin() {
        mLoadingDialog.show()
        val login = loginET.text.toString().trim()
        val password = passwordET.text.toString().trim()
        if (!areLoginAndPasswordValid(login, password)) {
            mLoadingDialog.dismiss()
            return
        }
        try {
            tryToRunFunctionOnInternet { mLoginViewModel.signInWithEmail(login, password) }
        } catch (exc: Exception) {
            showAccountNotFoundDialog()
        }
    }

    private fun configureGoogleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this@LoginActivity)
            .enableAutoManage(this@LoginActivity) {
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                showNoInternetDialogWithTryAgain {
                    if (mGoogleApiClient.isConnected) mGoogleApiClient.disconnect()
                    mGoogleApiClient.connect()
                }
            }
            .addApi(Auth.GOOGLE_SIGN_IN_API, options)
            .build()
        mGoogleApiClient.connect()
    }

    private fun configureFacebookSignIn() {
        facebookSignInBtn?.setPermissions("email")
        facebookSignInBtn?.registerCallback(mFacebookCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) = useFacebookLoginInformation(loginResult.accessToken)

            override fun onCancel() {
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
            }

            override fun onError(exception: FacebookException) = showAccountNotFoundDialog()
        })
    }

    private fun useFacebookLoginInformation(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { obj, _ ->
            tryToRunFunctionOnInternet {
                mLoginViewModel.signInWithSocial(obj.getString("email"), accessToken.userId, "facebook")
            }
            LoginManager.getInstance().logOut()
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun areLoginAndPasswordValid(login: String, password: String): Boolean {
        var isValid = true

        if (login.isEmpty()) {
            loginET.error = getString(R.string.login_error_empty)
            isValid = false
        }
        if (password.isEmpty()) {
            passwordET.error = getString(R.string.password_error_empty)
            isValid = false
        }
        return isValid
    }

    private fun showAccountNotFoundDialog() =
        AlertDialog.Builder(this@LoginActivity)
            .setMessage(R.string.account_not_found_dialog_message)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                dialog.dismiss()
            }
            .create()
            .show()

//    private fun generateKeyHash() {
//        try {
//            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            info.signatures.forEach {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(it.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e1: PackageManager.NameNotFoundException) {
//            Log.e("name not found", e1.toString())
//        } catch (e2: NoSuchAlgorithmException) {
//            Log.e("no such an algorithm", e2.toString())
//        } catch (e3: Exception) {
//            Log.e("exception", e3.toString())
//        }
//    }
}
