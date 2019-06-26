package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sheet_fragment_song.view.*

import pl.kapucyni.wolczyn.app.R

class SongFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.sheet_fragment_song, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.hideBottomSheet.setOnClickListener { (parentFragment as SongBookFragment).hideBottomSheet() }
    }
}
