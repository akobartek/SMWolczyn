package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentBreviarySelectBinding

class BreviarySelectFragment(val openBreviary: (Int) -> Unit) :
    BindingFragment<FragmentBreviarySelectBinding>() {

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreviarySelectBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        with(binding) {
            breviarySelectList.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_listview,
                resources.getStringArray(R.array.breviary_list)
            )
            breviarySelectList.setOnItemClickListener { _, _, position, _ ->
                openBreviary(position)
            }
        }
    }
}