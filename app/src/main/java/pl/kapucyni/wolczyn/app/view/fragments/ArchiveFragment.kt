package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_archive.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogDataOutOfDate
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ArchiveRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.ArchiveViewModel

class ArchiveFragment : Fragment() {

    private lateinit var mArchiveViewModel: ArchiveViewModel
    private lateinit var mAdapter: ArchiveRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_archive, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ArchiveRecyclerAdapter(activity as MainActivity)
        view.archiveRecyclerView.layoutManager = LinearLayoutManager(view.context)
        view.archiveRecyclerView.itemAnimator = DefaultItemAnimator()
        view.archiveRecyclerView.adapter = mAdapter

        mArchiveViewModel = ViewModelProviders.of(this@ArchiveFragment).get(ArchiveViewModel::class.java)
        activity?.let { if (!it.checkNetworkConnection()) it.showNoInternetDialogDataOutOfDate() }
        mArchiveViewModel.fetchMeetings()
        mArchiveViewModel.meetings.observe(this@ArchiveFragment, Observer { meetings ->
            mAdapter.setWeatherList(meetings.sortedByDescending { it.number })
            view.archiveRecyclerView.scheduleLayoutAnimation()
            view.loadingIndicator.hide()
            if (meetings.isNullOrEmpty()) {
                view.emptyView.visibility = View.VISIBLE
            } else {
                view.emptyView.visibility = View.INVISIBLE
            }
        })
    }

    override fun onStop() {
        mArchiveViewModel.cancelAllRequests()
        super.onStop()
    }
}
