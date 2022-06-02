package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.kapucyni.wolczyn.app.databinding.SheetFragmentSongBinding

class SongFragment : BindingFragment<SheetFragmentSongBinding>() {

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        SheetFragmentSongBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        binding.hideBottomSheet.setOnClickListener { (parentFragment as SongBookFragment).hideBottomSheet() }
    }

    fun setSongViews(title: String, text: String) {
        binding.songName.text = title
        binding.songText.text = text
    }
}
