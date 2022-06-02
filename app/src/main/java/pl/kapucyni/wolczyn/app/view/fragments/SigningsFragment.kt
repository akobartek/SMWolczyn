package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import pl.kapucyni.wolczyn.app.databinding.FragmentSigningsBinding
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class SigningsFragment : BindingFragment<FragmentSigningsBinding>() {

    private val mViewModel: MainViewModel by activityViewModels()

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSigningsBinding.inflate(inflater, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun setup(savedInstanceState: Bundle?) {
        binding.loadingIndicator.hide()
        binding.webView.settings.javaScriptEnabled = true
        if (savedInstanceState != null) binding.webView.restoreState(savedInstanceState)
        else requireActivity().tryToRunFunctionOnInternet {
            mViewModel.loadSignings(binding, (activity as MainActivity))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }
}
