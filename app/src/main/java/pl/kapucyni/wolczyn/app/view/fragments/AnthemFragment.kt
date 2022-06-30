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
        const val YOUTUBE_URL = "https://www.youtube.com/watch?v=FblN6tvGz1c"

        const val ANTHEM = "Nie boję się już, bo Ty jesteś ze mną\n" +
                "Nie boję się już, choć czasami tak trudno zrozumieć mi\n" +
                "Dokąd mam iść? Co mówić mam?  Kogo słuchać?\n" +
                "Ty uwalniasz mnie od lęków\n" +
                "Pokój dajesz mi\n\n" +
                "\t\tMiłość mnie ożywia, wszystko mi wyjaśnia\n" +
                "\t\tCierpliwie podnosi, jest zawsze wierna\n" +
                "\t\tMiłość mnie odżywia\n" +
                "\t\tWszystko  rozwiązuje\n" +
                "\t\tChcę być zawsze tam, gdzie ona\n" +
                "\t\tOd teraz wiem jak mam kochać\n" +
                "\t\tChce być zawsze tam, gdzie ona\n\n" +
                "W miłości chcę żyć\n" +
                "Bo ty działasz we mnie\n" +
                "W miłości chcę żyć\n" +
                "Dzięki mnie ten świat będzie zmieniał się\n" +
                "Uwielbiam tę miłość gdziekolwiek jest chcę za nią kroczyć\n" +
                "Ona uwalnia mnie od grzechu, daje nowy sens\n\n" +
                "\t\tMiłość mnie ożywia, wszystko mi wyjaśnia\n" +
                "\t\tCierpliwie podnosi,  jest zawsze wierna\n" +
                "\t\tMiłość mnie odżywia, wszystko rozwiązuje\n" +
                "\t\tChcę być zawsze tam, gdzie ona\n" +
                "\t\tOd teraz wiem jak mam kochać\n\n" +
                "Jeśli ta miłość ma działać we mnie, chcę być gotowy\n" +
                "Chcę iść za twoim głosem, więc mów do mnie\n" +
                "Jeśli ta miłość ma działać we mnie, chcę być gotowy\n" +
                "Chcę iść za twoim głosem, więc mów do mnie\n\n" +
                "Działaj! Działaj we mnie! \n\n" +
                "\t\tMiłość mnie ożywia, wszystko mi wyjaśnia\n" +
                "\t\tCierpliwie podnosi,  jest zawsze wierna\n" +
                "\t\tMiłość mnie odżywia, wszystko rozwiązuje\n" +
                "\t\tChcę być zawsze być tam gdzie ona\n" +
                "\t\tOd teraz wiem jak mam kochać\n\n" +
                "\t\tChcę być zawsze tam, gdzie ona\n"
    }
}
