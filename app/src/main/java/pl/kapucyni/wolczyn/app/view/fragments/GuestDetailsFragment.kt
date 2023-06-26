package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.SheetFragmentGuestDetailsBinding
import pl.kapucyni.wolczyn.app.model.Guest

class GuestDetailsFragment : BindingFragment<SheetFragmentGuestDetailsBinding>() {

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        SheetFragmentGuestDetailsBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        binding.hideBottomSheet.setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).hideBottomSheet()
            else (parentFragment as ScheduleFragment).hideBottomSheet()
        }
        binding.linksBarLayout.facebookImage.setupLinkButton(0)
        binding.linksBarLayout.instagramImage.setupLinkButton(1)
        binding.linksBarLayout.tiktokImage.setupLinkButton(2)
        binding.linksBarLayout.websiteImage.setupLinkButton(3)
        binding.linksBarLayout.youtubeImage.setupLinkButton(4)
    }

    private fun ImageButton.setupLinkButton(number: Int) {
        setOnClickListener {
            if (parentFragment is GuestListFragment)
                (parentFragment as GuestListFragment).onIconClick(number)
            else (parentFragment as ScheduleFragment).onIconClick(number)
        }
    }

    fun setViewsValues(guest: Guest) {
        guest.let {
            Glide.with(this@GuestDetailsFragment)
                .load(it.photoUrl)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.guestPhoto)
            binding.guestName.text = it.name
            binding.guestDescription.text = it.description
            binding.linksBarLayout.root.visibility = View.VISIBLE

            binding.linksBarLayout.facebookImage.isEnabled = it.sites[0] != ""
            binding.linksBarLayout.facebookImage.setImageResource(
                if (it.sites[0] != "") R.drawable.ic_facebook_color else R.drawable.ic_facebook_mono
            )
            binding.linksBarLayout.instagramImage.isEnabled = it.sites[1] != ""
            binding.linksBarLayout.instagramImage.setImageResource(
                if (it.sites[1] != "") R.drawable.ic_instagram_color else R.drawable.ic_instagram_mono
            )
            binding.linksBarLayout.tiktokImage.isEnabled = it.sites[2] != ""
            binding.linksBarLayout.tiktokImage.setImageResource(
                if (it.sites[2] != "") R.drawable.ic_tiktok_color else R.drawable.ic_tiktok_mono
            )
            binding.linksBarLayout.websiteImage.isEnabled = it.sites[3] != ""
            binding.linksBarLayout.websiteImage.setImageResource(
                if (it.sites[3] != "") R.drawable.ic_website_color else R.drawable.ic_website_mono
            )
            binding.linksBarLayout.youtubeImage.isEnabled = it.sites[4] != ""
            binding.linksBarLayout.youtubeImage.setImageResource(
                if (it.sites[4] != "") R.drawable.ic_youtube_color else R.drawable.ic_youtube_mono
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
