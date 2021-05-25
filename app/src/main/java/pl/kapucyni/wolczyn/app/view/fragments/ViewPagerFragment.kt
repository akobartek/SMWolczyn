package pl.kapucyni.wolczyn.app.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentViewpagerBinding
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogWithTryAgain
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ViewPagerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel
import java.util.*

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewpagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: MainViewModel
    private lateinit var mAdapter: ViewPagerAdapter
    private lateinit var mFragmentType: String
    private lateinit var mChildFragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mChildFragmentManager = childFragmentManager
        _binding = FragmentViewpagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentType = requireArguments().getString("fragmentType", "")
        activity?.let {
            mViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            when (mFragmentType) {
                "breviary" -> when {
                    mViewModel.wasBreviaryLoaded() -> setupViewPager()
                    else -> loadBreviary()
                }
                else -> setupViewPager()
            }
        }
    }

    override fun onStop() {
        when (mFragmentType) {
            "guests" -> (requireActivity() as MainActivity).removeViewFromToolbar(binding.tabLayout)
            "breviary" -> {
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    (activity as MainActivity).removeViewFromAppBar(binding.tabLayout)
                else
                    (activity as MainActivity).removeViewFromToolbar(binding.tabLayout)
            }
        }
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupViewPager() {
        if (!isAdded) return
        mAdapter = ViewPagerAdapter(
            mChildFragmentManager,
            when (mFragmentType) {
                "guests" -> arrayOf(getString(R.string.conferences), getString(R.string.concerts))
                "breviary" -> arrayOf(
                    getString(R.string.morning_prayer),
                    getString(R.string.evening_prayer),
                    getString(R.string.night_prayer)
                )
                else -> arrayOf("")
            },
            mFragmentType
        )
        binding.viewPager.adapter = mAdapter
        binding.viewPager.currentItem = when (mFragmentType) {
            "guests" -> 0
            "breviary" -> {
                val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
                if (hour in 5..16) 0 else if (hour < 22) 1 else 2
            }
            else -> 0
        }
        binding.viewPager.offscreenPageLimit = when (mFragmentType) {
            "guests" -> 2
            "breviary" -> 3
            else -> 2
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.removeView(binding.tabLayout)
        when (mFragmentType) {
            "guests" -> (activity as MainActivity).addViewToToolbar(binding.tabLayout)
            "breviary" -> {
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    (activity as MainActivity).addViewToAppBar(binding.tabLayout)
                else
                    (activity as MainActivity).addViewToToolbar(binding.tabLayout)
            }
        }
    }

    private fun loadBreviary() {
        activity?.let {
            if (!it.checkNetworkConnection()) {
                it.showNoInternetDialogWithTryAgain { loadBreviary() }
                return
            }
            val loadingDialog = AlertDialog.Builder(it)
                .setView(R.layout.dialog_loading)
                .setCancelable(false)
                .create()
            loadingDialog.show()
            mViewModel.loadBreviaryHtml(loadingDialog, this@ViewPagerFragment, it)
        }
    }

    fun onBackPressed(): Boolean =
        (mChildFragmentManager.fragments[binding.viewPager.currentItem] as GuestListFragment).onBackPressed()

    companion object {
        fun newInstance(fragmentType: String): ViewPagerFragment {
            return ViewPagerFragment().apply {
                arguments = Bundle().apply {
                    putString("fragmentType", fragmentType)
                }
            }
        }
    }
}
