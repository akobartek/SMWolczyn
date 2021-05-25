package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.kapucyni.wolczyn.app.databinding.SheetFragmentSongBinding

class SongFragment : Fragment() {

    private var _binding: SheetFragmentSongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hideBottomSheet.setOnClickListener { (parentFragment as SongBookFragment).hideBottomSheet() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setSongViews(title: String, text: String) {
        binding.songName.text = title
        binding.songText.text = text
    }
}
