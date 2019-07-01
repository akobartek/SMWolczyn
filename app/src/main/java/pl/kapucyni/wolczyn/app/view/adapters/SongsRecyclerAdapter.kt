package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_song.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.view.fragments.SongBookFragment

class SongsRecyclerAdapter(private val mFragment: SongBookFragment, private val mSongTitles: Array<String>) :
    RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder>(), Filterable {

    private var mSongTitlesFiltered = arrayOf<String>()

    init {
        mSongTitlesFiltered = mSongTitles
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder =
        SongViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false))

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.bindView(mSongTitlesFiltered[position])

    override fun getItemCount(): Int = mSongTitlesFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mSongTitlesFiltered = if (charString.isEmpty()) {
                    mSongTitles
                } else {
                    val filteredList = ArrayList<String>()
                    for (songIndex in mSongTitles.indices) {
                        if (mSongTitles[songIndex].toLowerCase().contains(charString.toLowerCase()) ||
                            SongBookFragment.songTexts[songIndex].toLowerCase().contains(charString.toLowerCase())
                        )
                            filteredList.add(mSongTitles[songIndex])
                    }

                    filteredList.toTypedArray()
                }

                val filterResults = FilterResults()
                filterResults.values = mSongTitlesFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                mSongTitlesFiltered = filterResults.values as Array<String>
                notifyDataSetChanged()
            }
        }
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(songTitle: String) {
            itemView.songTitle.text = songTitle

            itemView.setOnClickListener {
                if (mFragment.selectedSong == null) mFragment.expandBottomSheet(mSongTitles.indexOf(songTitle))
                else mFragment.hideBottomSheet()
            }
        }
    }
}