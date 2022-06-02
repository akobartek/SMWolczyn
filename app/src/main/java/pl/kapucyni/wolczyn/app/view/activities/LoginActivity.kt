package pl.kapucyni.wolczyn.app.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.kapucyni.wolczyn.app.BuildConfig
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ActivityLoginBinding
import pl.kapucyni.wolczyn.app.utils.*
import pl.kapucyni.wolczyn.app.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val mLoadingDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_loading)
            .setOnCancelListener { onBackPressed() }
            .create()
    }
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val mLoginViewModel: LoginViewModel by viewModels()
    private var isTokenLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.loginToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = !PreferencesManager.getNightMode()
        wic.isAppearanceLightNavigationBars = !PreferencesManager.getNightMode()
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_theme_background)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.app_theme_background)

        configureGoogleSignIn()
        setOnClickListeners()

        mLoginViewModel.bearerToken.observe(this@LoginActivity) { token ->
            if (mLoadingDialog.isShowing) mLoadingDialog.hide()
            if (token != null) {
                PreferencesManager.setBearerToken(token)
                isTokenLoaded = true
                tryToRunFunctionOnInternet { mLoginViewModel.fetchUser() }
            } else {
                showAccountNotFoundDialog()
            }
        }

        mLoginViewModel.loggedUser.observe(this@LoginActivity) { user ->
            if (isTokenLoaded) {
                if (user != null) {
                    if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                    Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show()
                    returnActivity(true)
                } else {
                    PreferencesManager.setBearerToken("")
                    showAccountNotSignedForEventDialog()
                }
            }
        }

        binding.contentLogin.forgotPasswordTV.text =
            binding.contentLogin.forgotPasswordTV.text.toString().createUnderlinedString()
        binding.contentLogin.forgotPasswordTV.setOnClickListener {
            openWebsiteInCustomTabsService("https://konto.kapucyni.pl/remind")
        }

        binding.contentLogin.createAccountTV?.text =
            binding.contentLogin.createAccountTV?.text.toString().createUnderlinedString()
        binding.contentLogin.createAccountTV?.setOnClickListener {
            openWebsiteInCustomTabsService("https://konto.kapucyni.pl/register")
        }

//        generateKeyHash()
    }

    override fun onBackPressed() {
        returnActivity(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStop() {
        if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
        super.onStop()
    }

    private fun returnActivity(logged: Boolean) {
        val returnIntent = Intent()
        returnIntent.putExtra("fragment", intent.getIntExtra("fragment", 0))
        returnIntent.putExtra("loginSuccess", logged)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private val googleSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account = task.getResult(ApiException::class.java)
                tryToRunFunctionOnInternet {
                    mLoadingDialog.show()
                    mLoginViewModel.signInWithSocial(
                        account.email!!, account.id!!, "google", this@LoginActivity
                    )
                }
                mGoogleSignInClient.signOut()
            } catch (exc: ApiException) {
                if (BuildConfig.DEBUG) Log.e("LoginActivity", "GoogleSignIn: ${exc.statusCode}")
                showAccountNotFoundDialog()
            } catch (exc: Exception) {
                if (BuildConfig.DEBUG) Log.e("LoginActivity", "GoogleSignIn: ${exc.stackTrace}")
                showAccountNotFoundDialog()
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnClickListeners() {
        val hideKeyboard: () -> Boolean = {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        binding.contentLogin.emailSignInBtn.setOnClickListener {
            hideKeyboard()
            tryToRunFunctionOnInternet { signInWithLogin() }
        }
        binding.contentLogin.googleSignInBtn.setOnClickListener {
            hideKeyboard()
            tryToRunFunctionOnInternet { googleSignIn.launch(mGoogleSignInClient.signInIntent) }
        }

        val onTouchListener: (v: View, event: MotionEvent) -> Boolean = { v, _ ->
            v.requestFocus()
            hideKeyboard()
        }
        binding.contentLogin.loginLayout.setOnTouchListener(onTouchListener)
        binding.contentLogin.logo.setOnTouchListener(onTouchListener)
    }

    private fun signInWithLogin() {
        mLoadingDialog.show()
        val login = binding.contentLogin.loginET.text.toString().trim()
        val password = binding.contentLogin.passwordET.text.toString().trim()
        if (!areLoginAndPasswordValid(login, password)) {
            mLoadingDialog.dismiss()
            return
        }
        try {
            tryToRunFunctionOnInternet {
                mLoginViewModel.signInWithEmail(login, password, this@LoginActivity)
            }
        } catch (exc: Throwable) {
            showAccountNotFoundDialog()
        }
    }

    private fun configureGoogleSignIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, options)
    }

    private fun areLoginAndPasswordValid(login: String, password: String): Boolean {
        var isValid = true

        if (login.isEmpty()) {
            binding.contentLogin.loginET.error = getString(R.string.login_error_empty)
            isValid = false
        }
        if (password.isEmpty()) {
            binding.contentLogin.passwordET.error = getString(R.string.password_error_empty)
            isValid = false
        }
        return isValid
    }

    fun showAccountNotFoundDialog() =
        AlertDialog.Builder(this@LoginActivity)
            .setMessage(R.string.account_not_found_dialog_message)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                dialog.dismiss()
            }
            .create()
            .show()

    private fun showAccountNotSignedForEventDialog() =
        AlertDialog.Builder(this@LoginActivity)
            .setMessage(R.string.account_not_signed_for_event_dialog_message)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                dialog.dismiss()
            }
            .setNeutralButton(R.string.menu_signings) { dialog, _ ->
                if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
                dialog.dismiss()
                openWebsiteInCustomTabsService("https://wolczyn.kapucyni.pl/zapisy/")
            }
            .create()
            .show()

//    private fun generateKeyHash() {
//        try {
//            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
//            info.signatures.forEach {
//                val md = MessageDigest.getInstance("SHA")
//                Log.d("SHA:", md.digest().toString())
//                md.update(it.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e1: PackageManager.NameNotFoundException) {
//            Log.e("name not found", e1.toString())
//        } catch (e2: NoSuchAlgorithmException) {
//            Log.e("no such an algorithm", e2.toString())
//        } catch (e3: Throwable) {
//            Log.e("exception", e3.toString())
//        }
//    }
}
