package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentAnthemBinding
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService

class AnthemFragment : Fragment() {

    private var _binding: FragmentAnthemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAnthemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.anthemText.text = ANTHEM
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.menu_anthem, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_yt_anthem -> {
            context?.openWebsiteInCustomTabsService(YOUTUBE_URL)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        const val YOUTUBE_URL = "https://www.youtube.com/watch?v=9NnGOWZyQoE"

        const val ANTHEM = "1. Utkałeś mnie w łonie mej matki,\n" +
                "pod jej sercem cudownie złożyłeś.\n" +
                "Tchnąłeś Ducha, więc żyję i kocham\n" +
                "chcę być taki jak, jak mnie stworzyłeś.\n" +
                "chcę być taki jak, jak mnie stworzyłeś.\n\n" +
                "2. Stworzyłeś nas mężczyzną i kobietą,\n" +
                "nasze święte życie, twoje od poczęcia,\n" +
                "ciało nasze jest twoją świątynią,\n" +
                "gdzie dusza spotyka swego Oblubieńca\n" +
                "gdzie dusza spotyka swego Oblubieńca\n\n" +
                "\t\tRef. Kto ma Ducha, słów tych słucha\n" +
                "\t\tsercem dziecka je przyjmuje.\n" +
                "\t\tKto bez Ducha, nie chce słuchać,\n" +
                "\t\tplanu Boga nie pojmuje. x2,\n\n" +
                "3. On i ona, ciał i dusz przymierze,\n" +
                "tu rodzi się życie i karmi się wiarą.\n" +
                "Dom ich kościołem, Kościół ich domem,\n" +
                "tu miłość dojrzewa, stając się ofiarą.\n" +
                "tu miłość dojrzewa, stając się ofiarą.\n\n" +
                "\t\tRef. Kto ma Ducha...\n\n" +
                "BRIDGE:\n" +
                "Nie chcę wymyślać świata bez Ciebie,\n" +
                "tego kim jestem, tego jak żyć,\n" +
                "lecz Ciebie słuchać, aby zrozumieć\n" +
                "jak Ty mnie widzisz, jaki mam być.\n\n" +
                "\t\tRef. Kto ma Ducha... X2\n\n" +
                "OUTRO:\n" +
                "Ooo Ooo Ooo\n" +
                "Słucham, by zrozumieć!\n\n"
    }
}
