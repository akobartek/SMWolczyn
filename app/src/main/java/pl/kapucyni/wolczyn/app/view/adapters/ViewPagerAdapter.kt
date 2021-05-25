package pl.kapucyni.wolczyn.app.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.kapucyni.wolczyn.app.view.fragments.BreviaryFragment
import pl.kapucyni.wolczyn.app.view.fragments.GuestListFragment

// TODO() switch ViewPager to ViewPager2
class ViewPagerAdapter(
    supportFragmentManager: FragmentManager,
    private val names: Array<String>,
    private val mFragmentType: String
) : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (mFragmentType) {
        "guests" -> GuestListFragment.newInstance(position)
        "breviary" -> BreviaryFragment.newInstance(position)
        else -> Fragment()
    }

    override fun getPageTitle(position: Int): CharSequence = names[position]

    override fun getCount(): Int = when (mFragmentType) {
        "guests" -> 2
        "breviary" -> 3
        else -> 1
    }
}