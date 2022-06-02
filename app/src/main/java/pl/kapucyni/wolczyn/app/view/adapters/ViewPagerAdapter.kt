package pl.kapucyni.wolczyn.app.view.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import pl.kapucyni.wolczyn.app.view.fragments.GuestListFragment

class ViewPagerAdapter(fragment: Fragment, private val names: Array<String>) :
    FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment =
        GuestListFragment.newInstance(position)

    override fun getItemCount(): Int = 2

    fun getTabTitle(position: Int) = names[position]
}