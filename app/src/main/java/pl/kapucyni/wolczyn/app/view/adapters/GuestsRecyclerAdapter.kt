package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ItemGuestBinding
import pl.kapucyni.wolczyn.app.model.Guest
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.view.fragments.GuestListFragment

class GuestsRecyclerAdapter(
    private var mGuestsList: Array<Guest>,
    private val mFragment: GuestListFragment
) : RecyclerView.Adapter<GuestsRecyclerAdapter.GuestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder =
        GuestViewHolder(
            ItemGuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) =
        holder.bindView(mGuestsList[position])

    override fun getItemCount(): Int = mGuestsList.size

    inner class GuestViewHolder(private val binding: ItemGuestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(guest: Guest) {
            with(binding) {
                root.tag = guest.name
                guestName.text = guest.name
                GlideApp.with(root.context)
                    .load(guest.photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_menu_guests)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(guestPhoto)

                root.setOnClickListener {
                    if (mFragment.selectedGuest == null) mFragment.expandBottomSheet(guest)
                    else mFragment.hideBottomSheet()
                }
            }
        }
    }
}