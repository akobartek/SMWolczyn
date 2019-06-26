package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Event
import pl.kapucyni.wolczyn.app.model.EventPlace
import pl.kapucyni.wolczyn.app.model.EventType

class ScheduleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    companion object {
        val events = arrayOf(
            Event(
                "Poniedziałek", "8 Lipca", "8:30", "Rejestracja Uczestników",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "17:30", "Rozpoczęcie XXV Spotkania Młodych w Wołczynie",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "18:15", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "20:00", "Koncert: Wyrwani z Niewoli",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 2
            ),
            Event(
                "Poniedziałek", "8 Lipca", "20:00", "Tajemnica powołania - Q&A",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "21:30", "Nabożeństwo rozpoczęcia: \"RATUNKU!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "Poniedziałek", "8 Lipca", "22:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "Wtorek", "9 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Wtorek", "9 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Wtorek", "9 Lipca", "9:30", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "Wtorek", "9 Lipca", "10:00", "\"Mój kościół zraniony\"\n- bp Edward Kawa OFMConv",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 1
            ),
            Event(
                "Wtorek", "9 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "Wtorek", "9 Lipca", "11:30", "Eucharystia (bp Edward Kawa OFMConv)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 1
            ),
            Event(
                "Wtorek", "9 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Wtorek", "9 Lipca", "14:00", "Mecz: Kapucyni vs. reszta świata",
                EventPlace.COURT, EventType.EXTRA, null
            ),
            Event(
                "Wtorek", "9 Lipca", "15:45", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUP, null
            ),
            Event(
                "Wtorek", "9 Lipca", "16:00", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUP, null
            ),
            Event(
                "Wtorek", "9 Lipca", "17:45", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Wtorek", "9 Lipca", "18:45", "Koncert: TATO",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 3
            ),
            Event(
                "Wtorek", "9 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Wtorek", "9 Lipca", "19:30", "Koncert: KapEl'a",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 0
            ),
            Event(
                "Wtorek", "9 Lipca", "21:00", "Urodziny",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "Wtorek", "9 Lipca", "21:40", "Nabożeństwo: \"ODKRYJ JEGO OBLICZE!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "Wtorek", "9 Lipca", "22:40", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "Środa", "10 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Środa", "10 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Środa", "10 Lipca", "9:30", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "Środa", "10 Lipca", "10:00", "\"Gdzie szukać cegieł i zaprawy\"\n- o. Antonello Cadeddu",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 4
            ),
            Event(
                "Środa", "10 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "Środa", "10 Lipca", "11:30", "Eucharystia (br. Tomasz Protasiewicz / o. Antonello Cadeddu)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 4
            ),
            Event(
                "Środa", "10 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Środa", "10 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "Środa", "10 Lipca", "15:15", "\"Kawalerka do wynajęcia czy dom na całe życie?\"\n- Michał \"PAX\" Bukowski",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 2
            ),
            Event(
                "Środa", "10 Lipca", "16:15", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUP, null
            ),
            Event(
                "Środa", "10 Lipca", "16:30", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUP, null
            ),
            Event(
                "Środa", "10 Lipca", "18:00", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Środa", "10 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Środa", "10 Lipca", "19:30", "Nabożeństwo pokutne: \"ODBUDUJ MÓJ KOŚCIÓŁ!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "Środa", "10 Lipca", "22:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "Czwartek", "11 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Czwartek", "11 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Czwartek", "11 Lipca", "9:00", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "Czwartek", "11 Lipca", "10:00", "\"Czym umeblować żeby było ładnie?\"\n- br. Paweł Teperski",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 3
            ),
            Event(
                "Czwartek", "11 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "Czwartek", "11 Lipca", "11:15", "Eucharystia (Bracia neoprezbiterzy / br. Paweł Teperski)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 3
            ),
            Event(
                "Czwartek", "11 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Czwartek", "11 Lipca", "13:30", "Taniec z gwiazdami",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "Czwartek", "11 Lipca", "15:00", "KORONKA",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "Czwartek", "11 Lipca", "15:20", "\"Kiedy będzie doskonale\"\n- ks. Maciej Sarbinowski SDB",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 5
            ),
            Event(
                "Czwartek", "11 Lipca", "16:15", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUP, null
            ),
            Event(
                "Czwartek", "11 Lipca", "18:00", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Czwartek", "11 Lipca", "18:00", "Grill MF Tau",
                EventPlace.GARDEN, EventType.MEAL, null
            ),
            Event(
                "Czwartek", "11 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Czwartek", "11 Lipca", "19:30", "Koncert: niemaGOtu",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 1
            ),
            Event(
                "Czwartek", "11 Lipca", "19:30", "\"Klucz do ikony\"\n- br. Marcin Świąder",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "Czwartek", "11 Lipca", "21:30", "Nabożeństwo: \"DAJ SIĘ POKOCHAĆ!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "Czwartek", "11 Lipca", "22:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "Piątek", "12 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "Piątek", "12 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "Piątek", "12 Lipca", "9:30", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "Piątek", "12 Lipca", "9:45", "\"Popatrzcie jak oni się miłują\"\n- Monika i Marcin Gomułkowie",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 0
            ),
            Event(
                "Piątek", "12 Lipca", "10:45", "Eucharystia (br. Tomasz Żak / br. Piotr Kowalski)",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "Piątek", "12 Lipca", "12:00", "Rozesłanie",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            )
        )
    }
}
