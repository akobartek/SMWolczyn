package pl.kapucyni.wolczyn.app.songbook.data.repository

import pl.kapucyni.wolczyn.app.songbook.domain.model.Song
import pl.kapucyni.wolczyn.app.songbook.domain.repository.SongBookRepository

class WolczynSongBookRepository : SongBookRepository {

    private val songs by lazy { songTitles.zip(songTexts).map { Song(it.first, it.second) } }

    override fun filterSongs(query: String): List<Song> =
        if (query.isBlank()) songs
        else songs.filter { it.isMatchingQuery(query) }

    private val songTitles = arrayOf(
        "HYMN",
        "Święty",
        "Kosmos",
        "Rzeka",
        "Jesteś, który jesteś",
        "Cała Chwała",
        "Otaczasz mnie dobrem",
        "Dom Pana",
        "Pan jest pasterzem moim",
        "Jezus Chrystus Panem jest",
        "Stare minęło",
        "Stoisz u naszych drzwi",
        "Widzę dom",
        "Nikt tak jak Ty",
        "Twoja miłość",
        "Woda życia",
        "Jak Mu nie wierzyć",
        "Groby zamienisz w ogrody",
        "Ucisz się",
        "Łania",
        "Podnieś mnie",
        "Psalm 131",
        "Świeży powiew",
        "Nie zabijaj",
        "Przebaczam Ci",
        "Zawitaj Ukrzyżowany",
        "Zaprowadź mnie tam",
        "Krzyżu Chrystusa",
        "W Tobie jest światło",
        "W Chrystusie mym",
        "Ukaż mi Panie swą twarz",
        "Zbliżam się w pokorze",
        "Witaj Pokarmie",
        "Dzięki Ci, Panie",
        "Ciebie całą duszą pragnę",
        // stare:
        "Każdy spragniony",
        "O tak tak / Pan jest wśród nas",
        "27 (CSM)",
        "Oceany",
        "Każdy dzień upewnia mnie",
        "Przed tronem Twym",
        "Przyjdź i zajmij miejsce swe",
        "Wzywam Cię, Duchu przyjdź",
        "Rozmowa",
        "Cały mój świat",
        "Hosanna (mam moc)",
        "Nowe serce",
        "Powiedz tylko słowo",
        "Serce do Serca",
        "Wszystkie pragnienia",
        "Hizop",
        "Tak bowiem Bóg",
        "Kim jesteś Ty, Panie",
        "Naucz nas",
        "Jaśnieje Krzyż Chwalebny",
        "Podnieś mnie Jezu",
        "Powstań i żyj",
        "On miłością jest",
        "Zmiłuj się nade mną Boże",
        "Odradzam się",
        "Jezu jesteś tu",
        "Duchu Święty przyjdź",
        "Królestwo",
        "Ubi Caritas"
    )

    private val songTexts = arrayOf(
        "Zwrotka:\nDziś jestem tu\nnie stoję sam\nczuję ten wiatr\n\nJESTEŚMY RAZEM\n\n" +
                "Nieważny trud \ngdy Słowo Twe znam \nnadzieję mam\n\nJESTEŚMY RAZEM\n\n" +
                "Łączy nas Bóg\nOn prowadzi mnie\nnie boję się\n\nJESTEŚMY RAZEM\n\n" +
                "Oddaję Mu\ncały ten czas\nniech widzi świat\n\nJESTEŚMY RAZEM\n\n" +
                "Refren:\nOooo To nie koniec, to początek\ndziś widzę nowy cel\n" +
                "Oooo To nie koniec, to początek\nnic nie zatrzyma mnie\nooo To nie koniec, to początek\n" +
                "od dzisiaj wiem już, że\nOooo każdy dzień to nowa szansa\n\n" +
                "Zwrotka\n\nRefren\n\n" +
                "Bridge:\nRadość jak rzeka rozlewa się\ngdy właśnie Ty jesteś obok mnie\n" +
                "W Tobie jest wartość, tak widzę ją\ndzisiaj już jestem sobą\n\nRefren\n",
        "Zwrotka:\nWszystko czego chcę to Ty i Twoja łaska\nWszystko czego chcę to Ty x2\n\n" +
                "Refren:\nJesteś święty, nieskończony, potężny Pan\n" +
                "Twoja łaska wiecznie trwa, dla Ciebie wszystko możliwe jest x2\n\n" +
                "Zwrotka\n\nRef. Jesteś święty...\n\n" +
                "Bridge:\nI raduje się moje serce,\nże przyszedłeś tu na ziemię,\n" +
                "żeby dać mi życie wieczne,\nuwielbiam Ciebie Panie x2\n\n" +
                "Ref. Jesteś święty...\n\n" +
                "Outro:\nJesteś Święty! x6\n",
        "1. Jak to możliwe jest,\nże Ty jeden umiesz kochać mnie za darmo,\nkochać za darmo\n" +
                "Nie odrzucasz mnie\nMimo błędów każdy dzień to nowa szansa\nto nowa szansa\n\n" +
                "Ref. Twoja miłość to kosmos\nniezmierzona tajemnica\n" +
                "Twoja miłość to kosmos\nnieustannie mnie zachwyca\nTwoja miłość to kosmos x3\n\n" +
                "2. W lustrze widzę Cię\nTwe odbicie patrzy na mnie z przebaczeniem\nz przebaczeniem\n" +
                "Nie oceniasz mnie\nw Twoich oczach najważniejsza jest miłość\nliczy się miłość\n\n" +
                "Refren x2\n",
        "1. Dobro wypełnia tej rzeki bieg\nKażdy mój smutek w jej źródle topi się\n" +
                "Ocean łaski - głębszy niż strach\nNiech się rozlewa, rośnie\n\n" +
                "2. W środku tej rzeki moc objawia się\nz Bożego serca wciąż wylewa się\n" +
                "niebieska łaska na nas spływa w dół\nNiech się rozlewa rośnie\n\n" +
                "Wzbierają wody, wzbierają rzeki\nZdrój wody Twej wylewa się /x2\n\n" +
                "Ref. Ta rzeka daje nam życie /x4\n\n" +
                "Bridge:Otwieraj więźniom drzwi\nWypuszczaj wolno ich\n" +
                "niech tryska moc, niech tryska moc\nniech we mnie budzi się\n\n" +
                "Nic nie zatrzyma mnie\nw radości tańczyć chcę\n" +
                "niech tryska moc, niech tryska moc\nniech we mnie budzi się\n",
        "1. Niezmienny\nBoże nadziei\nKtóry przyszłość znasz\n\n" +
                "Będziemy\nUfać Ci zawsze\nTy prowadzisz nas\n\n" +
                "Boże naszych ojców\nTwoje Imię trwa\nZawsze wierny\nOkazujesz się nam x2\n\n" +
                "2. Na przekór\nŚwiatu będziemy\nW tej nadziei stać\n\n" +
                "Ty jeden\nWiesz co najlepsze jest\nBądź wola Twa\n\nRefren\n\n" +
                "Bridge:\nJesteś który Jesteś\nAlfa i Omega\nLew i Baranek\nJesteś, Jesteś x4\n\nRefren x2\n",
        "Zwrotka 1:\n Kto we władaniu ma\nsłońce i każdą z planet\nkto poznał głębię mórz\nwidział nieba kres\n\n" +
                "Kto może radę dać\ntemu co jest mądrością\nJahwe\nJedyny Bóg\n\n" +
                "Pre-chorus:\nCała chwała\nwszelka chwała Twoja jest\nCała ziemia\nNiech wyśpiewa chwały pieśń\n\n" +
                "Refren:\nCała chwała\nwszelka chwała Twoja jest\nCała ziemia\nNiech wyśpiewa chwały pieśń\n\n" +
                "Zwrotka 2:\nKimże jest człowiek przed\nJego potężnym tronem\nCzym są narody wobec potęgi tej\n" +
                "Kto może równać się\nz Tym co nie ma sobie równych\nJahwe\nJedyny Bóg\n\n" +
                "Pre-chorus\nRefren x2\n\nBridge:\nŚwięty x2\nJest nasz Pan\n\nPre-chorus\nRefren x2\n",
        "Zwrotka 1:\nTy mnie tak dokładnie znasz\nKażdą moją myśl\nŁaskę swą każdego dnia\nOkazujesz mi\nI wołasz głębiej\n\n" +
                "Refren:\nCałym sercem ufam Ci\nCiebie kocham z całych sił\nOtaczasz mnie dobrem, otaczasz mnie dobrem\n" +
                "Nigdy nie opuścisz mnie\nNigdy nie rozmyślisz się\nOtaczasz mnie dobrem, otaczasz mnie dobrem\n\n" +
                "Zwrotka 2:\nGdy na sercu ciężko mi\nJesteś obok mnie\n" +
                "Wszystkie drogi oświetlasz i\nMasz najlepszy plan\nJesteś zawsze wierny\n\n" +
                "Refren\n\nBridge:\nBędę Ci śpiewać ile mam tchu, O Panie mój\n" +
                "Uwielbiam Cię, uwielbiam Cię\nBędę Ci śpiewać ile mam tchu na zawsze już\nUwielbiam Cię, uwielbiam Cię\n",
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
                "On nam wolność dał\nDzisiaj zbawieni i wdzięczni\nPodnieśmy swój głos\nNiech dom Pana uwielbi Go\n",
        "Zwrotka:\nPan jest pasterzem moim\nniczego mi nie braknie\nna zielonych niwach pasie mnie,\n" +
                "nad spokojne wody mnie prowadzi\ni duszę mą pokrzepia\ni wiedzie mnie ścieżkami sprawiedliwości swojej\n\n" +
                "Refren:\nChoćbym nawet szedł ciemną doliną\nzła się nie ulęknę, boś Ty ze mną\n" +
                "laska Twoja i kij Twój mnie pocieszają\n\nLaj la laj lalalalala laj\n",
        "Jezus Chrystus Panem jest\nKról to królów, panów Pan\nCała ziemia Jego jest\npo najdalszy świata kres\n\n" +
                "Jezus, królów Król\nJezus, świata Pan\nŚwiata Pan x4\n\nAlleluja\n",
        "Byłem umarły, teraz żyję\nNic nie widziałem, teraz widzę\nZmieniłeś moje życie\n" +
                "Zupełny akcji zwrot\nIdę za Tobą i nie cofnę się o krok\n\n" +
                "Refren:\nStare minęło\nTo więcej niż pewne\nJakie to szczęście\nByć nowym stworzeniem\n" +
                "To już nie ja\nTo już nie ja\nNie, nie ja\nTy żyjesz we mnie\n\n" +
                "Ty pokazałeś czym jest miłość\nTy przebaczyłeś to co było\nDałeś mi czyste serce\n" +
                "Dzieckiem nazwałeś mnie\nTo dzięki Tobie lęku już nie boję się\n\n" +
                "Refren\n\nBridge:\nTaki dobry, sprawiedliwy (Ty żyjesz we mnie)\n" +
                "Zawsze wierny, tak cierpliwy (Ty żyjesz we mnie)\nTaki święty, doskonały (Ty żyjesz we mnie)\n" +
                "Kochający, tak wspaniały (Ty żyjesz we mnie)\nTaki dobry, sprawiedliwy (Ty żyjesz we mnie)\n" +
                "Zawsze wierny, tak cierpliwy (Ty żyjesz we mnie)\n\nRefren\n",
        "Stoisz u naszych drzwi, kołaczesz długo\nSłowa życia wiecznego masz\nKto Twój usłyszy głos i drzwi otworzy\n" +
                "będzie trwał w Tobie, a Ty w nim\nPanie, wielka jest miłość Twa,\n" +
                "tak cierpliwa, wierna i łaskawa.\nChcesz ją wnieść do naszych serc.\n\n" +
                "Refren:\nPanie, wejdź, zamieszkaj w nas\nPanie, Ty dar życia masz\n" +
                "Panie, przyjdź, umocnij nas\nbyśmy wytrwali w miłości Twej\n",
        "Zwrotka:\nWidzę dom, w nim otwarte drzwi\nOjciec czeka na mnie w nich\n" +
                "Woła mnie wiem, że mogę wejść\nOn opatrzy rany me\nWiele bitew za mną jest\n\n" +
                "Refren:\nTo czas by ucztować\nW sercu mam pokój\nGdy wokół wojna\n" +
                "Na oczach moich wrogów\nPan zastawia stół x2\n\nZwrotka\n\nRefren\n\n" +
                "Bridge:\nStrach nie ma wstępu do tego domu\nOskarżyciele nie mają głosu\n" +
                "Pan sam pokonał moich wrogów x4\nBędę dziękować\n" +
                "Będę dziękować\nBędę dziękować memu Bogu x2\n\nRefren\n",
        "Zwrotka:\nUkochany Syn\nBlaskiem chwały Ojca lśni\nTajemnicę odkrył wieczną\nEmmanuel\n" +
                "Pierworodny Syn\nW nasze miejsce przyjął krzyż\nPrzez ofiarę nas pojednał\nMocą swojej krwi\n\n" +
                "Pre-chorus:\nKiedy Jezu, widzę Cię\nTo do domu pragnę biec\n" +
                "Poprzez bramę, wąskie drzwi\nW bliskość Ojca wtulić się\n\n" +
                "Refren:\nNikt tak jak Ty nie przyjął mnie\nNikt tak jak Ty nie kochał mnie\n" +
                "Jezu nie ma równych Tobie\nNikt tak jak Ty\n\nZwrotka\n\nPre-chorus\n\nRefren x2\n\n" +
                "Bridge:\nJezus jest moją drogą\nJezus jest moją bramą\n" +
                "Jezus, On sam zastawia dla mnie stół x4\n\nRefren x2\n",
        "Zwrotka:\nJesteś blisko mnie\nTęsknie za Duchem Twym\nKocham kroki Twe\n" +
                "Wiem jak pukasz do drzwi\nPrzychodzisz jak ciepły wiatr\nOtwieram się i czuję znów że\n\n" +
                "Refren:\nTwoja miłość jak ciepły deszcz\nTwoja miłość jak morze gwiazd za dnia\n" +
                "Twoja miłość sprawia że,\nNieskończenie dobry Święty Duch\nOgarnia mnie.\n",
        "Ty dajesz pragnącemu wodę życia\nI swoim chlebem co dzień karmisz mnie\n" +
                "Twe słowo to jedyne, co nasyca\nPoznawać Ciebie to mój życia sens\n\n" +
                "Bezcenną wodę dałeś mi\nOżywcze źródło bije w moim sercu\n" +
                "Pokarmem duszy jesteś Ty\nI to wystarczy mi\nTy wystarczysz mi\n\n" +
                "Nie muszę dłużej szukać ukojenia\nNie łaknę już, jest we mnie inny głód\n" +
                "To, czego pragnę dziś, to skarby Nieba\nCodziennie potrzebuję Twoich słów\n\n" +
                "Bezcenną wodę dałeś mi\nOżywcze źródło bije w moim sercu\n" +
                "Pokarmem duszy jesteś Ty\nI to wystarczy mi\nWodo życia płyń\n\n" +
                "Wodo życia płyń x8\nDuchu Święty wołam\nPrzenikaj moje wnętrze\n" +
                "Życiodajna rzeka\nRaduje Boże serce\nWodo życia płyń x5\n",
        "Jak mu nie wierzyć\nJak mu nie ufać\nW życiu nie zawiódł nigdy mnie\nNa rękach nosił\n" +
                "Szanował każdy mój krok\nPrzecierał mi szlak i karczował lasy, bym widział cel\n\n" +
                "Jak mu nie wierzyć\nJak mu nie ufać\nW bólu i smutku ze mną był\nPocieszał z krzyża\n" +
                "Ukradkiem liczył me łzy\nUwalniał od zła i wyprawiał ucztę gdy chciałem przyjść\n\n" +
                "Refren:\nOjcze, wierzę Ci,\nChryste, ufam Tobie,\n" +
                "Duchu Święty, przymnażaj wiary, by nie ustała. /x2\n\n" +
                "Jak mu nie wierzyć\nJak mu nie ufać\nSkoro z miłości zbawił mnie\nZostawił ziarno\n" +
                "I mocno podlał je krwią\nNasienie na siew niech zapada w duszę i daje plon\n\nRefren\n\n" +
                "Wielki wielki jest nasz Pan x2\nWierny, wierny jest nasz Pan\nBliski, bliski jest nasz Pan\n\n" +
                "Prowadź mnie drogą, którą jesteś\n\n" +
                "Ty jesteś Jedyną drogą, Jedyną prawdą, Jedynym życiem, którego szukam\n\nOdnajdujesz mnie na czas\n",
        "Cały ten świat\nnie dał mi spełnienia\nziemski mój skarb\nniewiele jest wart\nprzeminie jak wiatr\n\n" +
                "Lecz odkąd Cię znam\nżyję już na nowo\nnie trzeba mi nic\n" +
                "bo kochasz mnie Ty\nwięc wszystko już mam\n\n" +
                "Refren:\nWiem, że nie ma nikogo jak Ty\nO, nie ma nikogo jak Ty\n" +
                "Panie, nie ma\nNie ma nikogo jak Ty\n\n" +
                "Nie muszę się bać\nże widzisz moje błędy\nbo dobrze mnie znasz i mimo mych wad\n" +
                "przyjacielem chcesz być\n\nI gdziekolwiek się znajdę\nna górze czy w dolinie\n" +
                "pewność mam, że w każdym z tych miejsc\nTwoja łaska już jest\n\nRefren\n\n" +
                "Bridge:\nTy zamieniasz łzy w taniec\ntworzysz piękno z popiołów\nwstyd zamieniasz na chwałę\n" +
                "tylko Ty potrafisz tak x2\n\nGroby zmieniasz w ogrody\nsuche kości na armie\n" +
                "tworzysz drogę przez morze\ntylko Ty potrafisz tak\ntylko Ty potrafisz tak\n\nRefren\n\nBridge\n",
        "Jak wzburzone morze\nMyśli w mojej głowie\nNie wiem jak mam stać wśród tych fal\n" +
                "Widzę Cię przez mgłę\nStoisz tak spokojnie\nWołam Cię nie pozwól mi spaść\n\n" +
                "Ucisz mnie\nPowiedz coś\nJednym słowem swym masz moc\nZakończyć sztorm\n" +
                "Uchwyć mnie\nUkryj w myśli swej\nZniwecz każdą ciemną myśl\nŚwiatłem swym\n\n" +
                "Patrzę w Twoje oczy\nGroźne i łagodne\nNic nie może przerazić cię\n" +
                "Idę w Twoją stronę\nA Ty idziesz do mnie\nWznosisz głos i uśmiechasz się\n\n" +
                "Ucisz się\nJa jestem\nW zaufaniu Twym jest moc\nW ciszy Twej\n" +
                "Nie bój się\nPrzecież jesteś mój\nMożesz ufać mi i iść\nMimo burz\n\n" +
                "Ucisz się\nJa jestem\nW zaufaniu Twym jest moc\nW ciszy Twej\n" +
                "Nie bój się\nMam Cię w rękach swych\nMożesz ufać mi i iść\nBędę tu\n\n" +
                "Wokół wielka cisza\nGdzie są przeciwnicy\nUstąpiły burze i wiatr\n" +
                "Tu na szklanym morzu\nW tęczy Twojej chwały\nStoję i nie muszę się bać\n",
        "Jak łania pragnie wody ze strumieni\ntak dusza moja pragnie Ciebie Boże\ndusza moja pragnie Ciebie, pragnie\n\n" +
                "kiedyż więc przyjdę i ujrzę oblicze Boże?\nŁzy stały się dla mnie chlebem we dnie i w nocy,\n" +
                "dusza moja pragnie Ciebie, pragnie\n\nCzemu zgnębiona jesteś, duszo moja,\n" +
                "Bogu zaufaj, bo jeszcze będziesz Go wysławiać\ndusza moja pragnie Ciebie, pragnie\n",
        "Kiedy dom mojego serca leży w gruzach\nGdy nie widzę, gdy nie czuję\nA w modlitwie nie mam nic do powiedzenia\n" +
                "Gdy nie ufam, gdy nie wierzę\n\nChoć tak trudno mi, to chce trwać\n\n" +
                "Refren:\nPodnieś mnie,\nbo bez Ciebie nie mam już sił\ndalej wierzyć i dalej iść\nNie pozwól odejść mi\n\n" +
                "Przenieś mnie\nswoją łaską abym mógł żyć\nI przypomnij kim jesteś Ty\nNie pozwól odejść mi\n\n" +
                "Daj mi poczuć bliskość Twojej obecności\nWiem, że jesteś, wiem, że słyszysz\n Twojej mocy Panie wszystko jest możliwe\n" +
                "Wiem, że czuwasz, wiem, że działasz\n\nChoć tak trudno mi, to chce trwać\n\nRefren\n\n" +
                "Bridge:\nTy widzisz więcej, wyciągam ręce\nCzekam na Twoje działanie\nNiech Twoja wola objawia się\n\n" +
                "Przywróć mi radość, umocnij ducha\nCzekam na Twoje działanie\nNiech Twoja wola objawia się\n",
        "Tato, moje serce się nie pyszni\noczy moje nie chcą patrzeć z góry\nza tym co jest wielkie nie chcę gonić\n" +
                "albo co przerasta moje siły\n\njak niemowlę u swej mamy\ntak kołyszesz moją duszę\n" +
                "Ład i spokój we mnie zaprowadzasz\ncała ma nadzieja tylko w Tobie\n\ncała ma nadzieja jest w Tobie\n",
        "Panie nasz przyjdź jak wiatr\nOgień swój ześlij nam\nDuchu Święty dotknij naszych serc\n" +
                "Gdy żałujemy naszych win\nPrzebudzenia żar się tli\nOddech Twój niech rozpali nas\n\n" +
                "Refren 1:\nJak świeży powiew, jak zapach nieba\nDuchu Święty przyjdź, Duchu Święty przyjdź\n\n" +
                "Niech w sercach płonie bojaźń Twa\nOczyszcza je każdego dnia\nOgień Twój niech umacnia nas\n" +
                "My, Twój kościół chcemy nieść\nŚwiatu wokół światło Twe\nPrzyjdź Królestwo Twe modlimy się\n\n" +
                "Refren 2:\nJak świeży powiew, jak zapach nieba\nDuchu Święty przyjdź, Duchu Święty przyjdź\n" +
                "Potrzebujemy Twej obecności\nDuchu Święty przyjdź, Duchu Święty przyjdź\n\nDuchu Święty przyjdź\n\n" +
                "Bridge:\nNiech popłynie pieśń z rozpalonych serc\nSłychać już, jak wiatr wieje, wieje, wieje\n" +
                "Niech uwielbi Cię Twoich dzieci śpiew\nSłychać już, jak wiatr wieje, wieje, wieje\n\nRefren\n",
        "Są słowa, które karmią mnie co dzień\ndodają sił, dodają skrzydeł\nte same słowa niosą mnie do chmur\n" +
                "dodają piór, unoszą w górę\n\nlecz znam słowa\nod których boli głowa\n" +
                "po których krwawi serce\nja nie chcę słyszeć ich więcej\n\n" +
                "Refren:\nNie zabijaj mnie\nTwe słowa ranią\nwciąż umierają we mnie ogrody\n" +
                "Nie zabijaj mnie\nbo słowa mają moc!\nSłowa mają moc!\n\n" +
                "Są słowa, które niosą życie\nuśmierzyć mogą każdy ból\nTy możesz też te słowa mieć\n" +
                "jak balsam wlać je na swoją duszę\n\nlecz znam słowa\nod których boli głowa\n" +
                "po których krwawi serce\nja nie chcę słyszeć ich więcej\n\nRefren\n\n" +
                "Słowa jak kamienie\nciągną mnie w dół\nostrzem rozdzierają mi serce x3\n\n" +
                "Słowa jak naboje\nna wylot ranią mnie\nzamilcz lepiej,\numierać nie chcę, nie!\n\nRefren\n",
        "Zapłonęło serce gniewem\npożar wzniecił się\npłoną myśli, płoną słowa\nnie powstrzymasz mnie\n\n" +
                "Czuję jak gniewu lawina\nwciąż mnie spycha w dół\nchoćbym chciała, nie ucieknę\n" +
                "poprzez wrzący mur\n\nMyślałam, że tak lepiej będzie mi\nlecz nagle moje serce skamieniało\n\n" +
                "Refren:\nPrzebaczam Ci\nproszę wybacz i Ty\nnigdy nie jest za późno\nby uwolnić się\n" +
                "Przebaczam Ci\nproszę wybacz i Ty\nniechaj miłość wypełni\nco zranione jest\n\n" +
                "Ciężkie serce i zbolałe\nuzdrowienia chce\nw takim braku przebaczenia\ntrwać już nie chce, nie\n\n" +
                "Czuję jak gęste opary\npochłaniają je\nchoćbym chciała, nie ucieknę\n" +
                "w klatce duszę się\n\nMyślałam, że tak wygodniej jest\nlecz nagle moje serce skamieniało\n\nRefren\n\n" +
                "Bridge:\nMiłosierdzie dało wolność mi\nPozwoliło bym mogła zacząć od nowa\n" +
                "Kiedyś On już raz przebaczył mi\ni teraz ja uczę się jak powiedzieć Ci że...\n\nRefren\n\n" +
                "Nie jest za późno, by wybaczyć\nNie jest za późno, by uwolnić się!\n",
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
                "Outro:\nO Jezu, miłości zdroju, wzdycham do Twego pokoju!\nO Jezu, miłości zdroju\n",
        "Zaprowadź mnie tam, skąd powrotu nie ma\ngdzie ustaje wiara, spełnia się nadzieja\n" +
                "Gdzie światłością pachnie każdy skrawek nieba\nbo ją na swój obraz miłość wylewa\n",
        "Krzyżu Chrystusa bądźże pochwalony\nNa wieczne czasy bądźże pozdrowiony\nGdzie Bóg Król świata całego\n" +
                "Dokonał życia swojego\n\nKrzyżu Chrystusa bądźże pochwalony\nNa wieczne czasy bądźże pozdrowiony\n" +
                "Ta sama Krew Cię skropiła\nKtóra nas z grzechów obmyła\n\nKrzyżu Chrystusa bądźże pochwalony\n" +
                "Na wieczne czasy bądźże pozdrowiony\nZ Ciebie moc płynie i męstwo\n Tobie jest nasze zwycięstwo\n",
        "W Tobie jest światło\nKażdy mrok rozjaśni\nW Tobie jest życie\n" +
                "Ono śmierć zwycięża\nUfam Tobie Miłosierny\nJezu wybaw nas\n",
        "Zwrotka 1:\nW Chrystusie mym nadzieję mam\nOn moim światłem, pieśnią mą\n" +
                "On fundamentem, skałą mą\nOn mnie prowadzi w dzień i w noc\n\n" +
                "Jak wielka moc w miłości tej,\nco daje pokój duszy mej\nPociesza mnie i wszystkim jest\n" +
                "w Bożej miłości żyć dziś chcę\n\n" +
                "Zwrotka 2:\nCzłowiekiem stał się Chrystus Pan\nNiewinnym dzieckiem Bóg się stał\n" +
                "Miłości sprawiedliwej dar\nWzgardzony Boży Syn nam dał\n\nNa krzyżu tym, gdzie Jezus zmarł,\n" +
                "by za mnie swoje życie dać\nMój każdy grzech na siebie wziął\nW śmierci Chrystusa życie mam\n",
        "Ukaż mi Panie swą twarz\nDaj mi usłyszeć Twój głos\nBo słodki jest Twój głos\n" +
                "i twarz pełna wdzięku\nUkaż mi Panie swą twarz\n",
        "1. Zbliżam się w pokorze i niskości swej,\nWielbię Twój majestat, skryty w Hostii tej,\n" +
                "Tobie dziś w ofierze serce daję swe,\nO, utwierdzaj w wierze, Jezu, dzieci Twe.\n\n" +
                "2. Mylą się, o Boże, w Tobie wzrok i smak,\nKto się im poddaje, temu wiary brak,\n" +
                "Ja jedynie wierzyć Twej nauce chcę,\nŻe w postaci chleba utaiłeś się.\n\n" +
                "3. Bóstwo swe na krzyżu skryłeś wobec nas,\nI ukryte z Bóstwem człowieczeństwo wraz,\n" +
                "Lecz w Oboje wierząc, wiem, że dojdę tam,\nGdzieś przygarnął łotra, do Twych niebios bram.\n\n" +
                "4. Jak niewierny Tomasz twych nie szukam ran,\nLecz wyznaję z wiarą, żeś mój Bóg i Pan,\n" +
                "Pomóż wierze mojej, Jezu, łaską swą,\nOżyw mą nadzieję, rozpal miłość mą.\n\n" +
                "5. Ty, coś upamiętnił śmierci Bożej czas,\nChlebie żywy, życiem swym darzący nas,\n" +
                "Spraw, bym dla swej duszy życie z Ciebie brał,\nBym nad wszelką słodycz Ciebie poznać chciał.\n\n" +
                "6. Ty, co jak Pelikan Krwią swą karmisz lud,\nPrzywróć mi niewinność, oddal grzechów brud,\n" +
                "Oczyść mnie Krwią swoją, która wszystkich nas\nJedną kroplą może obmyć z win i zmaz.\n\n" +
                "7. Pod zasłoną teraz, Jezu, widzę Cię,\nNiech pragnienie serca kiedyś spełni się,\n" +
                "Bym oblicze Twoje tam oglądać mógł,\nGdzie wybranym miejsce przygotował Bóg\n",
        "Witaj Pokarmie, w którym niezmierzony\nnieba i ziemie Twórca jest zamkniony.\n" +
                "Witaj Napoju zupełnie gaszący\numysł pragnący.\n\n" +
                "Witaj Krynico wszystkiego dobrego,\ngdy bowiem w sobie masz Boga samego,\n" +
                "znasz ludziom wszystkie Jego wszechmocności,\nniesiesz godności.\n\n" +
                "Witaj z Niebiosów Manno padająca\nrozkoszny w sercu naszym smak czyniąca.\n" +
                "Wszystko na świecie, co jedno smakuje,\nw tym się najduje.\n\n" +
                "Witaj rozkoszne z ogrodu rajskiego\nDrzewo owocu pełne żywiącego:\n" +
                "kto Cię skosztuje, śmierci się nie boi,\nchoć nad nim stoi.\n\n" +
                "Witaj jedyna serc ludzkich Radości,\nwitaj strapionych wszelka Łaskawości.\n" +
                "Ciebie dziś moje łzy słodkie szukają,\nk'Tobie wołają.\n",
        "Refren:\nDzięki Ci, Panie, za Ciało Twe i Krew,\nZa dary nieskończone wielbimy Cię. /x2\n\n" +
                "1. Chwalimy Cię, Wszechmocny, za dary Twe nieskończone,\nZa Ciało i Twoją Krew.\n" +
                "Przebacz w Swojej dobroci tym, co Ciebie niegodni,\nKiedy do Twego stołu się zbliżamy.\n\n" +
                "2. Przebacz, żeś hojny i wierny.\nUwolnij z więzów grzechu,\nByśmy się odmienili\nPrzez Tajemnice Najświętsze.\n\n" +
                "3. Niechaj przestworem spłynie z gwiazd Anioł Twój miły,\nOczyści i uleczy nasze serca i ciała.\n" +
                "Powiedzie, za sprawą tajemnicy, na same szczyty nieba,\nA tu, na ziemi, ratuje obroną Twoją potężną.\n\n" +
                "4. Spojrzyj łaskawie, Stwórco, na nas znikomych i słabych.\nOcal, Dobry Pasterzu, owce na Swojej łące.\n" +
                "Tyś życie nam przywrócił wbrew nieprzyjacielowi\nI wzmacniasz już na zawsze, Siebie dając nam, Panie.\n\n" +
                "5. Spraw to, Ojcze Wszechmocny, w dobroci swej niezmiernej,\nByśmy się stali jedno,\n" +
                "Z Tobą, Chrystusem i Duchem\nTy, coś w Trójcy jedyny.\n",
        "Refren:\nBoże, jesteś moim Bogiem,\nCiebie z troską szukam. (2x)\n\n" +
                "1. Ciebie całą duszą pragnę\ni z tęsknotą oczekuję,\n" +
                "Jak spękana, zeschła ziemia\nw czas posuchy wody łaknie\n\n" +
                "2. W Twej świątyni ujrzeć pragnę\nTwą potęgę, moc i chwałę,\n" +
                "Bowiem Twoją miłość, Panie,\nBardziej cenię niźli życie.\n\n" +
                "3. Póki tylko istnieć będę,\nPragnę Ciebie chwalić, Boże.\n" +
                "Dusza moja pełna szczęścia,\nBędzie śpiewać Ci z radością.\n\n" +
                "4. Jesteś mym wspomożycielem,\ncień Twych skrzydeł daje radość,\n" +
                "całym sercem lgnę do Ciebie,\nTwa prawica mnie prowadzi.\n",
        // stare:
        "Zwrotka:\nKażdy spragniony i słaby dziś\nNiech przyjdzie do źródła\nW Wodzie Życia zanurzy się\n" +
                "Ból i cierpienie niech odpłyną w dal\nW morzu miłości serca uleczy dzisiaj Pan\n\n" +
                "Ref. 1: Panie Jezu przyjdź\n\nZwrotka\n\nRef. 2: Duchu Święty przyjdź\n\n" +
                "Outro:\nPanie Jezu, Panie Jezu, Panie Jezu przyjdź!\n" +
                "Duchu Święty, Duchu Święty, Duchu Święty przyjdź\n",
        "O tak, tak, tak Panie mówię tak Twemu słowu\n" +
                "O tak, tak, tak Panie mówię tak Twojej woli\n" +
                "O tak, tak, tak Panie mówię tak Twym natchnieniom\n" +
                "O tak, tak, tak Panie mówię tak Twemu prawu\n\n" +
                "Jesteś mym Pasterzem, uczysz mnie jak tutaj żyć\n" +
                "Twoje napomnienia chronią mnie, strzegą mnie, dają życie mi\n" +
                "O Alleluja, Alleluja\n\nPan jest wśród nas, prawdziwie jest wśród nas\n" +
                "Pan jest wśród nas, widzę Go /2x/\n\nKto zmartwychwstał i króluje?\n" +
                "Jezus, Jezus!\nKto jest tutaj by nam służyć?\nJezus, Jezus!\n\n" +
                "Pan zmartwychwstały,\nwspaniały nasz Przyjaciel,\nChrystus Emmanuel tutaj jest! /2x/\n",
        "1. Szukam Twojej twarzy\nCałym sercem\nTy nie chowasz jej przede mną\nJesteś zawsze blisko mnie\n\n" +
                "Wszystkie moje źródła\nNieskończone\nWszystko czego potrzebuję\nNawet więcej w Tobie mam\n\n" +
                "Pre-Chorus:\nI choćby drżało mi serce\nChoćbym się lękał\nTym sercem Tobie zaufam\n\n" +
                "Ref. Rozjaśnij oblicze swe\nPoprowadź mnie tam, gdzie chcesz x2\n\n" +
                "2. Jedno czego pragnę\nO czym myślę\nTo poznawać Ciebie bliżej\nW obecności Twojej żyć\n\n" +
                "Ufać bezgranicznie\nTwemu Słowu\nŻe mnie nigdy nie pominiesz\nKiedy wzywam Imię Twe\n\n" +
                "Pre-Chorus\n\nRefren\n\n" +
                "Bridge:\nNie będę się bał\nW najciemniejszą noc\nZe mną mój Pan\n" +
                "Na pewno to wiem\nŻe zobaczę Cię x4\n\nRef. Rojaśnij oblicze swe...\n",
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
                "mej duszy pokój w Tobie jest\nGdzie pójdziesz Ty,\nja pójdę też\n",
        "Zwrotka:\nKażdy dzień upewnia mnie\nPan w miłości wierny jest\nKtóż by inny mógł jak On\n" +
                "wybaczać co dzień?\n\nBoże, wiem jak ranię Cię\nczęsto słabnę gubię sens\n" +
                "jednak Ty podnosisz mnie\ni nadzieję dajesz mi\n\n" +
                "Refren:\nNikt nie odbierze mi tego, co mam w Tobie x2\n" +
                "Pełnię miłości, pełnię wolności, pełnię radości mam w Tobie... x2\n",
        "Przed tronem Twym stoimy\nWpatrzeni w Twej miłości blask\nDo Ciebie Panie podobni\nStajemy się widząc Twą twarz\n\n" +
                "Refren:\nChwała Twa wypełnia nas\nObecności Twojej blask\nGdy wielbimy Ciebie, wiem\nJesteś tu.\n\n" +
                "Bridge:\nChwała, cześć\nMądrość, moc, błogosławieństwo\nNa wieki, na wieki\n",
        "Zwrotka:\nPrzyjdź i zajmij miejsce swe na tronie naszych serc\nPrzyjdź i zajmij miejsce swe\n\n" +
                "Refren:\nCiebie pragnie dusza moja\nW suchej ziemi pragnę Cię\n\n" +
                "Bridge:\nPrzyjdź i zajmij, przyjdź i zajmij miejsce\n\n" +
                "Outro:\nPrzyjdź i zajmij, przyjdź i zajmij miejsce swe\n",
        "1. Wzywam Cię, Duchu, przyjdź\nCzekam wciąż, byś dotknął nas\n" +
                "Wołam Cię, Panie, przyjdź\nJezu, Zbawco, do dzieci Twych\n\n" +
                "Ref. Jak spragniona ziemia rosy dusza ma\nTylko Ty możesz wypełnić\nSerca głód, serca głód\n\n" +
                "2. Głębio morz, potęgo gór,\nBoże mój, nie mogę bez\n" +
                "Twej miłości żyć\nNie chcę bez Ciebie żyć\n\n" +
                "Ref. Jak spragniona ziemia...\n",
        "1. Przychodzę do Ciebie\nwiem, że czekasz na mnie zawsze, zawsze\n" +
                "Wszystko rozumiesz\nzanim zacznę Ci cokolwiek tłumaczyć\n\n" +
                "Pre-Chorus 1:\nGdzie ukryję się przed Tobą, O Panie\nSkoro wszystkie moje drogi są Ci znane\n\n" +
                "Chorus:\nTy znasz mnie, znasz moje serce\ni dobrze wiesz, czego mi trzeba\n" +
                "Chcę bliżej, być bliżej Ciebie\nrozmawiać o Twoich pragnieniach\n\n" +
                "2. Przychodzę do Ciebie\nstaram się szukać Cię zawsze, zawsze\n" +
                "Już się nie schowam\ngdy mnie znowu po imieniu zawołasz\n\n" +
                "Pre-Chorus 2:\nMoim szczęściem jest być z Tobą, O Panie\nTo kim jestem dziś na nowo Ci oddaję\n\n" +
                "Refren x2\n",
        "1. Jesteś moim domem, moim bezpieczeństwem\nJesteś wszystkim czego szukam\n" +
                "Ty jesteś, jesteś\n\nOoo, Obecny\nOoo, Ty jesteś\nOoo, Niezmienny\n\n" +
                "Refren:\nW Tobie cały mój świat\nTy jesteś moim ocaleniem\n" +
                "W Tobie cały mój świat\n\nW Tobie cały mój świat\n" +
                "Podnoszę ręce, by chwalić Ciebie\nW Tobie cały mój świat\n\n" +
                "2. Jesteś moim niebem, moim ukojeniem\nJesteś wszystkim na co czekam\nTy jesteś, jesteś\n\n" +
                "Ooo, Obecny\nOoo, Ty jesteś\nOoo, Niezmienny\n\n" +
                "Ref. W Tobie cały mój świat...\n\n" +
                "Bridge:\nCały mój świat x2\nW Tobie cały mój świat x4\n\n" +
                "Ref. W Tobie cały mój świat...\n",
        "1. Niech słaby powie: „Mam moc”,\nBiedny wyzna: „Wszystko mam”,\n" +
                "Ślepy mówi: „Widzę znów”, we mnie to uczynił Bóg\n\n" +
                "Ref. Hosanna, Hosanna Barankowi, co siebie dał.\n" +
                "Hosanna, Hosanna, Jezus zmarł i zmartwychwstał.\n\n" +
                "2. Wejdę w rzekę, gdzie grzechy me\nZmywasz, Zbawco, swoją krwią.\n" +
                "Z niebios miłość wylewa się, łaską swą ogarnij mnie.\n",
        "1. Zburzone mury, wokół pustego miasta\ntam gdzie płynęły rzeki, zaległy suche kości\n" +
                "Poszukujmy, poszukujmy Jego głosu\npoznajmy Jego Słowo\n\n" +
                "Ref. To co upadło Bóg podniesie jeszcze\n" +
                "przywróci swoją chwałę, uleczy co złamane\nSpod gruzów pychy Pan wyciągnie nowe serce\n\n" +
                "2. To nasze winy, zabrały nas w niewolę\nlecz teraz czas powrócić, nadeszło odkupienie\n" +
                "Nasłuchujmy, nasłuchujmy, czy już wzywa\nby ruszyć nową drogą\n\n" +
                "Ref. To co upadło...\n\n" +
                "Bridge:\nSłyszę cichy szum, czuję lekki powiew\natmosfera się odmienia, idzie nowe\n\n" +
                "Z nieba spada deszcz, z gór wypływa strumień\nsuche brzegi znów wypełnia, rzeka życia\n\n" +
                "Ref. To co upadło... x2\n",
        "Zwrotka:\nChrystus Pan\nMesjasz Król\nNa drzewo krzyża poniósł ciężar mój\n" +
                "Boży Syn Panów Pan\nPosłuszny Ojcu został całkiem sam\n\n" +
                "Ref. Powiedz tylko słowo\nA będzie uzdrowiona dusza ma\nPowiedz tylko słowo\nPanie mój\n\n" +
                "Bridge:\nUwielbiam Ciebie Jezu\nUwielbiam Ciebie Jezu\n",
        "1. Niech przylgnie serce moje\nDo Twego serca Panie\n" +
                "Niech przylgnie serce moje\nDo Twego serca Panie mój\n\n" +
                "Ref. Bo tylko w Tobie\nspełnienie me\nBo tylko w Tobie\nwszystko jest\n" +
                "Bo tylko w Tobie\nwszystko czego pragnę\nBo tylko w Tobie\nżyć, umierać chcę.\n",
        "Zwrotka:\nWszystkie pragnienia mego serca\nOddaję w Twoje święte ręce\n" +
                "Czekam z nadzieją, że je spełnisz\nZ Twoją wolą zgadzam się\n" +
                "Twe drogi nie są mi już obce\nSłucham uważnie Twego głosu\n" +
                "Tak bardzo cieszę się, że jesteś\nCieszę się, że jesteś blisko\n\n" +
                "Refren:\nCieszę się, że jesteś\nCieszę się, że jesteś blisko x2\n\n" +
                "Bridge:\nPrzyjdź do mnie Panie ze swoją radością\n" +
                "Przyjdź do mnie Panie ze swoją miłością\nWypełnij mnie, wypełnij mnie\n",
        "Daleki bądź od strachu\nze mną nie masz się czym martwić\nja pokażę Ci jak wytrwać w mej miłości\n" +
                "Twe słowo drogowskazem\nGdy nie mam siły\nWtedy mówię Pan jest większy\nwtedy mówię Pan jest większy\n\n" +
                "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n" +
                "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n\n" +
                "Gdy patrzę na twe niebo\ni to co wysoko na nim\nzastanawiam się kim jestem\nw twoich oczach\n" +
                "Wciąż szukam odpowiedzi\nGdy nie mam siły\nOtwórz moje oczy Panie\nOtwórz moje oczy Panie\n\n" +
                "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n" +
                "Alleluja, dziękujmy Panu\nAlleluja, jego łaska trwa na wieki\njego łaska trwa na wieki\n",
        "Tak bowiem Bóg umiłował świat,\nże Syna swego nam dał\nAby każdy kto w Niego wierzy\nmiał życie wieczne.\n",
        "Kim jesteś Ty Panie, a kim jestem ja?\nKim Ty? A kim ja?\n",
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
                "Nasza pomoc jest w Imieniu Pana, który stworzył niebo i ziemię\n",
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
                "Gdzie winny krzew rozpięty,\nSpływają krwawe strugi\nCzerwienią słodką wina.\n",
        "Podnieś mnie Jezu i prowadź do Ojca x2\nZanurz mnie w wodzie Jego miłosierdzia\nAmen\n",
        "Zwrotka:\nPowstań i żyj, chociaż wokół mrok,\npowstań i żyj, dobro wielką ma moc,\n" +
                "powstań i żyj, choć upadłeś nie raz,\nJezus doda Ci sił, On zmartwychwstał byś żył,\n" +
                "Jezus doda Ci sił, On zmartwychwstał byś żył!\n\n" +
                "Refren:\nIle trzeba łez, aby wrócić do Ciebie,\njak daleko oddalić się, by usłyszeć Twój szept,\n" +
                "jak bardzo żałować, aby pękło to serce kamienne.\nIle trzeba łez, aby wrócić do Ciebie.\n\n" +
                "Outro:\nPowstań i żyj, powstań i żyj\n",
        "Nasze życie nie jest łatwe\ngrzechem skażone każde jest\nJeśli w sercu Twym cierpienie\n" +
                "Jezus jego ukojeniem\n\nPozwól Mu, by wziął co trudne\nkażdy grzech i to co brudne jest\n" +
                "On jedynym Zbawicielem\nw Jego ranach Twoje odkupienie\n\n(instrumental)\n\n" +
                "Nasze życie nie jest łatwe\ngrzechem skażone każde jest\nJeśli w sercu Twym cierpienie\n" +
                "Jezus jego ukojeniem\n\nPozwól Mu, by wziął co trudne\nkażdy grzech i to co brudne jest\n" +
                "On jedynym Zbawicielem jest\n\nJezus podniesie Cię\nJezus wybacza grzech\n" +
                "Jezus pocieszy Cię, gdy Twoje serce płacze x2\n\nOn miłością jest\n",
        "Zmiłuj się nade mną Boże\nulituj się nad grzechem mym\nłaską swą zgładź mą nieprawość\n" +
                "i obmyj mnie z moich win\tx2\n\nSkruszonym sercem nie pogardzisz Panie\n" +
                "Skruszonym sercem nie pogardzisz Panie\nSkruszonym sercem nie pogardzisz Panie\n" +
                "Wiem, nie pogardzisz sercem mym\tx2\n\nOdnów we mnie serce czyste\n" +
                "Daj mi moc swojego Ducha\tx6\n\nOutro:\nPanie daj, daj serce czyste\n" +
                "poślij nam swojego Ducha\nPanie daj, daj serce czyste\ndaj mi moc, daj mi moc\n",
        "Zwrotka 1:\nDo Ciebie dziś wracam znów, tak jak wtedy\nKiedy przyjąłeś mnie po raz pierwszy\n" +
                "Przyjmij, teraz znów przyjmij mnie\n\nDo Ciebie sam wracam znów dziś już wolny\n" +
                "Chcę z Tobą żyć, chcę być tu, tu przy Tobie\nPrzyjmij, teraz znów przyjmij mnie\n\n" +
                "Refren:\nTy nie przestajesz kochać nas\nSprowadzasz wciąż z błędnych dróg\n" +
                "Otwierasz drzwi, dajesz czas,\nAby każdy z nas przyszłość swą wybrać mógł\n" +
                "Nową przyszłość od dziś\n\nPrzyjmij, teraz znów przyjmij mnie\n\n" +
                "Refren\n\nBridge:\nOdradzam się, odradzam się\n" +
                "i mogę żyć, cieszyć się tańczyć x2\n\n(instrumental)\n\nBridge x4\n",
        "Jezu jesteś tu,\nświat odszedł w cień\nNie mam już nic\nMoje życie to Ty!\n\n" +
                "Każdy dzień Twoim darem\nNie przestanę wielbić Cię\n\n" +
                "Ref:\nChwała! Chwała!\nJezu wielbię Cię\n",
        "Duchu Święty przyjdź\n\nREFREN:\nTylko Ty jesteś drogą\nTylko Ty jesteś prawdą\n" +
                "Tylko Ty jesteś życiem\nWypełnij nasze serca\n\n" +
                "OUTRO:\nDuchu miłości, przemieniaj to co stare\n" +
                "Tchnij nowe życie w to co jest umarłe\n\nOżyw nas\nPrzemień nas\n",
        "Ref. Oooo\n\n1. Ty Panie mnie znasz\nTy widzisz wszystko to co robię\n" +
                "Bez wahania biegnę tam\nTam gdzie Twe Królestwo,\nTam gdzie Twe Królestwo\n\n" +
                "Ty mnie znasz\nTy widzisz wszystko to co robię\nBez wahania biegnę tam\n" +
                "Tam gdzie Twe Królestwo,\nTam gdzie Twe Królestwo\n\nRefren\n\n" +
                "2. Nic nie jest jak Ty\nŻadne bogactwa tego świata\nJedyne dobro widzę tam\n" +
                "Tam gdzie Twe Królestwo,\nTam gdzie Twe Królestwo x2\n\n" +
                "Refren\n\nOutro:\nTylko w Tobie jest potęga\nTylko w Tobie moja siła\n",
        "Ubi Caritas et amor\nUbi Caritas Deus ibi est\n"
    )
}