package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.kapucyni.wolczyn.app.databinding.FragmentBreviaryBinding
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class BreviaryFragment : Fragment() {

    private var _binding: FragmentBreviaryBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreviaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().let {
            mViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            loadBreviary()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadBreviary() {
        mViewModel.getBreviaryHtml(requireArguments().getInt("breviaryType"))?.let { text ->
            with(binding.breviaryText) {
                loadDataWithBaseURL(null, text, "text/html", "UTF-8", null)
                visibility = View.VISIBLE
                scrollTo(0, 0)
                animate().alpha(1f).duration = 444L
            }
        }
    }

    companion object {
        fun newInstance(breviaryType: Int): BreviaryFragment {
            return BreviaryFragment().apply {
                arguments = Bundle().apply {
                    putInt("breviaryType", breviaryType)
                }
            }
        }
    }
}
