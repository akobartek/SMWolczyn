package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_viewpager.view.*

import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialog
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ViewPagerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel
import java.util.*

class ViewPagerFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mAdapter: ViewPagerAdapter
    private lateinit var mFragmentType: String
    private lateinit var mTabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_viewpager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentType = arguments!!.getString("fragmentType", "")
        mTabLayout = view.tabLayout
        activity?.let {
            mViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
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
        (activity as MainActivity).removeViewFromAppBar(mTabLayout)
        super.onStop()
    }

    fun setupViewPager() {
        mAdapter = ViewPagerAdapter(
            childFragmentManager,
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
        view?.viewPager?.adapter = mAdapter
        view?.viewPager?.currentItem = when (mFragmentType) {
            "guests" -> 0
            "breviary" -> {
                val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
                if (hour < 17) 0 else if (hour < 21) 1 else 2
            }
            else -> 0
        }
        view?.viewPager?.offscreenPageLimit = when (mFragmentType) {
            "guests" -> 2
            "breviary" -> 3
            else -> 2
        }
        mTabLayout.setupWithViewPager(view?.viewPager)
        view!!.viewPager.removeView(mTabLayout)
        (activity!! as MainActivity).addViewToAppBar(mTabLayout)
    }

    private fun loadBreviary() {
        if (!activity!!.checkNetworkConnection()) {
            activity!!.showNoInternetDialog { loadBreviary() }
            return
        }
        val loadingDialog = AlertDialog.Builder(activity!!)
            .setView(R.layout.dialog_loading)
            .setOnCancelListener { (activity as MainActivity).goBackToSchedule() }
            .create()
        loadingDialog.show()
        mViewModel.loadBreviaryHtml(
            loadingDialog,
            this@ViewPagerFragment,
            activity!!
        )
    }

    fun onBackPressed(): Boolean =
        (childFragmentManager.fragments[view!!.viewPager.currentItem] as GuestListFragment).onBackPressed()

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
