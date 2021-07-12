package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentGroupBinding
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.getAttributeDrawable
import pl.kapucyni.wolczyn.app.utils.showBearsDialog
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class GroupFragment : Fragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().let {
            mMainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            mMainViewModel.currentUser.observe(viewLifecycleOwner, { user ->
                if (user?.group != null) {
                    binding.groupsEmptyView.visibility = View.INVISIBLE
                    GlideApp.with(this@GroupFragment)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(it.getAttributeDrawable(R.attr.logoMenu))
                        .into(binding.userPhoto)
                    binding.userName.text =
                        "${if (user.prefix != null) user.prefix + " " else ""}${user.name} ${user.surname}"
                    binding.userId.text = getString(R.string.user_id, user.number.toString())
                    binding.userGroup.text = getString(R.string.user_group, user.group.toString())

                    if (user.type != null && user.type == 2) mMainViewModel.fetchGroup()
                    else binding.groupMembersTitle.visibility = View.GONE

                    if (user.bears != null && user.bears > 0) it.showBearsDialog()
                } else {
                    binding.groupMembers.visibility = View.GONE
                    binding.groupsEmptyView.visibility = View.VISIBLE
                }
            })

            mMainViewModel.userGroup.observe(viewLifecycleOwner, { group ->
                if (group?.persons != null) {
                    val groupMembers = StringBuilder()
                    group.persons.forEach { person ->
                        groupMembers.append("${person.name}, ${person.age} - ${person.city}\n")
                    }
                    binding.groupMembers.visibility = View.VISIBLE
                    binding.groupMembers.text = groupMembers.toString()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
