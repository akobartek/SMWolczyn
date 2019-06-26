package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_breviary.view.*

import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class BreviaryFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_breviary, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            mViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            loadBreviary()
        }
    }

    private fun loadBreviary() {
        mViewModel.getBreviaryHtml(arguments!!.getInt("breviaryType"))?.let { text ->
            view?.let {
                it.breviaryText.loadDataWithBaseURL(
                    null, text,
                    "text/html", "UTF-8", null
                )
                it.breviaryText.visibility = View.VISIBLE
                it.breviaryText.scrollTo(0, 0)
                it.breviaryText.animate().alpha(1f).duration = 444L
            }
        }
    }

    companion object {
        fun newInstance(breviaryType: Int): BreviaryFragment {
            return BreviaryFragment().apply {
                arguments = Bundle().apply {
                    putInt("breviaryType", breviaryType)
                }
            }
        }
    }
}
