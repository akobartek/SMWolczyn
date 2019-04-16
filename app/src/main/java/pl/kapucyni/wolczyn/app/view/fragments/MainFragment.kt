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
import android.view.MotionEvent
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
        // disable scroll on touch
        view.webView.setOnTouchListener { _, event -> event.action == MotionEvent.ACTION_MOVE }

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
        else mViewModel.loadMainSite(view!!, (activity as MainActivity), this@MainFragment)
    }

    fun showNoInternetDialog() =
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
}
