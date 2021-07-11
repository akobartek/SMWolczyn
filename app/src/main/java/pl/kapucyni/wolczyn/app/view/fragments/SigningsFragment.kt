package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.kapucyni.wolczyn.app.databinding.FragmentSigningsBinding
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class SigningsFragment : Fragment() {

    private var _binding: FragmentSigningsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigningsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingIndicator.hide()
        requireActivity().let { mViewModel = ViewModelProvider(it).get(MainViewModel::class.java) }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
