package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentGroupBinding
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.getAttributeDrawable
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

        activity?.let {
            mMainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            mMainViewModel.currentUser.observe(viewLifecycleOwner, { user ->
                if (user?.group != null) {
                    binding.groupsEmptyView.visibility = View.INVISIBLE
                    GlideApp.with(this@GroupFragment)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(it.getAttributeDrawable(R.attr.logoMenu))
                        .into(binding.userPhoto)
                    binding.userId.text = user.number.toString()
                    binding.userName.text =
                        "${if (user.prefix != null) user.prefix + " " else ""}${user.name} ${user.surname}"
                    binding.userGroup.text = getString(R.string.user_group, user.group.toString())

                    if (user.type != null && user.type == 2) mMainViewModel.fetchGroup()

                    if (user.bears != null && user.bears > 0) showBearsDialog(it)
                } else {
                    binding.groupsEmptyView.visibility = View.VISIBLE
                }
            })

            mMainViewModel.userGroup.observe(viewLifecycleOwner, { group ->
                if (group?.persons != null) {
                    val groupMembers = StringBuilder()
                    group.persons.forEach { person ->
                        groupMembers.append("${person.name}, ${person.age} - ${person.city}\n")
                    }
                    binding.groupMembers.text =
                        getString(R.string.group_members, groupMembers.toString())
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBearsDialog(activity: Activity) =
        AlertDialog.Builder(activity)
            .setTitle(R.string.bears_dialog_title)
            .setMessage(R.string.bears_dialog_message)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
}
