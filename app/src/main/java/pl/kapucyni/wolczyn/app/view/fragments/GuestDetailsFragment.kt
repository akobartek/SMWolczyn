package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.SheetFragmentGuestDetailsBinding
import pl.kapucyni.wolczyn.app.model.Guest
import pl.kapucyni.wolczyn.app.utils.GlideApp

class GuestDetailsFragment : Fragment() {

    private var _binding: SheetFragmentGuestDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentGuestDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hideBottomSheet.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).hideBottomSheet()
            else (parentFragment as ScheduleFragment).hideBottomSheet()
        }
        binding.linksBarLayout.facebookImage.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(0)
            else (parentFragment as ScheduleFragment).onIconClick(0)
        }
        binding.linksBarLayout.instagramImage.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(1)
            else (parentFragment as ScheduleFragment).onIconClick(1)
        }
        binding.linksBarLayout.youtubeImage.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(2)
            else (parentFragment as ScheduleFragment).onIconClick(2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setViewsValues(guest: Guest) {
        guest.let {
            GlideApp.with(this@GuestDetailsFragment)
                .load(it.photoUrl)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.guestPhoto)
            binding.guestName.text = it.name
            binding.guestDescription.text = it.description
            binding.linksBarLayout.root.visibility = View.VISIBLE

            binding.linksBarLayout.facebookImage.setImageResource(
                if (it.sites[0] != "") R.drawable.ic_facebook_color else R.drawable.ic_facebook_mono
            )
            binding.linksBarLayout.instagramImage.setImageResource(
                if (it.sites[1] != "") R.drawable.ic_instagram_color else R.drawable.ic_instagram_mono
            )
            binding.linksBarLayout.youtubeImage.setImageResource(
                if (it.sites[2] != "") R.drawable.ic_youtube_color else R.drawable.ic_youtube_mono
            )
        }
    }

    fun hideViews() {
        binding.guestPhoto.setImageResource(android.R.color.transparent)
        binding.guestName.text = ""
        binding.guestDescription.text = ""
        binding.linksBarLayout.root.visibility = View.INVISIBLE
    }
}
