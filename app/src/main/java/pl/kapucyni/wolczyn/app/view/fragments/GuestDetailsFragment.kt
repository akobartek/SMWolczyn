package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_links_bar.view.*
import kotlinx.android.synthetic.main.sheet_fragment_guest_details.view.*

import pl.kapucyni.wolczyn.app.R

class GuestDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.sheet_fragment_guest_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.hideBottomSheet.setOnClickListener { (parentFragment as GuestListFragment).hideBottomSheet() }
        view.facebookImage.setOnClickListener { (parentFragment as GuestListFragment).onIconClick(0) }
        view.instagramImage.setOnClickListener { (parentFragment as GuestListFragment).onIconClick(1) }
        view.youtubeImage.setOnClickListener { (parentFragment as GuestListFragment).onIconClick(2) }
    }
}
