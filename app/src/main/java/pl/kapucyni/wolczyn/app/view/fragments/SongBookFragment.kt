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
            "Święty",
            "Każdy spragniony",
            "Kosmos",
            "Rzeka",
            "O tak tak / Pan jest wśród nas",
            "Jesteś, który jesteś",
            "Cała Chwała",
            "27 (CSM)",
            "Twoja miłość",
            "Oceany",
            "Każdy dzień upewnia mnie",
            "Otaczasz mnie dobrem",
            "Dom Pana",
            "Przed tronem Twym",
            "Pan jest pasterzem moim",
            "Jezus Chrystus Panem jest",
            "W Tobie jest światło",
            "Ciebie całą duszą pragnę",
            "Przyjdź i zajmij miejsce swe",
            "Wzywam Cię, Duchu przyjdź",
            "Rozmowa",
            // stare:
            "Cały mój świat",
            "Hosanna (mam moc)",
            "Nowe serce",
            "Powiedz tylko słowo",
            "Serce do Serca",
            "Wszystkie pragnienia",
            "Hizop",
            "Tak bowiem Bóg",
            "Kim jesteś Ty, Panie",
            "Ukaż mi Panie swą twarz",
            "Naucz nas",
            "Zawitaj Ukrzyżowany",
            "Jaśnieje Krzyż Chwalebny",
            "Podnieś mnie Jezu",
            "Powstań i żyj",
            "On miłością jest",
            "W Chrystusie mym",
            "Zmiłuj się nade mną Boże",
            "Odradzam się",
            "Jezu jesteś tu",
            "Duchu Święty przyjdź",
            "Królestwo",
            "Zaprowadź mnie tam",
            "Ubi Caritas"
        )

        val songTexts = arrayOf(
            "",
            "Zwrotka:\nWszystko czego chcę to Ty i Twoja łaska\nWszystko czego chcę to Ty x2\n\n" +
                    "Refren:\nJesteś święty, nieskończony, potężny Pan\n" +
                    "Twoja łaska wiecznie trwa, dla Ciebie wszystko możliwe jest x2\n\n" +
                    "Zwrotka\n\nRef. Jesteś święty...\n\n" +
                    "Bridge:\nI raduje się moje serce,\nże przyszedłeś tu na ziemię,\n" +
                    "żeby dać mi życie wieczne,\nuwielbiam Ciebie Panie x2\n\n" +
                    "Ref. Jesteś święty...\n\n" +
                    "Outro:\nJesteś Święty! x6\n\n",
            "Zwrotka:\nKażdy spragniony i słaby dziś\nNiech przyjdzie do źródła\nW Wodzie Życia zanurzy się\n" +
                    "Ból i cierpienie niech odpłyną w dal\nW morzu miłości serca uleczy dzisiaj Pan\n\n" +
                    "Ref. 1: Panie Jezu przyjdź\n\nZwrotka\n\nRef. 2: Duchu Święty przyjdź\n\n" +
                    "Outro:\nPanie Jezu, Panie Jezu, Panie Jezu przyjdź!\n" +
                    "Duchu Święty, Duchu Święty, Duchu Święty przyjdź\n\n",
            "1. Jak to możliwe jest,\nże Ty jeden umiesz kochać mnie za darmo,\nkochać za darmo\n" +
                    "Nie odrzucasz mnie\nMimo błędów każdy dzień to nowa szansa\nto nowa szansa\n\n" +
                    "Ref. Twoja miłość to kosmos\nniezmierzona tajemnica\n" +
                    "Twoja miłość to kosmos\nnieustannie mnie zachwyca\nTwoja miłość to kosmos x3\n\n" +
                    "2. W lustrze widzę Cię\nTwe odbicie patrzy na mnie z przebaczeniem\nz przebaczeniem\n" +
                    "Nie oceniasz mnie\nw Twoich oczach najważniejsza jest miłość\nliczy się miłość\n\n" +
                    "Refren x2\n\n",
            "1. Dobro wypełnia tej rzeki bieg\nKażdy mój smutek w jej źródle topi się\n" +
                    "Ocean łaski - głębszy niż strach\nNiech się rozlewa, rośnie\n\n" +
                    "2. W środku tej rzeki moc objawia się\nz Bożego serca wciąż wylewa się\n" +
                    "niebieska łaska na nas spływa w dół\nNiech się rozlewa rośnie\n\n" +
                    "Wzbierają wody, wzbierają rzeki\nZdrój wody Twej wylewa się /x2\n\n" +
                    "Ref. Ta rzeka daje nam życie /x4\n\n" +
                    "Bridge:Otwieraj więźniom drzwi\nWypuszczaj wolno ich\n" +
                    "niech tryska moc, niech tryska moc\nniech we mnie budzi się\n\n" +
                    "Nic nie zatrzyma mnie\nw radości tańczyć chcę\n" +
                    "niech tryska moc, niech tryska moc\nniech we mnie budzi się\n\n",
            "O tak, tak, tak Panie mówię tak Twemu słowu\n" +
                    "O tak, tak, tak Panie mówię tak Twojej woli\n" +
                    "O tak, tak, tak Panie mówię tak Twym natchnieniom\n" +
                    "O tak, tak, tak Panie mówię tak Twemu prawu\n\n" +
                    "Jesteś mym Pasterzem, uczysz mnie jak tutaj żyć\n" +
                    "Twoje napomnienia chronią mnie, strzegą mnie, dają życie mi\n" +
                    "O Alleluja, Alleluja\n\nPan jest wśród nas, prawdziwie jest wśród nas\n" +
                    "Pan jest wśród nas, widzę Go /2x/\n\nKto zmartwychwstał i króluje?\n" +
                    "Jezus, Jezus!\nKto jest tutaj by nam służyć?\nJezus, Jezus!\n\n" +
                    "Pan zmartwychwstały,\nwspaniały nasz Przyjaciel,\nChrystus Emmanuel tutaj jest! /2x/\n\n",
            "1. Niezmienny\nBoże nadziei\nKtóry przyszłość znasz\n\n" +
                    "Będziemy\nUfać Ci zawsze\nTy prowadzisz nas\n\n" +
                    "Boże naszych ojców\nTwoje Imię trwa\nZawsze wierny\nOkazujesz się nam x2\n\n" +
                    "2. Na przekór\nŚwiatu będziemy\nW tej nadziei stać\n\n" +
                    "Ty jeden\nWiesz co najlepsze jest\nBądź wola Twa\n\nRefren\n\n" +
                    "Bridge:\nJesteś który Jesteś\nAlfa i Omega\nLew i Baranek\nJesteś, Jesteś x4\n\nRefren x2\n\n",
            "Zwrotka 1:\n Kto we władaniu ma\nsłońce i każdą z planet\nkto poznał głębię mórz\nwidział nieba kres\n\n" +
                    "Kto może radę dać\ntemu co jest mądrością\nJahwe\nJedyny Bóg\n\n" +
                    "Pre-chorus:\nCała chwała\nwszelka chwała Twoja jest\nCała ziemia\nNiech wyśpiewa chwały pieśń\n\n" +
                    "Refren:\nCała chwała\nwszelka chwała Twoja jest\nCała ziemia\nNiech wyśpiewa chwały pieśń\n\n" +
                    "Zwrotka 2:\nKimże jest człowiek przed\nJego potężnym tronem\nCzym są narody wobec potęgi tej\n" +
                    "Kto może równać się\nz Tym co nie ma sobie równych\nJahwe\nJedyny Bóg\n\n" +
                    "Pre-chorus\nRefren x2\n\nBridge:\nŚwięty x2\nJest nasz Pan\n\nPre-chorus\nRefren x2\n\n",
            "1. Szukam Twojej twarzy\nCałym sercem\nTy nie chowasz jej przede mną\nJesteś zawsze blisko mnie\n\n" +
                    "Wszystkie moje źródła\nNieskończone\nWszystko czego potrzebuję\nNawet więcej w Tobie mam\n\n" +
                    "Pre-Chorus:\nI choćby drżało mi serce\nChoćbym się lękał\nTym sercem Tobie zaufam\n\n" +
                    "Ref. Rozjaśnij oblicze swe\nPoprowadź mnie tam, gdzie chcesz x2\n\n" +
                    "2. Jedno czego pragnę\nO czym myślę\nTo poznawać Ciebie bliżej\nW obecności Twojej żyć\n\n" +
                    "Ufać bezgranicznie\nTwemu Słowu\nŻe mnie nigdy nie pominiesz\nKiedy wzywam Imię Twe\n\n" +
                    "Pre-Chorus\n\nRefren\n\n" +
                    "Bridge:\nNie będę się bał\nW najciemniejszą noc\nZe mną mój Pan\n" +
                    "Na pewno to wiem\nŻe zobaczę Cię x4\n\nRef. Rojaśnij oblicze swe...\n\n",
            "Zwrotka:\nJesteś blisko mnie\nTęsknie za Duchem Twym\nKocham kroki Twe\n" +
                    "Wiem jak pukasz do drzwi\nPrzychodzisz jak ciepły wiatr\nOtwieram się i czuję znów że\n\n" +
                    "Refren:\nTwoja miłość jak ciepły deszcz\nTwoja miłość jak morze gwiazd za dnia\n" +
                    "Twoja miłość sprawia że,\nNieskończenie dobry Święty Duch\nOgarnia mnie.\n\n",
            "Zwrotka 1:\nZnad oceanów do mnie wołasz\nGdzie każdy krok niepewny jest\n" +
                    "Ty jesteś tam, gdzie niewiadoma\nTam znajdę grunt dla wiary mej\n\n" +
                    "Refren:\nI będę wzywać imię Twe\nI ponad fale patrzeć chcę\nGdy burzą się,\n" +
                    "mej duszy pokój w Tobie jest\nGdzie pójdziesz Ty,\nja pójdę też\n\n" +
                    "Zwrotka 2:\nMój strach utonie w Twojej łasce\nBo ręka Twa, prowadzi mnie\n" +
                    "I Ty mnie nigdy nie zawiodłeś\nWiem teraz też nie zawiedziesz mnie\n\n" +
                    "Refren:\nI będę wzywać imię Twe\nI ponad fale patrzeć chcę\nGdy burzą się,\n" +
                    "mej duszy pokój w Tobie jest\nGdzie pójdziesz Ty,\nja pójdę też\n\n" +
                    "\n\nBridge:\nDuchu prowadź mnie, gdzie wiara nie ma granic\n" +
                    "Daj mi chodzić nad wodami\nGdziekolwiek mnie zabierzesz\nProwadź głębiej niż pójść mogą moje stopy\n" +
                    "Moja wiara się umocni\nW Twej obecności Boże\n\n" +
                    "Refren:\nI będę wzywać imię Twe\nI ponad fale patrzeć chcę\nGdy burzą się,\n" +
                    "mej duszy pokój w Tobie jest\nGdzie pójdziesz Ty,\nja pójdę też\n\n",
            "Zwrotka:\nKażdy dzień upewnia mnie\nPan w miłości wierny jest\nKtóż by inny mógł jak On\n" +
                    "wybaczać co dzień?\n\nBoże, wiem jak ranię Cię\nczęsto słabnę gubię sens\n" +
                    "jednak Ty podnosisz mnie\ni nadzieję dajesz mi\n\n" +
                    "Refren:\nNikt nie odbierze mi tego, co mam w Tobie x2\n" +
                    "Pełnię miłości, pełnię wolności, pełnię radości mam w Tobie... x2\n\n",
            "Zwrotka 1:\nTy mnie tak dokładnie znasz\nKażdą moją myśl\nŁaskę swą każdego dnia\nOkazujesz mi\nI wołasz głębiej\n\n" +
                    "Refren:\nCałym sercem ufam Ci\nCiebie kocham z całych sił\nOtaczasz mnie dobrem, otaczasz mnie dobrem\n" +
                    "Nigdy nie opuścisz mnie\nNigdy nie rozmyślisz się\nOtaczasz mnie dobrem, otaczasz mnie dobrem\n\n" +
                    "Zwrotka 2:\nGdy na sercu ciężko mi\nJesteś obok mnie\n" +
                    "Wszystkie drogi oświetlasz i\nMasz najlepszy plan\nJesteś zawsze wierny\n\n" +
                    "Refren\n\nBridge:\nBędę Ci śpiewać ile mam tchu, O Panie mój\n" +
                    "Uwielbiam Cię, uwielbiam Cię\nBędę Ci śpiewać ile mam tchu na zawsze już\nUwielbiam Cię, uwielbiam Cię\n\n",
            "Zwrotka 1:\nNasz Bóg godzien chwały jest\nTo Jemu śpiewamy pieśń\n" +
                    "On był jest i będzie zawsze taki sam\n\nNasz Bóg przeciął morze w pół\n" +
                    "I zburzył więzienia mur\nZwycięstwo w swojej dłoni ma\n\n" +
                    "Refren:\nDom Pana wypełnia dziś śpiew\nDom Pana wypełnia radosny śpiew\n" +
                    "Wielbijmy Go razem\nOddajmy Mu cześć\n\nDom Pana wypełnia dziś śpiew\n" +
                    "Nasz Zbawca z nami tutaj jest\nWielbijmy Go razem\nOddajmy Mu cześć\n\n" +
                    "Zwrotka 2:\nNasz Bóg ma potężną moc\nŚlepemu przywraca wzrok\n" +
                    "On z każdej trudności dobre wyjście zna\n\n" +
                    "Bo On za nas przelał krew\nPotem sam pokonał śmierć\nI nadal cuda czynić chce\n\n" +
                    "Refren\n\nBridge:\nByliśmy zgubieni\nOn odnalazł nas\nW grzechu więzieni\n" +
                    "On nam wolność dał\nDzisiaj zbawieni i wdzięczni\nPodnieśmy swój głos\nNiech dom Pana uwielbi Go\n\n",
            "Przed tronem Twym stoimy\nWpatrzeni w Twej miłości blask\nDo Ciebie Panie podobni\nStajemy się widząc Twą twarz\n\n" +
                    "Refren:\nChwała Twa wypełnia nas\nObecności Twojej blask\nGdy wielbimy Ciebie, wiem\nJesteś tu.\n\n" +
                    "Bridge:\nChwała, cześć\nMądrość, moc, błogosławieństwo\nNa wieki, na wieki\n\n",
            "Zwrotka:\nPan jest pasterzem moim\nniczego mi nie braknie\nna zielonych niwach pasie mnie,\n" +
                    "nad spokojne wody mnie prowadzi\ni duszę mą pokrzepia\ni wiedzie mnie ścieżkami sprawiedliwości swojej\n\n" +
                    "Refren:\nChoćbym nawet szedł ciemną doliną\nzła się nie ulęknę, boś Ty ze mną\n" +
                    "laska Twoja i kij Twój mnie pocieszają\n\nLaj la laj lalalalala laj\n\n",
            "Jezus Chrystus Panem jest\nKról to królów, panów Pan\nCała ziemia Jego jest\npo najdalszy świata kres\n\n" +
                    "Jezus, królów Król\nJezus, świata Pan\nŚwiata Pan x4\n\nAlleluja\n\n",
            "W Tobie jest światło\nKażdy mrok rozjaśni\nW Tobie jest życie\n" +
                    "Ono śmierć zwycięża\nUfam Tobie Miłosierny\nJezu wybaw nas\n\n",
            "1. Ciebie całą duszą pragnę\ni z tęsknotą oczekuję,\n" +
                    "Jak spękana, zeschła ziemia\nw czas posuchy wody łaknie\n\n" +
                    "Ref. Boże, jesteś moim Bogiem,\nCiebie z troską szukam. (2x)\n\n" +
                    "2. W Twej świątyni ujrzeć pragnę\nTwą potęgę, moc i chwałę,\n" +
                    "Bowiem Twoją miłość, Panie,\nBardziej cenię niźli życie.\n\n" +
                    "3. Póki tylko istnieć będę,\nPragnę Ciebie chwalić, Boże.\n" +
                    "Dusza moja pełna szczęścia,\nBędzie śpiewać Ci z radością.\n\n",
            "Zwrotka:\nPrzyjdź i zajmij miejsce swe na tronie naszych serc\nPrzyjdź i zajmij miejsce swe\n\n" +
                    "Refren:\nCiebie pragnie dusza moja\nW suchej ziemi pragnę Cię\n\n" +
                    "Bridge:\nPrzyjdź i zajmij, przyjdź i zajmij miejsce\n\n" +
                    "Outro:\nPrzyjdź i zajmij, przyjdź i zajmij miejsce swe\n\n",
            "1. Wzywam Cię, Duchu, przyjdź\nCzekam wciąż, byś dotknął nas\n" +
                    "Wołam Cię, Panie, przyjdź\nJezu, Zbawco, do dzieci Twych\n\n" +
                    "Ref. Jak spragniona ziemia rosy dusza ma\nTylko Ty możesz wypełnić\nSerca głód, serca głód\n\n" +
                    "2. Głębio morz, potęgo gór,\nBoże mój, nie mogę bez\n" +
                    "Twej miłości żyć\nNie chcę bez Ciebie żyć\n\n" +
                    "Ref. Jak spragniona ziemia...\n\n",
            "1. Przychodzę do Ciebie\nwiem, że czekasz na mnie zawsze, zawsze\n" +
                    "Wszystko rozumiesz\nzanim zacznę Ci cokolwiek tłumaczyć\n\n" +
                    "Pre-Chorus 1:\nGdzie ukryję się przed Tobą, O Panie\nSkoro wszystkie moje drogi są Ci znane\n\n" +
                    "Chorus:\nTy znasz mnie, znasz moje serce\ni dobrze wiesz, czego mi trzeba\n" +
                    "Chcę bliżej, być bliżej Ciebie\nrozmawiać o Twoich pragnieniach\n\n" +
                    "2. Przychodzę do Ciebie\nstaram się szukać Cię zawsze, zawsze\n" +
                    "Już się nie schowam\ngdy mnie znowu po imieniu zawołasz\n\n" +
                    "Pre-Chorus 2:\nMoim szczęściem jest być z Tobą, O Panie\nTo kim jestem dziś na nowo Ci oddaję\n\n" +
                    "Refren x2\n\n",
            // stare:
            "1. Jesteś moim domem, moim bezpieczeństwem\nJesteś wszystkim czego szukam\n" +
                    "Ty jesteś, jesteś\n\nOoo, Obecny\nOoo, Ty jesteś\nOoo, Niezmienny\n\n" +
                    "Refren:\nW Tobie cały mój świat\nTy jesteś moim ocaleniem\n" +
                    "W Tobie cały mój świat\n\nW Tobie cały mój świat\n" +
                    "Podnoszę ręce, by chwalić Ciebie\nW Tobie cały mój świat\n\n" +
                    "2. Jesteś moim niebem, moim ukojeniem\nJesteś wszystkim na co czekam\nTy jesteś, jesteś\n\n" +
                    "Ooo, Obecny\nOoo, Ty jesteś\nOoo, Niezmienny\n\n" +
                    "Ref. W Tobie cały mój świat...\n\n" +
                    "Bridge:\nCały mój świat x2\nW Tobie cały mój świat x4\n\n" +
                    "Ref. W Tobie cały mój świat...\n\n",
            "1. Niech słaby powie: „Mam moc”,\nBiedny wyzna: „Wszystko mam”,\n" +
                    "Ślepy mówi: „Widzę znów”, we mnie to uczynił Bóg\n\n" +
                    "Ref. Hosanna, Hosanna Barankowi, co siebie dał.\n" +
                    "Hosanna, Hosanna, Jezus zmarł i zmartwychwstał.\n\n" +
                    "2. Wejdę w rzekę, gdzie grzechy me\nZmywasz, Zbawco, swoją krwią.\n" +
                    "Z niebios miłość wylewa się, łaską swą ogarnij mnie.\n\n",
            "1. Zburzone mury, wokół pustego miasta\ntam gdzie płynęły rzeki, zaległy suche kości\n" +
                    "Poszukujmy, poszukujmy Jego głosu\npoznajmy Jego Słowo\n\n" +
                    "Ref. To co upadło Bóg podniesie jeszcze\n" +
                    "przywróci swoją chwałę, uleczy co złamane\nSpod gruzów pychy Pan wyciągnie nowe serce\n\n" +
                    "2. To nasze winy, zabrały nas w niewolę\nlecz teraz czas powrócić, nadeszło odkupienie\n" +
                    "Nasłuchujmy, nasłuchujmy, czy już wzywa\nby ruszyć nową drogą\n\n" +
                    "Ref. To co upadło...\n\n" +
                    "Bridge:\nSłyszę cichy szum, czuję lekki powiew\natmosfera się odmienia, idzie nowe\n\n" +
                    "Z nieba spada deszcz, z gór wypływa strumień\nsuche brzegi znów wypełnia, rzeka życia\n\n" +
                    "Ref. To co upadło... x2\n\n",
            "Zwrotka:\nChrystus Pan\nMesjasz Król\nNa drzewo krzyża poniósł ciężar mój\n" +
                    "Boży Syn Panów Pan\nPosłuszny Ojcu został całkiem sam\n\n" +
                    "Ref. Powiedz tylko słowo\nA będzie uzdrowiona dusza ma\nPowiedz tylko słowo\nPanie mój\n\n" +
                    "Bridge:\nUwielbiam Ciebie Jezu\nUwielbiam Ciebie Jezu\n\n",
            "1. Niech przylgnie serce moje\nDo Twego serca Panie\n" +
                    "Niech przylgnie serce moje\nDo Twego serca Panie mój\n\n" +
                    "Ref. Bo tylko w Tobie\nspełnienie me\nBo tylko w Tobie\nwszystko jest\n" +
                    "Bo tylko w Tobie\nwszystko czego pragnę\nBo tylko w Tobie\nżyć, umierać chcę.\n\n",
            "Zwrotka:\nWszystkie pragnienia mego serca\nOddaję w Twoje święte ręce\n" +
                    "Czekam z nadzieją, że je spełnisz\nZ Twoją wolą zgadzam się\n" +
                    "Twe drogi nie są mi już obce\nSłucham uważnie Twego głosu\n" +
                    "Tak bardzo cieszę się, że jesteś\nCieszę się, że jesteś blisko\n\n" +
                    "Refren:\nCieszę się, że jesteś\nCieszę się, że jesteś blisko x2\n\n" +
                    "Bridge:\nPrzyjdź do mnie Panie ze swoją radością\n" +
                    "Przyjdź do mnie Panie ze swoją miłością\nWypełnij mnie, wypełnij mnie\n\n",
            "Daleki bądź od strachu\nze mną nie masz się czym martwić\nja pokażę Ci jak wytrwać w mej miłości\n" +
                    "Twe słowo drogowskazem\nGdy nie mam siły\nWtedy mówię Pan jest większy\nwtedy mówię Pan jest większy\n\n" +
                    "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n" +
                    "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n\n" +
                    "Gdy patrzę na twe niebo\ni to co wysoko na nim\nzastanawiam się kim jestem\nw twoich oczach\n" +
                    "Wciąż szukam odpowiedzi\nGdy nie mam siły\nOtwórz moje oczy Panie\nOtwórz moje oczy Panie\n\n" +
                    "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n" +
                    "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n\n",
            "Tak bowiem Bóg umiłował świat,\nże Syna swego nam dał\nAby każdy kto w Niego wierzy\nmiał życie wieczne.\n",
            "Kim jesteś Ty Panie, a kim jestem ja?\nKim Ty? A kim ja?\n\n",
            "Ukaż mi Panie swą twarz\nDaj mi usłyszeć Twój głos\nBo słodki jest Twój głos\n" +
                    "i twarz pełna wdzięku\nUkaż mi Panie swą twarz\n\n",
            "1. Przychodzimy do Ciebie Panie\nprzynosimy Ci wszystko co mamy\n" +
                    "Otwórz nasze oczy na cuda,\nktórych nie dostrzegamy\n\n" +
                    "Przychodzimy do Ciebie Panie\nprzynosimy Ci wszystko co mamy\n" +
                    "Otwórz nasze serca na miłość,\nktórej nie dostrzegamy\n\n" +
                    "Naucz nas pragnąć Ciebie\nPrzebywać w Twej obecności\n" +
                    "Pozwól nam ujrzeć Twoją twarz\nDoświadczyć Twojej miłości\n\n(instrumental)\n\n" +
                    "2. Przychodzimy do Ciebie Panie\nprzynosimy Ci wszystko co mamy\n" +
                    "Otwórz nasze oczy na cuda,\nktórych nie dostrzegamy\n\n" +
                    "Naucz nas pragnąć Ciebie\nPrzebywać w Twej obecności\n" +
                    "Pozwól nam ujrzeć Twoją twarz\nDoświadczyć Twojej miłości\n\n" +
                    "Ref. Tylko Tobie chwała\nTobie chwała\n\nBridge:\nJezus\n" +
                    "Nasza pomoc jest w Imieniu Pana, który stworzył niebo i ziemię\n\n",
            "Zwrotka 1:\nZawitaj, Ukrzyżowany,\nJezu Chryste przez Twe rany.\n" +
                    "Królu na niebie, prosimy Ciebie,\nratuj nas w każdej potrzebie.\n\n" +
                    "Zwrotka 2:\nZawitaj, Ukrzyżowany,\ncałujem Twe święte rany;\n" +
                    "przebite ręce, nogi w Twej męce,\nmiejcież nas w swojej opiece.\n\n" +
                    "Zwrotka 3:\nZawitaj, Ukrzyżowany,\ncierniem ukoronowany;\n" +
                    "we czci i chwale zniszczony wcale\nw takiej koronie, zbolałe skronie,\n" +
                    "miejcież nas w swojej obronie.\n\n" +
                    "Bridge:\nSmutki i żale, w serca upale wynieścież nas ku swej chwale\n" +
                    "Rano w ramieniu, z niej Krwi strumieniu, pociągnij nas ku zbawieniu\n" +
                    "Za grzechy płaczę sercem Cię raczę, Krzyżem Twoim głowę znaczę.\n\n" +
                    "Outro:\nO Jezu, miłości zdroju, wzdycham do Twego pokoju!\nO Jezu, miłości zdroju\n\n",
            "Jaśnieje Krzyż chwalebny,\nUnosi ciało Pana,\nZaś On swej krwi strumieniem\n" +
                    "Obmywa nasze rany.\n\nZ miłości czystej dla nas\nPokorną stał się żertwą,\n" +
                    "Baranek święty wyrwał\nSwe owce z paszczy wilka.\n\nWykupił świat od klęski\n" +
                    "Przebitych rąk zapłatą,\nI własne tracąc życie,\nPowstrzymał pochód śmierci.\n\n" +
                    "Skrwawionym ostrzem gwoździa\nTę samą dłoń przeszyto,\nCo zmyła winę Pawła,\n" +
                    "Wydarła śmierci Piotra.\n\nO Drzewo Życiodajne,\nSzlachetne w swej słodyczy,\n" +
                    "Wszak zieleń twych gałęzi\nWydaje owoc nowy.\n\nTwa woń ma moc obudzić\n" +
                    "Wystygłe ciała zmarłych,\nPowrócą wnet do życia\nMieszkańcy kraju nocy.\n\n" +
                    "Pod liści twoich cieniem\nNie straszny czas upału,\nSłoneczny żar w południe\n" +
                    "I blask księżyca nocą.\n\nJaśniejesz zasadzone\nNad wody żywej zdrojem,\n" +
                    "I blask rozsiewasz wokół\nŚwieżością kwiecia zdobny.\n\nPośrodku twoich ramion,\n" +
                    "Gdzie winny krzew rozpięty,\nSpływają krwawe strugi\nCzerwienią słodką wina.\n\n",
            "Podnieś mnie Jezu i prowadź do Ojca x2\nZanurz mnie w wodzie Jego miłosierdzia\nAmen\n\n",
            "Zwrotka:\nPowstań i żyj, chociaż wokół mrok,\npowstań i żyj, dobro wielką ma moc,\n" +
                    "powstań i żyj, choć upadłeś nie raz,\nJezus doda Ci sił, On zmartwychwstał byś żył,\n" +
                    "Jezus doda Ci sił, On zmartwychwstał byś żył!\n\n" +
                    "Refren:\nIle trzeba łez, aby wrócić do Ciebie,\njak daleko oddalić się, by usłyszeć Twój szept,\n" +
                    "jak bardzo żałować, aby pękło to serce kamienne.\nIle trzeba łez, aby wrócić do Ciebie.\n\n" +
                    "Outro:\nPowstań i żyj, powstań i żyj\n\n",
            "Nasze życie nie jest łatwe\ngrzechem skażone każde jest\nJeśli w sercu Twym cierpienie\n" +
                    "Jezus jego ukojeniem\n\nPozwól Mu, by wziął co trudne\nkażdy grzech i to co brudne jest\n" +
                    "On jedynym Zbawicielem\nw Jego ranach Twoje odkupienie\n\n(instrumental)\n\n" +
                    "Nasze życie nie jest łatwe\ngrzechem skażone każde jest\nJeśli w sercu Twym cierpienie\n" +
                    "Jezus jego ukojeniem\n\nPozwól Mu, by wziął co trudne\nkażdy grzech i to co brudne jest\n" +
                    "On jedynym Zbawicielem jest\n\nJezus podniesie Cię\nJezus wybacza grzech\n" +
                    "Jezus pocieszy Cię, gdy Twoje serce płacze x2\n\nOn miłością jest\n\n",
            "Zwrotka 1:\nW Chrystusie mym nadzieję mam\nOn moim światłem, pieśnią mą\n" +
                    "On fundamentem, skałą mą\nOn mnie prowadzi w dzień i w noc\n\n" +
                    "Jak wielka moc w miłości tej,\nco daje pokój duszy mej\nPociesza mnie i wszystkim jest\n" +
                    "w Bożej miłości żyć dziś chcę\n\n(instrumental)\n\nZwrotka 1\n\n" +
                    "Zwrotka 2:\nCzłowiekiem stał się Chrystus Pan\nNiewinnym dzieckiem Bóg się stał\n" +
                    "Miłości sprawiedliwej dar\nWzgardzony Boży Syn nam dał\n\nNa krzyżu tym, gdzie Jezus zmarł,\n" +
                    "by za mnie swoje życie dać\nMój każdy grzech na siebie wziął\nW śmierci Chrystusa życie mam\n\n" +
                    "Na krzyżu tym, gdzie Jezus zmarł,\nby za mnie swoje życie dać x4\n\n" +
                    "Mój każdy grzech na siebie wziął\nW śmierci Chrystusa życie mam\n\n",
            "Zmiłuj się nade mną Boże\nulituj się nad grzechem mym\nłaską swą zgładź mą nieprawość\n" +
                    "i obmyj mnie z moich win\tx2\n\nSkruszonym sercem nie pogardzisz Panie\n" +
                    "Skruszonym sercem nie pogardzisz Panie\nSkruszonym sercem nie pogardzisz Panie\n" +
                    "Wiem, nie pogardzisz sercem mym\tx2\n\nOdnów we mnie serce czyste\n" +
                    "Daj mi moc swojego Ducha\tx6\n\nOutro:\nPanie daj, daj serce czyste\n" +
                    "poślij nam swojego Ducha\nPanie daj, daj serce czyste\ndaj mi moc, daj mi moc\n\n",
            "Zwrotka 1:\nDo Ciebie dziś wracam znów, tak jak wtedy\nKiedy przyjąłeś mnie po raz pierwszy\n" +
                    "Przyjmij, teraz znów przyjmij mnie\n\nDo Ciebie sam wracam znów dziś już wolny\n" +
                    "Chcę z Tobą żyć, chcę być tu, tu przy Tobie\nPrzyjmij, teraz znów przyjmij mnie\n\n" +
                    "Refren:\nTy nie przestajesz kochać nas\nSprowadzasz wciąż z błędnych dróg\n" +
                    "Otwierasz drzwi, dajesz czas,\nAby każdy z nas przyszłość swą wybrać mógł\n" +
                    "Nową przyszłość od dziś\n\nPrzyjmij, teraz znów przyjmij mnie\n\n" +
                    "Refren\n\nBridge:\nOdradzam się, odradzam się\n" +
                    "i mogę żyć, cieszyć się tańczyć x2\n\n(instrumental)\n\nBridge x4\n\n",
            "Jezu jesteś tu,\nświat odszedł w cień\nNie mam już nic\nMoje życie to Ty!\n\n" +
                    "Każdy dzień Twoim darem\nNie przestanę wielbić Cię\n\n" +
                    "Ref:\nChwała! Chwała!\nJezu wielbię Cię\n\n",
            "Duchu Święty przyjdź\n\nREFREN:\nTylko Ty jesteś drogą\nTylko Ty jesteś prawdą\n" +
                    "Tylko Ty jesteś życiem\nWypełnij nasze serca\n\n" +
                    "OUTRO:\nDuchu miłości, przemieniaj to co stare\n" +
                    "Tchnij nowe życie w to co jest umarłe\n\nOżyw nas\nPrzemień nas\n\n",
            "Ref. Oooo\n\n1. Ty Panie mnie znasz\nTy widzisz wszystko to co robię\n" +
                    "Bez wahania biegnę tam\nTam gdzie Twe Królestwo,\nTam gdzie Twe Królestwo\n\n" +
                    "Ty mnie znasz\nTy widzisz wszystko to co robię\nBez wahania biegnę tam\n" +
                    "Tam gdzie Twe Królestwo,\nTam gdzie Twe Królestwo\n\nRefren\n\n" +
                    "2. Nic nie jest jak Ty\nŻadne bogactwa tego świata\nJedyne dobro widzę tam\n" +
                    "Tam gdzie Twe Królestwo,\nTam gdzie Twe Królestwo x2\n\n" +
                    "Refren\n\nOutro:\nTylko w Tobie jest potęga\nTylko w Tobie moja siła\n\n",
            "Zaprowadź mnie tam, skąd powrotu nie ma\ngdzie ustaje wiara, spełnia się nadzieja\n" +
                    "Gdzie światłością pachnie każdy skrawek nieba\nbo ją na swój obraz miłość wylewa\n\n",
            "Ubi Caritas et amor\nUbi Caritas Deus ibi est\n\n"
        )
    }
}
