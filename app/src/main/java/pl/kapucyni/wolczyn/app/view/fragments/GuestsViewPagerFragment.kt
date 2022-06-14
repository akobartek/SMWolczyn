package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentGuestsViewPagerBinding
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ViewPagerAdapter

class GuestsViewPagerFragment : BindingFragment<FragmentGuestsViewPagerBinding>() {

    private lateinit var mAdapter: ViewPagerAdapter

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGuestsViewPagerBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        setupViewPager()
    }

    override fun onStop() {
        (requireActivity() as MainActivity).removeViewFromToolbar(binding.tabLayout)
        super.onStop()
    }

    private fun setupViewPager() {
        if (!isAdded) return
        mAdapter = ViewPagerAdapter(
            this@GuestsViewPagerFragment,
            arrayOf(getString(R.string.conferences), getString(R.string.concerts))
        )
        binding.viewPager.apply {
            adapter = mAdapter
            currentItem = 0
            offscreenPageLimit = 2
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = mAdapter.getTabTitle(position)
        }.attach()
        binding.root.removeView(binding.tabLayout)
        (activity as MainActivity).addViewToToolbar(binding.tabLayout)
    }

    fun onBackPressed(): Boolean =
        (childFragmentManager.fragments[binding.viewPager.currentItem] as GuestListFragment).onBackPressed()
}
