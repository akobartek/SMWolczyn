package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_signings.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialog

class SigningsFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_signings, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.loadingIndicator.hide()
        activity?.let { mViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java) }
        view.webView.settings.javaScriptEnabled = true
        when {
            savedInstanceState != null -> view.webView.restoreState(savedInstanceState)
            activity!!.checkNetworkConnection() -> mViewModel.loadMainSite(view, (activity as MainActivity))
            else -> activity!!.showNoInternetDialog { mViewModel.loadMainSite(view, (activity as MainActivity)) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.webView?.saveState(outState)
    }
}
