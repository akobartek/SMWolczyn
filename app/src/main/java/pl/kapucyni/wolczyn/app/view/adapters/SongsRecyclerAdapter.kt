package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import pl.kapucyni.wolczyn.app.databinding.ItemSongBinding
import pl.kapucyni.wolczyn.app.view.fragments.SongBookFragment
import java.util.*
import kotlin.collections.ArrayList

class SongsRecyclerAdapter(
    private val mFragment: SongBookFragment,
    private val mSongTitles: Array<String>
) :
    RecyclerView.Adapter<SongsRecyclerAdapter.SongViewHolder>(), Filterable {

    private var mSongTitlesFiltered = arrayOf<String>()

    init {
        mSongTitlesFiltered = mSongTitles
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder =
        SongViewHolder(
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.bindView(mSongTitlesFiltered[position])

    override fun getItemCount(): Int = mSongTitlesFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mSongTitlesFiltered =
                    if (charString.isEmpty()) mSongTitles
                    else {
                        val filteredList = ArrayList<String>()
                        for (songIndex in mSongTitles.indices) {
                            if (mSongTitles[songIndex].lowercase(Locale.getDefault())
                                    .contains(charString.lowercase(Locale.getDefault()))
                                || SongBookFragment.songTexts[songIndex].lowercase(Locale.getDefault())
                                    .contains(charString.lowercase(Locale.getDefault()))
                            ) filteredList.add(mSongTitles[songIndex])
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

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(songTitle: String) {
            with(binding) {
                this.songTitle.text = songTitle
                root.setOnClickListener {
                    if (mFragment.selectedSong == null)
                        mFragment.onSongSelected(mSongTitles.indexOf(songTitle))
                    else mFragment.hideBottomSheet()
                }
            }
        }
    }
}