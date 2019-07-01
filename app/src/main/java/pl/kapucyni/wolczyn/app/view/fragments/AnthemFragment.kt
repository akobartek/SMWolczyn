package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_anthem.view.*

import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService

class AnthemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_anthem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.anthemText.text = ANTHEM
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.menu_anthem, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_yt_anthem -> {
            context?.openWebsiteInCustomTabsService(YOUTUBE_URL)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        const val YOUTUBE_URL = "https://www.youtube.com/watch?v=WuTZBX8pbSs"

        const val ANTHEM = "\t\tRef. Dajesz ciało żeby służyć,\n" +
                "\t\tPieniądz żeby służyć,\n" +
                "\t\tWładzę aby sługą być.\n" +
                "\t\tDaj mi nie żyć już dla siebie,\n" +
                "\t\tW drugim widzieć ciebie,\n" +
                "\t\tCzystym wolnym być.\n" +
                "\t\t(W Tobie żyć)\n\n" +
                "Sprzedaj co masz i rozdaj ubogim,\n" +
                "Daj ciało na ofiarę Bogu przyjemną.\n" +
                "Pierwszy jest sługą, bierze miejsce ostatnie.\n" +
                "Ta brama, ta ścieżka wiedzie tam gdzie Ojciec mieszka.\n\n" +
                "\t\tRef. Dajesz ciało żeby...\n\n" +
                "Gromadzenie, pożądanie, nad bliźnim panowanie \n" +
                "Nie tak będzie wśród was, tak czynią poganie\n" +
                "Czystość, posłuszeństwo, Bogu pierwszeństwo\n" +
                "Franciszek, nasz brat, budował Boży ład.\n\n" +
                "\t\tRef. Dajesz ciało żeby...\n\n" +
                "Zbuduj mnie na nowo przez Twoje słowo\n" +
                "Bym ja, by moje ciało, świątynią Twą się stało.\n" +
                "Daj mi serce nowe, daj serce gotowe,\n" +
                "Odważnie i z radością budować święty Kościół\n\n" +
                "\t\tRef. Dajesz ciało żeby...\n\n"
    }
}
