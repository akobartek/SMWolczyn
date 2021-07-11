package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentShowersBinding
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.getAttributeDrawable
import pl.kapucyni.wolczyn.app.utils.showBearsDialog
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class ShowersFragment : Fragment() {

    private var _binding: FragmentShowersBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().let {
            mMainViewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            mMainViewModel.currentUser.observe(viewLifecycleOwner, { user ->
                if (user?.showers != null && user.global_showers != null) {
                    binding.showersEmptyView.visibility = View.GONE
                    binding.showersLayout.visibility = View.VISIBLE
                    GlideApp.with(this@ShowersFragment)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(it.getAttributeDrawable(R.attr.logoMenu))
                        .into(binding.userPhoto)
                    binding.userName.text =
                        "${if (user.prefix != null) user.prefix + " " else ""}${user.name} ${user.surname}"
                    binding.userId.text = getString(R.string.user_id, user.number.toString())

                    val showerNames = hashMapOf<Int, String>()
                    for ((day, list) in user.global_showers)
                        list.forEach { shower -> showerNames[shower.id!!] = "$day: ${shower.hour}" }
                    binding.showersList.text =
                        user.showers.substring(1, user.showers.length - 1)
                            .split(", ")
                            .map { stringId -> stringId.toInt() }
                            .map { id -> showerNames[id] }
                            .joinToString(",\n")

                    if (user.bears != null && user.bears > 0) it.showBearsDialog()
                } else {
                    binding.showersEmptyView.visibility = View.VISIBLE
                    binding.showersLayout.visibility = View.GONE
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}