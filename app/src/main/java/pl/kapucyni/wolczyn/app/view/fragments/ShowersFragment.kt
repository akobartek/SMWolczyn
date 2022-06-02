package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentShowersBinding
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.showBearsDialog
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class ShowersFragment : BindingFragment<FragmentShowersBinding>() {

    private val mMainViewModel: MainViewModel by activityViewModels()

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentShowersBinding.inflate(inflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun setup(savedInstanceState: Bundle?) {
        requireActivity().let {
            mMainViewModel.currentUser.observe(viewLifecycleOwner) { user ->
                if (user?.showers != null && user.global_showers != null) {
                    binding.showersEmptyView.visibility = View.GONE
                    binding.showersLayout.visibility = View.VISIBLE
                    GlideApp.with(this@ShowersFragment)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(R.drawable.ic_logo)
                        .into(binding.userPhoto)
                    binding.userName.text =
                        "${if (user.prefix != null) user.prefix + " " else ""}${user.name} ${user.surname}"
                    binding.userId.text = getString(R.string.user_id, user.number.toString())

                    val showerNames = hashMapOf<Int, String>()
//                    user.global_showers.returnAllShowers().forEach { showerWithDay ->
//                        showerNames[showerWithDay.shower.id!!] =
//                            "${showerWithDay.day}: ${showerWithDay.shower.hour}"
//                    }
                    for ((day, list) in user.global_showers)
                        list.forEach { shower -> showerNames[shower.id!!] = "$day: ${shower.hour}" }
                    binding.showersList.text =
                        user.showers.substring(1, user.showers.length - 1)
                            .replace("\\", "")
                            .replace("/", "")
                            .replace("\"", "")
                            .split(", ")
                            .map { stringId -> stringId.toInt() }
                            .sortedBy { id -> id }
                            .map { id -> showerNames[id] }
                            .joinToString(",\n")

                    if (user.bears != null && user.bears > 0) it.showBearsDialog()
                } else {
                    binding.showersEmptyView.visibility = View.VISIBLE
                    binding.showersLayout.visibility = View.GONE
                }
            }
        }
    }
}