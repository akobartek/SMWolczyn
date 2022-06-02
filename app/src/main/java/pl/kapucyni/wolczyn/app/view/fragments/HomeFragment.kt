package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import pl.kapucyni.wolczyn.app.databinding.FragmentHomeBinding

class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {}
}
