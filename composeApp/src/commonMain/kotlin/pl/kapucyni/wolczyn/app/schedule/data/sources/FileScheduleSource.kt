package pl.kapucyni.wolczyn.app.schedule.data.sources

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import pl.kapucyni.wolczyn.app.schedule.domain.model.Event
import pl.kapucyni.wolczyn.app.schedule.domain.model.EventPlace
import pl.kapucyni.wolczyn.app.schedule.domain.model.EventType
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay

val schedule = listOf(
    ScheduleDay(
        date = LocalDate(dayOfMonth = 22, monthNumber = 7, year = 2024),
        name = "chaos",
        events = listOf(
            Event(
                id = "2024-07-22-01",
                hour = LocalTime(7, 30),
                name = "rozpoczęcie rejestracji",
                place = EventPlace.BIG_TENT,
                type = EventType.ORGANIZATION
            ),
            Event(
                id = "2024-07-22-02",
                hour = LocalTime(17, 30),
                name = "rozpoczęcie 30. Spotkania Młodych",
                place = EventPlace.BIG_TENT,
                type = EventType.ORGANIZATION
            ),
            Event(
                id = "2024-07-22-03",
                hour = LocalTime(18, 30),
                name = "kolacja",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-22-04",
                hour = LocalTime(20, 0),
                name = "koncert. KapEl'a",
                place = EventPlace.BIG_TENT,
                type = EventType.CONCERT
            ),
            Event(
                id = "2024-07-22-05",
                hour = LocalTime(21, 30),
                name = "nabożeństwo",
                place = EventPlace.BIG_TENT,
                type = EventType.DEVOTION
            ),
            Event(
                id = "2024-07-22-06",
                hour = LocalTime(22, 30),
                name = "podsumowanie dnia",
                place = EventPlace.BIG_TENT,
                type = EventType.PRAYER
            ),
        )
    ),
    ScheduleDay(
        date = LocalDate(dayOfMonth = 23, monthNumber = 7, year = 2024),
        name = "śmierć",
        events = listOf(
            Event(
                id = "2024-07-23-01",
                hour = LocalTime(7, 30),
                name = "jutrznia",
                place = EventPlace.CHURCH,
                type = EventType.BREVIARY
            ),
            Event(
                id = "2024-07-23-02",
                hour = LocalTime(8, 0),
                name = "śniadanie",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-23-03",
                hour = LocalTime(9, 0),
                name = "rozgrzewka",
                place = EventPlace.BIG_TENT,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-23-04",
                hour = LocalTime(9, 30),
                name = "konferencja",
                place = EventPlace.BIG_TENT,
                type = EventType.CONFERENCE,
                guest = "ks. Rafał Główczyński SDS"
            ),
            Event(
                id = "2024-07-23-05",
                hour = LocalTime(10, 30),
                name = "przygotowanie do Eucharystii",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
            ),
            Event(
                id = "2024-07-23-06",
                hour = LocalTime(10, 45),
                name = "Eucharystia",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
                guest = "ks. bp Łukasz Buzun"
            ),
            Event(
                id = "2024-07-23-07",
                hour = LocalTime(12, 0),
                name = "obiad",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL,
            ),
            Event(
                id = "2024-07-23-08",
                hour = LocalTime(13, 15),
                name = "warsztaty",
                place = EventPlace.UNKNOWN,
                type = EventType.WORKSHOPS,
            ),
            Event(
                id = "2024-07-23-09",
                hour = LocalTime(15, 30),
                name = "spotkania w grupach",
                place = EventPlace.EVERYWHERE,
                type = EventType.GROUPS,
            ),
            Event(
                id = "2024-07-23-10",
                hour = LocalTime(18, 0),
                name = "kolacja",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL,
            ),
            Event(
                id = "2024-07-23-11",
                hour = LocalTime(18, 0),
                name = "koncert: Piotr \"Edzio\" Bylina",
                place = EventPlace.BIG_TENT,
                type = EventType.CONCERT,
            ),
            Event(
                id = "2024-07-23-12",
                hour = LocalTime(20, 30),
                name = "nabożeństwo",
                place = EventPlace.BIG_TENT,
                type = EventType.DEVOTION
            ),
            Event(
                id = "2024-07-23-13",
                hour = LocalTime(22, 0),
                name = "podsumowanie dnia",
                place = EventPlace.BIG_TENT,
                type = EventType.PRAYER
            ),
        )
    ),
    ScheduleDay(
        date = LocalDate(dayOfMonth = 24, monthNumber = 7, year = 2024),
        name = "bitwa",
        events = listOf(
            Event(
                id = "2024-07-24-01",
                hour = LocalTime(7, 30),
                name = "jutrznia",
                place = EventPlace.CHURCH,
                type = EventType.BREVIARY
            ),
            Event(
                id = "2024-07-24-02",
                hour = LocalTime(8, 0),
                name = "śniadanie",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-24-03",
                hour = LocalTime(9, 0),
                name = "rozgrzewka",
                place = EventPlace.BIG_TENT,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-24-04",
                hour = LocalTime(9, 30),
                name = "konferencja",
                place = EventPlace.BIG_TENT,
                type = EventType.CONFERENCE,
                guest = "s. Teresa Pawlak ZSAPU"
            ),
            Event(
                id = "2024-07-24-05",
                hour = LocalTime(10, 30),
                name = "przygotowanie do Eucharystii",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
            ),
            Event(
                id = "2024-07-24-06",
                hour = LocalTime(10, 45),
                name = "Eucharystia",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
                guest = "ks. Rafał Główczyński"
            ),
            Event(
                id = "2024-07-24-07",
                hour = LocalTime(12, 0),
                name = "obiad",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL,
            ),
            Event(
                id = "2024-07-24-08",
                hour = LocalTime(13, 15),
                name = "warsztaty",
                place = EventPlace.UNKNOWN,
                type = EventType.WORKSHOPS,
            ),
            Event(
                id = "2024-07-24-09",
                hour = LocalTime(15, 0),
                name = "spotkania w grupach",
                place = EventPlace.EVERYWHERE,
                type = EventType.GROUPS,
            ),
            Event(
                id = "2024-07-24-10",
                hour = LocalTime(18, 0),
                name = "kolacja",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL,
            ),
            Event(
                id = "2024-07-24-11",
                hour = LocalTime(20, 30),
                name = "nabożeństwo",
                place = EventPlace.BIG_TENT,
                type = EventType.DEVOTION
            ),
            Event(
                id = "2024-07-24-12",
                hour = LocalTime(22, 0),
                name = "podsumowanie dnia",
                place = EventPlace.BIG_TENT,
                type = EventType.PRAYER
            ),
        )
    ),
    ScheduleDay(
        date = LocalDate(dayOfMonth = 25, monthNumber = 7, year = 2024),
        name = "radość",
        events = listOf(
            Event(
                id = "2024-07-25-01",
                hour = LocalTime(7, 30),
                name = "jutrznia",
                place = EventPlace.CHURCH,
                type = EventType.BREVIARY
            ),
            Event(
                id = "2024-07-25-02",
                hour = LocalTime(8, 0),
                name = "śniadanie",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-25-03",
                hour = LocalTime(9, 0),
                name = "rozgrzewka",
                place = EventPlace.BIG_TENT,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-25-04",
                hour = LocalTime(9, 30),
                name = "konferencja",
                place = EventPlace.BIG_TENT,
                type = EventType.CONFERENCE,
                guest = "ks. Kamil Sowada"
            ),
            Event(
                id = "2024-07-25-05",
                hour = LocalTime(10, 30),
                name = "przygotowanie do Eucharystii",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
            ),
            Event(
                id = "2024-07-25-06",
                hour = LocalTime(10, 45),
                name = "Eucharystia",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
                guest = "ks. Kamil Sowada"
            ),
            Event(
                id = "2024-07-25-07",
                hour = LocalTime(12, 0),
                name = "obiad",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL,
            ),
            Event(
                id = "2024-07-25-08",
                hour = LocalTime(13, 15),
                name = "warsztaty",
                place = EventPlace.UNKNOWN,
                type = EventType.WORKSHOPS,
            ),
            Event(
                id = "2024-07-25-09",
                hour = LocalTime(15, 0),
                name = "spotkania w grupach",
                place = EventPlace.EVERYWHERE,
                type = EventType.GROUPS,
            ),
            Event(
                id = "2024-07-25-10",
                hour = LocalTime(18, 0),
                name = "kolacja",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL,
            ),
            Event(
                id = "2024-07-25-11",
                hour = LocalTime(18, 0),
                name = "nabożeństwo + koncert",
                place = EventPlace.BIG_TENT,
                type = EventType.DEVOTION,
                guest = "Anna Madej, br. Grzegorz Dziedzic i młodzież Wołczyna"
            ),
            Event(
                id = "2024-07-25-12",
                hour = LocalTime(21, 30),
                name = "podsumowanie dnia",
                place = EventPlace.BIG_TENT,
                type = EventType.PRAYER
            ),
        )
    ),
    ScheduleDay(
        date = LocalDate(dayOfMonth = 26, monthNumber = 7, year = 2024),
        name = "pokój",
        events = listOf(
            Event(
                id = "2024-07-26-01",
                hour = LocalTime(7, 30),
                name = "jutrznia",
                place = EventPlace.CHURCH,
                type = EventType.BREVIARY
            ),
            Event(
                id = "2024-07-26-02",
                hour = LocalTime(8, 0),
                name = "śniadanie",
                place = EventPlace.CAMPSITE,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-26-03",
                hour = LocalTime(9, 0),
                name = "rozgrzewka",
                place = EventPlace.BIG_TENT,
                type = EventType.MEAL
            ),
            Event(
                id = "2024-07-26-04",
                hour = LocalTime(9, 30),
                name = "konferencja",
                place = EventPlace.BIG_TENT,
                type = EventType.CONFERENCE,
                guest = "br. Paweł Frąckowiak OFMCap"
            ),
            Event(
                id = "2024-07-25-05",
                hour = LocalTime(10, 30),
                name = "przygotowanie do Eucharystii",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
            ),
            Event(
                id = "2024-07-26-06",
                hour = LocalTime(10, 45),
                name = "Eucharystia",
                place = EventPlace.BIG_TENT,
                type = EventType.MASS,
                guest = "br. Marek Miszczyński OFMCap"
            ),
            Event(
                id = "2024-07-26-07",
                hour = LocalTime(12, 0),
                name = "rozesłanie",
                place = EventPlace.BIG_TENT,
                type = EventType.ORGANIZATION,
            ),
        )
    ),
)