package pl.kapucyni.wolczyn.app.view.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentSongbookBinding
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.SongsRecyclerAdapter

class SongBookFragment : BindingFragment<FragmentSongbookBinding>() {

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var mAdapter: SongsRecyclerAdapter
    private lateinit var mSearchView: SearchView
    var selectedSong: Int? = null

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) = run {
        setHasOptionsMenu(true)
        FragmentSongbookBinding.inflate(inflater, container, false)
    }

    override fun setup(savedInstanceState: Bundle?) {
        mAdapter = SongsRecyclerAdapter(this@SongBookFragment, songTitles)
        binding.songsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
            adapter = mAdapter
            scheduleLayoutAnimation()
        }

        mBottomSheetBehavior = BottomSheetBehavior.from(binding.songTextSheet)
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val songFragment =
                    childFragmentManager.findFragmentById(R.id.songTextSheet) as SongFragment
                selectedSong?.let {
                    songFragment.setSongViews(songTitles[it], songTexts[it])
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.songsRecyclerView.alpha = 1f
                    selectedSong = null
                    songFragment.setSongViews("", "")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        if (savedInstanceState != null) {
            val song = savedInstanceState.getInt("song", -1)
            if (song != -1) onSongSelected(song)
        }

        binding.songsListLayout.setOnClickListener { if (selectedSong != null) hideBottomSheet() }
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

    fun onSongSelected(position: Int) {
        if (position != 0) {
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            selectedSong = position
            binding.songsRecyclerView.animate().alpha(0.15f).duration = 200
        } else if (MainActivity.APP_MEETING_MODE_ACTIVATED)
            (requireActivity() as MainActivity).goToSelectedFragment(R.id.nav_anthem)
        else
            showAnthemNotAvailableDialog()
    }

    fun hideBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun onBackPressed(): Boolean =
        if (!mSearchView.isIconified) {
            mSearchView.isIconified = true
            false
        } else if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            true
        } else {
            hideBottomSheet()
            false
        }

    private fun showAnthemNotAvailableDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.anthem_not_available_title)
            .setMessage(R.string.anthem_not_available_msg)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        val songTitles = arrayOf(
            "HYMN",
            "Tak bowiem Bóg",
            "Cały mój świat",
            "Kim jesteś Ty, Panie",
            "Przed tronem Twym",
            "Kosmos",
            "Ukaż mi Panie swą twarz",
            "Hosanna (mam moc)",
            "Naucz nas",
            "Zawitaj Ukrzyżowany",
            "Jaśnieje Krzyż Chwalebny",
            "Podnieś mnie Jezu",
            "Powstań i żyj",
            "On miłością jest",
            "W Chrystusie mym",
            "Zmiłuj się nade mną Boże",
            "Święty",
            "Odradzam się",
            "O tak tak / Pan jest wśród nas",
            "Jezus Chrystus Panem jest",
            "Rzeka",
            "Wzywam Cię",
            "Jezu jesteś tu",
            "Duchu Święty przyjdź",
            "Królestwo",
            "Zaprowadź mnie tam",
            "Ubi Caritas"
        )

        val songTexts = arrayOf(
            "",
            "Tak bowiem Bóg umiłował świat,\n" +
                    "że Syna swego nam dał\n" +
                    "Aby każdy kto w Niego wierzy\n" +
                    "miał życie wieczne.\n",
            "Zwrotka:\n" +
                    "Jesteś moim domem, moim bezpieczeństwem\n" +
                    "Jesteś wszystkim czego szukam\n" +
                    "Ty jesteś, jesteś\n\n" +
                    "Ooo, Obecny\n" +
                    "Ooo, Ty jesteś\n" +
                    "Ooo, Niezmienny\n\n" +
                    "Refren:\n" +
                    "W Tobie cały mój świat\n" +
                    "Ty jesteś moim ocaleniem\n" +
                    "W Tobie cały mój świat\n\n" +
                    "W Tobie cały mój świat\n" +
                    "Podnoszę ręce, by chwalić Ciebie\n" +
                    "W Tobie cały mój świat\n\n" +
                    "Zwrotka 2:\n" +
                    "Jesteś moim niebem, moim ukojeniem\n" +
                    "Jesteś wszystkim na co czekam\n" +
                    "Ty jesteś, jesteś\n\n" +
                    "Ooo, Obecny\n" +
                    "Ooo, Ty jesteś\n" +
                    "Ooo, Niezmienny\n\n" +
                    "Ref. W Tobie cały mój świat...\n\n" +
                    "Bridge:\n" +
                    "Cały mój świat x2\n" +
                    "W Tobie cały mój świat x4\n\n" +
                    "Ref. W Tobie cały mój świat...\n\n",
            "Kim jesteś Ty Panie, a kim jestem ja?\n" +
                    "Kim Ty? A kim ja?\n\n",
            "Przed tronem Twym stoimy\n" +
                    "Wpatrzeni w Twej miłości blask\n" +
                    "Do Ciebie Panie podobni\n" +
                    "Stajemy się widząc Twą twarz\n\n" +
                    "Refren:\n" +
                    "Chwała Twa wypełnia nas\n" +
                    "Obecności Twojej blask\n" +
                    "Gdy wielbimy Ciebie, wiem\n" +
                    "Jesteś tu.\n\n" +
                    "Bridge:\n" +
                    "Chwała, cześć\n" +
                    "Mądrość, moc, błogosławieństwo\n" +
                    "Na wieki, na wieki\n\n",
            "ZWROTKA 1:\n" +
                    "Jak to możliwe jest,\n" +
                    "że Ty jeden umiesz kochać mnie za darmo,\n" +
                    "kochać za darmo\n" +
                    "Nie odrzucasz mnie\n" +
                    "Mimo błędów każdy dzień to nowa szansa\n" +
                    "to nowa szansa\n\n" +
                    "REFREN:\n" +
                    "Twoja miłość to kosmos\n" +
                    "niezmierzona tajemnica\n" +
                    "Twoja miłość to kosmos\n" +
                    "nieustannie mnie zachwyca\n" +
                    "Twoja miłość to kosmos x3\n\n" +
                    "ZWROTKA 2:\n" +
                    "W lustrze widzę Cię\n" +
                    "Twe odbicie patrzy na mnie z przebaczeniem\n" +
                    "z przebaczeniem\n" +
                    "Nie oceniasz mnie\n" +
                    "w Twoich oczach najważniejsza jest miłość\n" +
                    "liczy się miłość\n\n" +
                    "REFREN x2\n\n",
            "Ukaż mi Panie swą twarz\n" +
                    "Daj mi usłyszeć Twój głos\n" +
                    "Bo słodki jest Twój głos\n" +
                    "i twarz pełna wdzięku\n" +
                    "Ukaż mi Panie swą twarz\n\n",
            "1. Niech słaby powie: „Mam moc”,\n" +
                    "Biedny wyzna: „Wszystko mam”,\n" +
                    "Ślepy mówi: „Widzę znów”, we mnie to uczynił Bóg\n\n" +
                    "Ref. Hosanna, Hosanna Barankowi, co siebie dał.\n" +
                    "Hosanna, Hosanna, Jezus zmarł i zmartwychwstał.\n\n" +
                    "2. Wejdę w rzekę, gdzie grzechy me\n" +
                    "Zmywasz, Zbawco, swoją krwią.\n" +
                    "Z niebios miłość wylewa się, łaską swą ogarnij mnie.\n\n",
            "Zwrotka 1:\n" +
                    "Przychodzimy do Ciebie Panie\n" +
                    "przynosimy Ci wszystko co mamy\n" +
                    "Otwórz nasze oczy na cuda,\n" +
                    "których nie dostrzegamy\n\n" +
                    "Przychodzimy do Ciebie Panie\n" +
                    "przynosimy Ci wszystko co mamy\n" +
                    "Otwórz nasze serca na miłość,\n" +
                    "której nie dostrzegamy\n\n" +
                    "Naucz nas pragnąć Ciebie\n" +
                    "Przebywać w Twej obecności\n" +
                    "Pozwól nam ujrzeć Twoją twarz\n" +
                    "Doświadczyć Twojej miłości\n\n" +
                    "(instrumental)\n\n" +
                    "Zwrotka 2:\n" +
                    "Przychodzimy do Ciebie Panie\n" +
                    "przynosimy Ci wszystko co mamy\n" +
                    "Otwórz nasze oczy na cuda,\n" +
                    "których nie dostrzegamy\n\n" +
                    "Naucz nas pragnąć Ciebie\n" +
                    "Przebywać w Twej obecności\n" +
                    "Pozwól nam ujrzeć Twoją twarz\n" +
                    "Doświadczyć Twojej miłości\n\n" +
                    "Refren:\n" +
                    "Tylko Tobie chwała\n" +
                    "Tobie chwała\n\n" +
                    "Bridge:\n" +
                    "Jezus\n" +
                    "Nasza pomoc jest w Imieniu Pana, który stworzył niebo i ziemię\n\n",
            "Zwrotka 1:\n" +
                    "Zawitaj, Ukrzyżowany,\n" +
                    "Jezu Chryste przez Twe rany.\n" +
                    "Królu na niebie, prosimy Ciebie,\n" +
                    "ratuj nas w każdej potrzebie.\n\n" +
                    "Zwrotka 2:\n" +
                    "Zawitaj, Ukrzyżowany,\n" +
                    "całujem Twe święte rany;\n" +
                    "przebite ręce, nogi w Twej męce,\n" +
                    "miejcież nas w swojej opiece.\n\n" +
                    "Zwrotka 3:\n" +
                    "Zawitaj, Ukrzyżowany,\n" +
                    "cierniem ukoronowany;\n" +
                    "we czci i chwale zniszczony wcale\n" +
                    "w takiej koronie, zbolałe skronie,\n" +
                    "miejcież nas w swojej obronie.\n\n" +
                    "Bridge:\n" +
                    "Smutki i żale, w serca upale wynieścież nas ku swej chwale\n" +
                    "Rano w ramieniu, z niej Krwi strumieniu, pociągnij nas ku zbawieniu\n" +
                    "Za grzechy płaczę sercem Cię raczę, Krzyżem Twoim głowę znaczę.\n\n" +
                    "Outro:\n" +
                    "O Jezu, miłości zdroju, wzdycham do Twego pokoju!\n" +
                    "O Jezu, miłości zdroju\n\n",
            "Jaśnieje Krzyż chwalebny,\n" +
                    "Unosi ciało Pana,\n" +
                    "Zaś On swej krwi strumieniem\n" +
                    "Obmywa nasze rany.\n\n" +
                    "Z miłości czystej dla nas\n" +
                    "Pokorną stał się żertwą,\n" +
                    "Baranek święty wyrwał\n" +
                    "Swe owce z paszczy wilka.\n\n" +
                    "Wykupił świat od klęski\n" +
                    "Przebitych rąk zapłatą,\n" +
                    "I własne tracąc życie,\n" +
                    "Powstrzymał pochód śmierci.\n\n" +
                    "Skrwawionym ostrzem gwoździa\n" +
                    "Tę samą dłoń przeszyto,\n" +
                    "Co zmyła winę Pawła,\n" +
                    "Wydarła śmierci Piotra.\n\n" +
                    "O Drzewo Życiodajne,\n" +
                    "Szlachetne w swej słodyczy,\n" +
                    "Wszak zieleń twych gałęzi\n" +
                    "Wydaje owoc nowy.\n\n" +
                    "Twa woń ma moc obudzić\n" +
                    "Wystygłe ciała zmarłych,\n" +
                    "Powrócą wnet do życia\n" +
                    "Mieszkańcy kraju nocy.\n\n" +
                    "Pod liści twoich cieniem\n" +
                    "Nie straszny czas upału,\n" +
                    "Słoneczny żar w południe\n" +
                    "I blask księżyca nocą.\n\n" +
                    "Jaśniejesz zasadzone\n" +
                    "Nad wody żywej zdrojem,\n" +
                    "I blask rozsiewasz wokół\n" +
                    "Świeżością kwiecia zdobny.\n\n" +
                    "Pośrodku twoich ramion,\n" +
                    "Gdzie winny krzew rozpięty,\n" +
                    "Spływają krwawe strugi\n" +
                    "Czerwienią słodką wina.\n\n",
            "Podnieś mnie Jezu i prowadź do Ojca x2\n" +
                    "Zanurz mnie w wodzie Jego miłosierdzia\n" +
                    "Amen\n\n",
            "Zwrotka:\n" +
                    "Powstań i żyj, chociaż wokół mrok,\n" +
                    "powstań i żyj, dobro wielką ma moc,\n" +
                    "powstań i żyj, choć upadłeś nie raz,\n" +
                    "Jezus doda Ci sił, On zmartwychwstał byś żył,\n" +
                    "Jezus doda Ci sił, On zmartwychwstał byś żył!\n\n" +
                    "Refren:\n" +
                    "Ile trzeba łez, aby wrócić do Ciebie,\n" +
                    "jak daleko oddalić się, by usłyszeć Twój szept,\n" +
                    "jak bardzo żałować, aby pękło to serce kamienne.\n" +
                    "Ile trzeba łez, aby wrócić do Ciebie.\n\n" +
                    "Outro:\n" +
                    "Powstań i żyj, powstań i żyj\n\n",
            "Nasze życie nie jest łatwe\n" +
                    "grzechem skażone każde jest\n" +
                    "Jeśli w sercu Twym cierpienie\n" +
                    "Jezus jego ukojeniem\n\n" +
                    "Pozwól Mu, by wziął co trudne\n" +
                    "każdy grzech i to co brudne jest\n" +
                    "On jedynym Zbawicielem\n" +
                    "w Jego ranach Twoje odkupienie\n\n" +
                    "(instrumental)\n\n" +
                    "Nasze życie nie jest łatwe\n" +
                    "grzechem skażone każde jest\n" +
                    "Jeśli w sercu Twym cierpienie\n" +
                    "Jezus jego ukojeniem\n\n" +
                    "Pozwól Mu, by wziął co trudne\n" +
                    "każdy grzech i to co brudne jest\n" +
                    "On jedynym Zbawicielem jest\n\n" +
                    "Jezus podniesie Cię\n" +
                    "Jezus wybacza grzech\n" +
                    "Jezus pocieszy Cię, gdy Twoje serce płacze x2\n\n" +
                    "On miłością jest\n\n",
            "Zwrotka 1:\n" +
                    "W Chrystusie mym nadzieję mam\n" +
                    "On moim światłem, pieśnią mą\n" +
                    "On fundamentem, skałą mą\n" +
                    "On mnie prowadzi w dzień i w noc\n\n" +
                    "Jak wielka moc w miłości tej,\n" +
                    "co daje pokój duszy mej\n" +
                    "Pociesza mnie i wszystkim jest\n" +
                    "w Bożej miłości żyć dziś chcę\n\n" +
                    "(instrumental)\n\n" +
                    "Zwrotka 1\n\n" +
                    "Zwrotka 2:\n" +
                    "Człowiekiem stał się Chrystus Pan\n" +
                    "Niewinnym dzieckiem Bóg się stał\n" +
                    "Miłości sprawiedliwej dar\n" +
                    "Wzgardzony Boży Syn nam dał\n\n" +
                    "Na krzyżu tym, gdzie Jezus zmarł,\n" +
                    "by za mnie swoje życie dać\n" +
                    "Mój każdy grzech na siebie wziął\n" +
                    "W śmierci Chrystusa życie mam\n\n" +
                    "Na krzyżu tym, gdzie Jezus zmarł,\n" +
                    "by za mnie swoje życie dać x4\n\n" +
                    "Mój każdy grzech na siebie wziął\n" +
                    "W śmierci Chrystusa życie mam\n\n",
            "Zmiłuj się nade mną Boże\n" +
                    "ulituj się nad grzechem mym\n" +
                    "łaską swą zgładź mą nieprawość\n" +
                    "i obmyj mnie z moich win\tx2\n\n" +
                    "Skruszonym sercem nie pogardzisz Panie\n" +
                    "Skruszonym sercem nie pogardzisz Panie\n" +
                    "Skruszonym sercem nie pogardzisz Panie\n" +
                    "Wiem, nie pogardzisz sercem mym\tx2\n\n" +
                    "Odnów we mnie serce czyste\n" +
                    "Daj mi moc swojego Ducha\tx6\n\n" +
                    "Outro:\n" +
                    "Panie daj, daj serce czyste\n" +
                    "poślij nam swojego Ducha\n" +
                    "Panie daj, daj serce czyste\n" +
                    "daj mi moc, daj mi moc\n\n",
            "Zwrotka:\n" +
                    "Wszystko czego chcę to Ty i Twoja łaska\n" +
                    "Wszystko czego chcę to Ty x2\n\n" +
                    "Refren:\n" +
                    "Jesteś święty, nieskończony, potężny Pan\n" +
                    "Twoja łaska wiecznie trwa, dla Ciebie wszystko możliwe jest x2\n\n" +
                    "Zwrotka\n\n" +
                    "Ref. Jesteś święty...\n\n" +
                    "Bridge:\n" +
                    "I raduje się moje serce,\n" +
                    "że przyszedłeś tu na ziemię,\n" +
                    "żeby dać mi życie wieczne,\n" +
                    "uwielbiam Ciebie Panie x2\n\n" +
                    "Ref. Jesteś święty...\n\n" +
                    "Outro:\n" +
                    "Jesteś Święty! x6\n\n",
            "Zwrotka 1:\n" +
                    "Do Ciebie dziś wracam znów, tak jak wtedy\n" +
                    "Kiedy przyjąłeś mnie po raz pierwszy\n" +
                    "Przyjmij, teraz znów przyjmij mnie\n\n" +
                    "Do Ciebie sam wracam znów dziś już wolny\n" +
                    "Chcę z Tobą żyć, chcę być tu, tu przy Tobie\n" +
                    "Przyjmij, teraz znów przyjmij mnie\n\n" +
                    "Refren:\n" +
                    "Ty nie przestajesz kochać nas\n" +
                    "Sprowadzasz wciąż z błędnych dróg\n" +
                    "Otwierasz drzwi, dajesz czas,\n" +
                    "Aby każdy z nas przyszłość swą wybrać mógł\n" +
                    "Nową przyszłość od dziś\n\n" +
                    "Przyjmij, teraz znów przyjmij mnie\n\n" +
                    "Refren\n\n" +
                    "Bridge:\n" +
                    "Odradzam się, odradzam się\n" +
                    "i mogę żyć, cieszyć się tańczyć x2\n\n" +
                    "(instrumental)\n\n" +
                    "Bridge x4",
            "O tak, tak, tak Panie mówię tak Twemu słowu\n" +
                    "O tak, tak, tak Panie mówię tak Twojej woli\n" +
                    "O tak, tak, tak Panie mówię tak Twym natchnieniom\n" +
                    "O tak, tak, tak Panie mówię tak Twemu prawu\n\n" +
                    "Jesteś mym Pasterzem, uczysz mnie jak tutaj żyć\n" +
                    "Twoje napomnienia chronią mnie, strzegą mnie, dają życie mi\n" +
                    "O Alleluja, Alleluja\n\n" +
                    "Pan jest wśród nas, prawdziwie jest wśród nas\n" +
                    "Pan jest wśród nas, widzę Go /2x/\n\n" +
                    "Kto zmartwychwstał i króluje?\n" +
                    "Jezus, Jezus!\n" +
                    "Kto jest tutaj by nam służyć?\n" +
                    "Jezus, Jezus!\n\n" +
                    "Pan zmartwychwstały,\n" +
                    "wspaniały nasz Przyjaciel,\n" +
                    "Chrystus Emmanuel tutaj jest! /2x/\n\n",
            "Jezus Chrystus Panem jest\n" +
                    "Król to królów, panów Pan\n" +
                    "Cała ziemia Jego jest\n" +
                    "po najdalszy świata kres\n\n" +
                    "Jezus, królów Król\n" +
                    "Jezus, świata Pan\n\n" +
                    "Świata Pan x4\n\n" +
                    "Alleluja x3 o Alleluja",
            "Dobro wypełnia tej rzeki bieg\n" +
                    "Każdy mój smutek w jej źródle topi się\n" +
                    "Ocean łaski - głębszy niż strach\n" +
                    "Niech się rozlewa, rośnie\n\n" +
                    "W środku tej rzeki moc objawia się\n" +
                    "z Bożego serca wciąż wylewa się\n" +
                    "niebieska łaska na nas spływa w dół\n" +
                    "Niech się rozlewa rośnie\n\n" +
                    "Wzbierają wody, wzbierają rzeki\n" +
                    "Zdrój wody Twej wylewa się /x2\n\n" +
                    "Ta rzeka daje nam życie /x4\n\n" +
                    "BRIDGE:" +
                    "Otwieraj więźniom drzwi\n" +
                    "Wypuszczaj wolno ich\n" +
                    "niech tryska moc, niech tryska moc\n" +
                    "niech we mnie budzi się\n\n" +
                    "Nic nie zatrzyma mnie\n" +
                    "w radości tańczyć chcę\n" +
                    "niech tryska moc, niech tryska moc\n" +
                    "niech we mnie budzi się\n\n",
            "Zwrotka 1:\n" +
                    "Wzywam Cię, Duchu, przyjdź\n" +
                    "Czekam wciąż, byś dotknął nas\n" +
                    "Wołam Cię, Panie, przyjdź\n" +
                    "Jezu, Zbawco, do dzieci Twych\n\n" +
                    "Refren:\n" +
                    "Jak spragniona ziemia rosy dusza ma\n" +
                    "Tylko Ty możesz wypełnić\n" +
                    "Serca głód, serca głód\n\n" +
                    "Zwrotka 2:\n" +
                    "Głębio morz, potęgo gór,\n" +
                    "Boże mój, nie mogę bez\n" +
                    "Twej miłości żyć\n" +
                    "Nie chcę bez Ciebie żyć\n\n" +
                    "Ref. Jak spragniona ziemia...\n\n",
            "Jezu jesteś tu,\n" +
                    "świat odszedł w cień\n" +
                    "Nie mam już nic\n" +
                    "Moje życie to Ty!\n\n" +
                    "Każdy dzień Twoim darem\n" +
                    "Nie przestanę wielbić Cię\n\n" +
                    "Ref:\n" +
                    "Chwała! Chwała!\n" +
                    "Jezu wielbię Cię\n\n",
            "Duchu Święty przyjdź\n\n" +
                    "REFREN:\n" +
                    "Tylko Ty jesteś drogą\n" +
                    "Tylko Ty jesteś prawdą\n" +
                    "Tylko Ty jesteś życiem\n" +
                    "Wypełnij nasze serca\n\n" +
                    "OUTRO:\n" +
                    "Duchu miłości, przemieniaj to co stare\n" +
                    "Tchnij nowe życie w to co jest umarłe\n\n" +
                    "Ożyw nas\n" +
                    "Przemień nas\n\n",
            "Refren:\n" +
                    "Oooo\n\n" +
                    "Zwrotka 1:\n" +
                    "Ty Panie mnie znasz\n" +
                    "Ty widzisz wszystko to co robię\n" +
                    "Bez wahania biegnę tam\n" +
                    "Tam gdzie Twe Królestwo,\n" +
                    "Tam gdzie Twe Królestwo\n\n" +
                    "Ty mnie znasz\n" +
                    "Ty widzisz wszystko to co robię\n" +
                    "Bez wahania biegnę tam\n" +
                    "Tam gdzie Twe Królestwo,\n" +
                    "Tam gdzie Twe Królestwo\n\n" +
                    "Refren\n\n" +
                    "Zwrotka 2:\n" +
                    "Nic nie jest jak Ty\n" +
                    "Żadne bogactwa tego świata\n" +
                    "Jedyne dobro widzę tam\n" +
                    "Tam gdzie Twe Królestwo,\n" +
                    "Tam gdzie Twe Królestwo x2\n\n" +
                    "Refren\n\n" +
                    "Outro:\n" +
                    "Tylko w Tobie jest potęga\n" +
                    "Tylko w Tobie moja siła\n\n",
            "Zaprowadź mnie tam, skąd powrotu nie ma\n" +
                    "gdzie ustaje wiara, spełnia się nadzieja\n" +
                    "Gdzie światłością pachnie każdy skrawek nieba\n" +
                    "bo ją na swój obraz miłość wylewa\n\n",
            "Ubi Caritas et amor\nUbi Caritas Deus ibi est\n\n"
        )
    }
}
