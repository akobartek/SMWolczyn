package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentAnthemBinding
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService

class AnthemFragment : BindingFragment<FragmentAnthemBinding>() {

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) = run {
        setHasOptionsMenu(true)
        FragmentAnthemBinding.inflate(inflater, container, false)
    }

    override fun setup(savedInstanceState: Bundle?) {
        binding.anthemText.text = ANTHEM
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_anthem, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_yt_anthem -> {
            requireContext().openWebsiteInCustomTabsService(YOUTUBE_URL)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        const val YOUTUBE_URL = "https://www.youtube.com/watch?v=zxmi6j548V8"

        const val ANTHEM = "Kim jestem w Twoich oczach\nJak widzisz mnie Ty\n" +
                "Tyle pytań mam do Ciebie\nGłowa pełna wątpliwości jest\n\n" +
                "Czy odkryje tutaj prawdę\nCzy usłyszę Twój głos\nCzy naprawdę mnie kochasz\n\n" +
                "Chcę się dowiedzieć\nCzekam na Ciebie\nWołam przyjdź\n\n" +
                "To jest czas prawdy\nprawdziwego przebaczenia\npełnej wolności\nduchowego wyzwolenia\n\n" +
                "W mojej duszy wielki wybuch\nPada na mnie Twój cień\n" +
                "Odpalam się dla Ciebie\nW lustrze widzę wreszcie Twoją twarz\n\n" +
                "Dziś odrzucam moją ciemność\nzapamiętam ten czas\nCzy to światło nie zgaśnie\n\n" +
                "Chcę się dowiedzieć\nCzekam na Ciebie\nWołam przyjdź żyj we mnie\n"
    }
}
