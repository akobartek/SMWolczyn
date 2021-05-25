package pl.kapucyni.wolczyn.app.view.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_songbook.view.*
import kotlinx.android.synthetic.main.sheet_fragment_song.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.view.adapters.SongsRecyclerAdapter

class SongBookFragment : Fragment() {

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var mAdapter: SongsRecyclerAdapter
    private lateinit var mSearchView: SearchView
    var selectedSong: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_songbook, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = SongsRecyclerAdapter(this@SongBookFragment, songTitles)
        view.songsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        view.songsRecyclerView.itemAnimator = DefaultItemAnimator()
        view.songsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                DividerItemDecoration.VERTICAL
            )
        )
        view.songsRecyclerView.adapter = mAdapter
        view.songsRecyclerView.scheduleLayoutAnimation()

        mBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.songTextSheet))
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                selectedSong?.let {
                    bottomSheet.songName.text = songTitles[it]
                    bottomSheet.songText.text = songTexts[it]
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.songsRecyclerView.alpha = 1f
                    selectedSong = null
                    bottomSheet.songName.text = ""
                    bottomSheet.songText.text = ""
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        if (savedInstanceState != null) {
            val song = savedInstanceState.getInt("song", -1)
            if (song != -1) expandBottomSheet(song)
        }

        view.songsListLayout.setOnClickListener { if (selectedSong != null) hideBottomSheet() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedSong?.let { outState.putInt("song", it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_songbook, menu)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = menu.findItem(R.id.action_search).actionView as SearchView
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        mSearchView.maxWidth = Integer.MAX_VALUE

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_search) true else super.onOptionsItemSelected(item)
    }

    fun expandBottomSheet(position: Int) {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        selectedSong = position
        view?.songsRecyclerView?.animate()
            ?.alpha(0.15f)
            ?.duration = 200
    }

    fun hideBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun onBackPressed(): Boolean {
        return if (!mSearchView.isIconified) {
            mSearchView.isIconified = true
            false
        } else if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            true
        } else {
            hideBottomSheet()
            false
        }
    }

    companion object {
        val songTitles = arrayOf(
            "Hosanna (mam moc)",
            "Jesteśmy piękni",
            "Jezu ufam Tobie",
            "Łaska Pana",
            "Melodia stęsknionych serc",
            "Niech oblicze Twe",
            "O tak tak / Pan jest wśród nas",
            "On miłością jest",
            "Pan jest pasterzem moim",
            "Podnieś mnie Jezu",
            "Powstań i żyj",
            "Przed tronem Twym",
            "Rzeka",
            "Święty",
            "Ukaż mi Panie swą twarz",
            "W Tobie jest światło",
            "Zmiłuj się nade mną Boże"
        )

        val songTexts = arrayOf(
            "1. Niech słaby powie: mam moc\n" +
                    "Biedy wyzna: wszystko mam\n" +
                    "ślepy mówi: widzę znów\n" +
                    "We mnie to uczynił Bóg\n\n" +
                    "Hosanna Hosanna Barankowi co siebie dał\n" +
                    "Hosanna Hosanna Jezus zmarł i zmartwychwstał\n\n" +
                    "2. Wejdę w rzekę gdzie grzechy me\n" +
                    "Zmywasz Zbawco swoją krwią\n" +
                    "Z niebios miłość wylewa się\n" +
                    "Łaską swą ogarnij mnie\n\n",
            "Jesteśmy piękni Twoim pięknem Panie! X2\n" +
                    "Ty otwierasz nasze oczy, na piękno Twoje Panie!\n" +
                    "Ty otwierasz nasze oczy, Panie!\n" +
                    "Ty otwierasz nasze oczy na piękno Twoje Panie!\n" +
                    "Ty otwierasz nasze oczy Panie, na Twoje piękno!\n\n",
            "Jezu ufam Tobie x4",
            "Niech nas ogarnie łaska Panie Twa,\n" +
                    "Duch Twój Święty niech dotnie nas\n",
            "Jezus Jezus, melodia stęsknionych serc\n" +
                    "Jezu Jezu przyjdź do nas wołamy Cię\n" +
                    "Hosanna Hosanna Ty koisz wszelki ból\n" +
                    "Przyjaciel, Wybawca\n" +
                    "oczekiwany Król\n\n" +
                    "Jeden dzień jak tysiąc lat,\n" +
                    "tysiąc lat jak jeden dzień,\n" +
                    "a my wciąż z nadzieją spoglądamy w górę\n\n" +
                    "Kiedy się wypełni czas,\n" +
                    "każde oko ujrzy Cię,\n" +
                    "każdy język wyzna,\n" +
                    "że Ty jesteś Królem\n\n",
            "Niech oblicze Twe Panie mój,\n" +
                    "zajaśnieje nad sługą Twym,\n" +
                    "niech przenika mnie jego blask.\n\n",
            "O tak, tak, tak Panie mówię tak Twemu słowu\n" +
                    "O tak, tak, tak Panie mówię tak Twojej woli\n" +
                    "O tak, tak, tak Panie mówię tak Twym natchnieniom\n" +
                    "O tak, tak, tak Panie mówię tak Twemu prawu\n\n" +
                    "Jesteś mym Pasterzem, uczysz mnie jak tutaj żyć\n" +
                    "Twoje napomnienia chronią mnie, strzegą mnie, dają życie mi\n" +
                    "O Alleluja, Alleluja\n\n" +
                    "Pan jest wśród nas, prawdziwie jest wśród nas\n" +
                    "Pan jest wśród nas, widzę Go\n\n" +
                    "Kto zmartwychwstał i króluje?\n" +
                    "Jezus, Jezus!\n" +
                    "Kto jest tutaj by nam służyć?\n" +
                    "Jezus, Jezus!\n\n" +
                    "Pan zmartwychwstały,\n" +
                    "wspaniały nasz Przyjaciel,\n" +
                    "Chrystus Emmanuel tutaj jest! /2x/\n\n",
            "Nasze życie nie jest łatwe,\n" +
                    "grzechem skażone każde jest.\n" +
                    "Jeśli w sercu Twym cierpienie,\n" +
                    "Jezus jego ukojeniem.\n" +
                    "Pozwól Mu, by wziął co trudne,\n" +
                    "każdy grzech i to co brudne jest.\n" +
                    "On jedynym Zbawicielem jest!\n\n" +
                    "Jezus podniesie Cię,\n" +
                    "Jezus wybacza grzech\n" +
                    "Jezus pocieszy Cię gdy Twoje serce płacze\n" +
                    "On miłością jest!\n\n",
            "Pan jest Pasterzem moim,\n" +
                    "niczego mi nie braknie,\n" +
                    "na zielonych niwach pasie mnie.\n" +
                    "Nad spokojne wody mnie prowadzi\n" +
                    "i duszę mą pokrzepia,\n" +
                    "i wiedzie mnie ścieżkami\n" +
                    "sprawiedliwości Swojej.\n\n" +
                    "Choćbym nawet szedł ciemną doliną,\n" +
                    "zła się nie ulęknę, boś Ty ze mną,\n" +
                    "laska Twoja i kij Twój mnie pocieszają.\n\n" +
                    "La laj, la la la laj...\n\n",
            "Podnieś mnie Jezu i prowadź do Ojca x2\n" +
                    "\n" +
                    "Zanurz mnie w wodzie Jego miłosierdzia\n" +
                    "Amen\n\n",
            "Powstań i żyj, chociaż wokół mrok.,\n" +
                    "powstań i żyj, dobro wielką ma moc,\n" +
                    "\n" +
                    "powstań i żyj, choć upadłeś nie raz,\n" +
                    "Jezus doda Ci sił, On zmartwychwstał byś żył,\n" +
                    "Jezus doda Ci sił, On zmartwychwstał byś żył!\n\n" +
                    "Ile trzeba łez, aby wrócić do Ciebie,\n" +
                    "jak daleko oddalić się, by usłyszeć twój szept,\n" +
                    "jak bardzo żałować, aby pękło to serce kamienne.\n" +
                    "Ile trzeba łez, aby wrócić do Ciebie.\n\n",
            "Przed tronem Twym stoimy\n" +
                    "Wpatrzeni w Twej miłości blask\n" +
                    "Do Ciebie Panie podobni\n" +
                    "Stajemy się widząc Twą twarz\n\n" +
                    "Chwała Twa wypełnia nas\n" +
                    "Obecności Twojej blask\n" +
                    "Gdy wielbimy Ciebie, wiem\n" +
                    "Jesteś tu.\n\n" +
                    "Chwała, cześć\n" +
                    "Mądrość, moc, błogosławieństwo\n" +
                    "Na wieki, na wieki\n\n",
            "Dobro wypełnia tej rzeki bieg\n" +
                    "Każdy mój smutek w jej źródle topi się\n" +
                    "Ocean łaski - głębszy niż strach\n" +
                    "Niech się rozlewa, rośnie\n\n" +
                    "W środku tej rzeki moc objawia się\n" +
                    "z Bożego serca wciąż wylewa się\n" +
                    "niebieska łaska na nas spływa w dół\n" +
                    "Niech się rozlewa rośnie\n\n" +
                    "Wzbierają wody, wzbierają rzeki\n" +
                    "Zdrój wody Twej wylewa się /x2\n" +
                    "Ta rzeka daje nam życie /x4\n\n" +
                    "Otwieraj więźniom drzwi\n" +
                    "Wypuszczaj wolno ich\n" +
                    "niech tryska moc, niech tryska moc\n" +
                    "niech we mnie budzi się\n\n" +
                    "Nic nie zatrzyma mnie\n" +
                    "w radości tańczyć chcę\n" +
                    "niech tryska moc, niech tryska moc\n" +
                    "niech we mnie budzi się\n\n",
            "Wszystko czego chcę to Ty i Twoja łaska\n" +
                    "Wszystko czego chcę to Ty\n" +
                    "Jesteś Święty, Nieskończony, Potężny Pan\n" +
                    "Twoja łaska wiecznie trwa,\n" +
                    "dla Ciebie wszystko możliwe jest\n" +
                    "I raduje się moje serce,\n" +
                    "że przyszedłeś tu na ziemię,\n" +
                    "żeby dać mi życie wieczne,\n" +
                    "uwielbiam Ciebie Panie!\n\n",
            "Ukaż mi Panie swą twarz\n" +
                    "Daj mi usłyszeć Twój głos\n" +
                    "Bo słodki jest Twój głos\n" +
                    "i twarz pełna wdzięku\n" +
                    "Ukaż mi Panie swą twarz\n\n",
            "W Tobie jest światło" +
                    "każdy mrok rozjaśnia\n" +
                    "W Tobie jest życie\n" +
                    "Ono śmierć zwycięża\n" +
                    "Ufam Tobie, Miłosierny\n" +
                    "Jezu wybaw nas\n\n",
            "Zmiłuj się nade mną Boże\n" +
                    "ulituj się nad grzechem mym\n" +
                    "Łaską swą zgładź mą nieprawość\n" +
                    "i obmyj mnie z moich win\n\n" +
                    "Skruszonym sercem nie pogardzisz Panie x3\n" +
                    "Wiem, nie pogardzisz sercem mym\n\n" +
                    "Odnów we mnie serce czyste\n" +
                    "Daj mi moc swojego Ducha\n\n" +
                    "Panie daj, daj serce czyste\n" +
                    "poślij nam swojego Ducha\n" +
                    "Panie daj, daj serce czyste\n" +
                    "daj mi moc, daj mi moc\n\n"
        )
    }
}
