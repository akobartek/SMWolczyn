package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_guest.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Guest
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.view.fragments.GuestListFragment

class GuestsRecyclerAdapter(
    private var mGuestsList: Array<Guest>,
    private val mFragment: GuestListFragment
) :
    RecyclerView.Adapter<GuestsRecyclerAdapter.GuestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder =
        GuestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_guest, parent, false)
        )

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) =
        holder.bindView(mGuestsList[position])

    override fun getItemCount(): Int = mGuestsList.size

    inner class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(guest: Guest) {
            itemView.tag = guest.name
            itemView.guestName.text = guest.name
            GlideApp.with(itemView.context)
                .load(guest.photoUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_menu_guests)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.guestPhoto)

            itemView.setOnClickListener {
                if (mFragment.selectedGuest == null) mFragment.expandBottomSheet(guest)
                else mFragment.hideBottomSheet()
            }
        }
    }
}