package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_links_bar.view.*
import kotlinx.android.synthetic.main.sheet_fragment_guest_details.view.*
import pl.kapucyni.wolczyn.app.R

class GuestDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.sheet_fragment_guest_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.hideBottomSheet.setOnClickListener {
            if (parentFragment is GuestListFragment) (parentFragment as GuestListFragment).hideBottomSheet()
            else (parentFragment as ScheduleFragment).hideBottomSheet()
        }
        view.facebookImage.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(0)
            else (parentFragment as ScheduleFragment).onIconClick(0)
        }
        view.instagramImage.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(1)
            else (parentFragment as ScheduleFragment).onIconClick(1)
        }
        view.youtubeImage.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(2)
            else (parentFragment as ScheduleFragment).onIconClick(2)
        }
    }
}
