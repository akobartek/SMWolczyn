package pl.kapucyni.wolczyn.app.view.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_group.view.*

import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.getAttributeDrawable
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class GroupFragment : Fragment() {

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_group, container, false)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            mMainViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            mMainViewModel.userLiveData.observe(this@GroupFragment, Observer { user ->
                if (user?.group != null) {
                    view.groupsEmptyView.visibility = View.INVISIBLE
                    GlideApp.with(this@GroupFragment)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(it.getAttributeDrawable(R.attr.logoMenu))
                        .into(view.userPhoto)
                    view.userId.text = user.number.toString()
                    view.userName.text = "${user.name} ${user.surname}"
                    view.userGroup.text = getString(R.string.user_group, user.group.toString())
                } else {
                    view.groupsEmptyView.visibility = View.VISIBLE
                }
            })
        }
    }
}
