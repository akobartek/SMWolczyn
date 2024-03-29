package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentGroupBinding
import pl.kapucyni.wolczyn.app.utils.showBearsDialog
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class GroupFragment : BindingFragment<FragmentGroupBinding>() {

    private val mMainViewModel: MainViewModel by activityViewModels()

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGroupBinding.inflate(inflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun setup(savedInstanceState: Bundle?) {
        requireActivity().let {
            mMainViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user?.group != null) {
                    binding.groupsEmptyView.visibility = View.INVISIBLE
                    Glide.with(this@GroupFragment)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(R.drawable.ic_logo)
                        .into(binding.userPhoto)
                    binding.userName.text =
                        "${if (user.prefix != null) user.prefix + " " else ""}${user.name} ${user.surname}"
                    binding.userId.text = getString(R.string.user_id, user.number.toString())
                    binding.userGroup.text = getString(R.string.user_group, user.group.toString())

                    if (user.type != null && user.type == 2) mMainViewModel.fetchGroup()
                    else binding.groupMembersTitle.visibility = View.GONE

                    if (user.bears != null && user.bears > 0) it.showBearsDialog()
                } else {
                    binding.groupMembersTitle.visibility = View.GONE
                    binding.groupMembers.visibility = View.GONE
                    binding.groupsEmptyView.visibility = View.VISIBLE
                }
            }

            mMainViewModel.userGroup.observe(viewLifecycleOwner) { group ->
                if (group?.persons != null) {
                    val groupMembers = StringBuilder()
                    group.persons.forEach { person ->
                        groupMembers.append("${person.name}, ${person.age} - ${person.city}\n")
                    }
                    binding.groupMembersTitle.visibility = View.VISIBLE
                    binding.groupMembers.visibility = View.VISIBLE
                    val membersString = SpannableString(groupMembers.toString())
                    membersString.setSpan(
                        UnderlineSpan(),
                        0,
                        groupMembers.indexOf("\n"),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding.groupMembers.text = membersString
                }
            }
        }
    }
}
