package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentScheduleBinding
import pl.kapucyni.wolczyn.app.model.Event
import pl.kapucyni.wolczyn.app.model.EventPlace
import pl.kapucyni.wolczyn.app.model.EventType
import pl.kapucyni.wolczyn.app.model.Guest
import pl.kapucyni.wolczyn.app.utils.*
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ScheduleRecyclerAdapter
import pl.kapucyni.wolczyn.app.view.ui.ScheduleTimeHeadersDecoration
import pl.kapucyni.wolczyn.app.viewmodels.ScheduleViewModel
import java.util.*

class ScheduleFragment : BindingFragment<FragmentScheduleBinding>() {

    private lateinit var mScheduleViewModel: ScheduleViewModel
    private lateinit var mAdapter: ScheduleRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var mDayViews: Array<TextView>
    private var mSelectedDay = 0
    var selectedGuest: Guest? = null

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentScheduleBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        val (schedule, positions) = createList()

        mAdapter = ScheduleRecyclerAdapter(schedule, this@ScheduleFragment)
        val llm = LinearLayoutManager(requireContext())
        binding.scheduleRecyclerView.apply {
            layoutManager = llm
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
            run {
                doOnNextLayout {
                    if (itemDecorationCount > 0)
                        for (i in itemDecorationCount - 1 downTo 0)
                            removeItemDecorationAt(i)
                    addItemDecoration(ScheduleTimeHeadersDecoration(it.context, schedule))
                }
            }
        }

        mScheduleViewModel =
            ViewModelProvider(this@ScheduleFragment)[ScheduleViewModel::class.java]
        requireActivity().let {
            if (it.checkNetworkConnection()) mScheduleViewModel.fetchSchedule()
            else it.showNoInternetDialogDataOutOfDate()
        }
        mScheduleViewModel.eventsFromFirestore.observe(viewLifecycleOwner) {
            it.forEach { firestoreEvent ->
                if (firestoreEvent.id.isNotEmpty()) {
                    val index = events.indexOfFirst { event -> event.id == firestoreEvent.id }
                    events[index].videoUrl = firestoreEvent.videoUrl
                    mAdapter.notifyItemChanged(index + 1)
                }
            }
        }

        binding.scheduleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemPosition = llm.findFirstCompletelyVisibleItemPosition()
                (activity as MainActivity).changeToolbarTitle(
                    when {
                        visibleItemPosition < positions[1] -> {
                            mSelectedDay = 0
                            "${getString(R.string.menu_schedule)} - ${getString(R.string.monday)}"
                        }
                        visibleItemPosition < positions[2] -> {
                            mSelectedDay = 1
                            "${getString(R.string.menu_schedule)} - ${getString(R.string.tuesday)}"
                        }
                        visibleItemPosition < positions[3] -> {
                            mSelectedDay = 2
                            "${getString(R.string.menu_schedule)} - ${getString(R.string.wednesday)}"
                        }
                        visibleItemPosition < positions[4] -> {
                            mSelectedDay = 3
                            "${getString(R.string.menu_schedule)} - ${getString(R.string.thursday)}"
                        }
                        else -> {
                            mSelectedDay = 4
                            "${getString(R.string.menu_schedule)} - ${getString(R.string.friday)}"
                        }
                    }
                )
                invalidateDayViews()
            }
        })

        mDayViews = arrayOf(
            binding.daysBarLayout.firstDay,
            binding.daysBarLayout.secondDay,
            binding.daysBarLayout.thirdDay,
            binding.daysBarLayout.fourthDay,
            binding.daysBarLayout.fifthDay
        )
        mDayViews.forEachIndexed { i, v ->
            v.setOnClickListener { llm.scrollToPositionWithOffset(positions[i], 10) }
        }
        binding.scheduleListLayout.removeView(binding.daysBarLayout.root)
        (activity as MainActivity).addViewToAppBar(binding.daysBarLayout.root)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        if (day in 12..16 && month == 7)
            mDayViews[day - 12].performClick()

        mBottomSheetBehavior = BottomSheetBehavior.from(binding.scheduleGuestSheet)
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val guestDetailsFragment =
                    childFragmentManager.findFragmentById(R.id.scheduleGuestSheet) as GuestDetailsFragment
                selectedGuest?.let {
                    guestDetailsFragment.setViewsValues(it)
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.scheduleRecyclerView.alpha = 1f
                    selectedGuest = null
                    guestDetailsFragment.hideViews()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onStop() {
        (activity as MainActivity).removeViewFromAppBar(binding.daysBarLayout.root)
        super.onStop()
    }

    fun onItemClick(event: Event) {
        if (selectedGuest == null) when (event.eventType) {
            EventType.BREVIARY -> (requireActivity() as MainActivity).goToSelectedFragment(R.id.nav_breviary)
            EventType.CONCERT -> expandBottomSheet(GuestListFragment.concertGuests[event.guestIndex!!])
            EventType.CONFERENCE -> expandBottomSheet(GuestListFragment.conferenceGuests[event.guestIndex!!])
            EventType.MASS -> if (event.guestIndex != null) expandBottomSheet(GuestListFragment.conferenceGuests[event.guestIndex])
            else -> when (event.id) {
                "2022-07-13-09", "2022-07-13-12" -> requireActivity().showMFTauDialog()
                else -> return
            }
        } else hideBottomSheet()
    }

    private fun invalidateDayViews() {
        val daySelectedDrawable =
            AppCompatResources.getDrawable(requireContext(), R.drawable.day_selected)
        mDayViews.forEach {
            it.background = null
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_theme_primary))
        }
        mDayViews[mSelectedDay].background = daySelectedDrawable
        mDayViews[mSelectedDay]
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.app_theme_onPrimary))
    }

    private fun createList(): Pair<ArrayList<Any>, ArrayList<Int>> {
        val scheduleList = arrayListOf<Any>()
        val positions = arrayListOf<Int>()
        val days = events.distinctBy { it.date }.map { "${it.day}, ${it.date}" }
        days.forEachIndexed { index, day ->
            positions.add(scheduleList.size)
            scheduleList.add(day + "~${dayNames[index]}")
            events.forEach { event -> if (event.day == day.split(",")[0]) scheduleList.add(event) }
        }
        return Pair(scheduleList, positions)
    }

    private fun expandBottomSheet(guest: Guest) {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        selectedGuest = guest
        binding.scheduleRecyclerView.isEnabled = false
        binding.scheduleRecyclerView.animate().alpha(0.15f).duration = 200
    }

    fun hideBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun onBackPressed(): Boolean {
        return if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            true
        } else {
            hideBottomSheet()
            false
        }
    }

    fun onIconClick(iconNumber: Int) {
        selectedGuest?.let {
            if (it.sites[iconNumber] != "")
                requireContext().openWebsiteInCustomTabsService(it.sites[iconNumber])
        }
    }

    companion object {
        val dayNames = listOf("Obdarzeni", "Ożywieni", "Odżywieni", "Odziani", "Oddani")

        val events = listOf(
            Event(
                "2022-07-11-01", "Poniedziałek", "11 Lipca", "8:30", "Rejestracja uczestników",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2022-07-11-02", "Poniedziałek", "11 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.CAMPSITE, EventType.EXTRA, null
            ),
            Event(
                "2022-07-11-03", "Poniedziałek", "11 Lipca", "17:30",
                "Rozpoczęcie 28. Spotkania Młodych",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2022-07-11-04", "Poniedziałek", "11 Lipca", "18:15", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-11-05", "Poniedziałek", "11 Lipca", "19:15", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-11-06", "Poniedziałek", "11 Lipca", "20:00", "Koncert: Carrantuohill",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 1
            ),
            Event(
                "2022-07-11-07", "Poniedziałek", "11 Lipca", "21:30",
                "Nabożeństwo rozpoczęcia: \"Obdarzeni Miłością\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2022-07-11-08", "Poniedziałek", "11 Lipca", "22:30",
                "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2022-07-12-01", "Wtorek", "12 Lipca", "7:30", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-12-02", "Wtorek", "12 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-12-03", "Wtorek", "12 Lipca", "9:30",
                "Modlitwa poranna / Rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2022-07-12-04", "Wtorek", "12 Lipca", "10:00",
                "Konferenecja: Tymoteusz Filar",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 0
            ),
            Event(
                "2022-07-12-05", "Wtorek", "12 Lipca", "11:15",
                "Mecz: Kapucyni vs. Reszta Świata",
                EventPlace.COURT, EventType.EXTRA, null
            ),
            Event(
                "2022-07-12-06", "Wtorek", "12 Lipca", "12:15", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-12-07", "Wtorek", "12 Lipca", "14:00", "Świadectwo",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, null
            ),
            Event(
                "2022-07-12-08", "Wtorek", "12 Lipca", "15:00", "Spotkania w grupkach",
                EventPlace.AMPHITHEATRE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-12-09", "Wtorek", "12 Lipca", "16:30", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2022-07-12-10", "Wtorek", "12 Lipca", "17:00",
                "Eucharystia (bp Damian Bryl)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 1
            ),
            Event(
                "2022-07-12-11", "Wtorek", "12 Lipca", "18:00", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-12-12", "Wtorek", "12 Lipca", "18:30", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-12-13", "Wtorek", "12 Lipca", "19:00", "Koncert: Muode Koty",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 0
            ),
            Event(
                "2022-07-12-15", "Wtorek", "12 Lipca", "20:30",
                "Nabożeństwo: \"Ożywieni Miłością\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2022-07-12-16", "Wtorek", "12 Lipca", "22:30",
                "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2022-07-13-01", "Środa", "13 Lipca", "7:30", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-13-02", "Środa", "13 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-13-03", "Środa", "13 Lipca", "9:30",
                "Modlitwa poranna / Rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2022-07-13-04", "Środa", "13 Lipca", "10:00",
                "Konferencja: Sebastian Kubis oraz br. Tomasz Łakomczyk",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 2
            ),
            Event(
                "2022-07-13-05", "Środa", "13 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2022-07-13-06", "Środa", "13 Lipca", "11:30", "Eucharystia",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2022-07-13-07", "Środa", "13 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-13-08", "Środa", "13 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.CAMPSITE, EventType.EXTRA, null
            ),
            Event(
                "2022-07-13-09", "Środa", "13 Lipca", "15:00", "Koronka (Młodzież Franciszkańska Tau)",
                EventPlace.CHURCH, EventType.PRAYER, null
            ),
            Event(
                "2022-07-13-10", "Środa", "13 Lipca", "15:00", "Świadectwo: br. Maciej Jabłoński",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 3
            ),
            Event(
                "2022-07-13-11", "Środa", "13 Lipca", "16:30", "Spotkanie w grupkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2022-07-13-12", "Środa", "13 Lipca", "18:00", "Kolacja || Grill z MF Tau",
                EventPlace.GARDEN, EventType.MEAL, null
            ),
            Event(
                "2022-07-13-13", "Środa", "13 Lipca", "19:15", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-13-14", "Środa", "13 Lipca", "19:30",
                "Koncert: KapEl'a",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 0
            ),
            Event(
                "2022-07-13-15", "Środa", "13 Lipca", "21:00",
                "Nabożeństwo: \"Odżywieni Miłością\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2022-07-13-16", "Środa", "13 Lipca", "22:00",
                "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2022-07-14-01", "Czwartek", "14 Lipca", "7:30", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-14-02", "Czwartek", "14 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-14-03", "Czwartek", "14 Lipca", "9:00",
                "Modlitwa poranna / Rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2022-07-14-04", "Czwartek", "14 Lipca", "10:00",
                "Konferencja: s. Aleksandra Szyborska",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 4
            ),
            Event(
                "2022-07-14-05", "Czwartek", "14 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2022-07-14-06", "Czwartek", "14 Lipca", "11:15", "Eucharystia",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2022-07-14-07", "Czwartek", "14 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-14-08", "Czwartek", "14 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.CAMPSITE, EventType.EXTRA, null
            ),
            Event(
                "2022-07-14-09", "Czwartek", "14 Lipca", "15:00", "Koronka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2022-07-14-10", "Czwartek", "14 Lipca", "15:20",
                "Świadectwo: Magdalena Myjak",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 5
            ),
            Event(
                "2022-07-14-11", "Czwartek", "14 Lipca", "16:00", "Spotkanie w grupkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2022-07-14-12", "Czwartek", "14 Lipca", "18:00", "Kolacja",
                EventPlace.GARDEN, EventType.MEAL, null
            ),
            Event(
                "2022-07-14-13", "Czwartek", "14 Lipca", "19:15", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-14-14", "Czwartek", "14 Lipca", "20:00", "Wieczór chwały: SOWINSKY",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 3
            ),
            Event(
                "2022-07-14-15", "Czwartek", "14 Lipca", "21:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2022-07-15-01", "Piątek", "15 Lipca", "7:00", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2022-07-15-02", "Piątek", "15 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2022-07-15-03", "Piątek", "15 Lipca", "9:30",
                "Modlitwa poranna / Rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2022-07-15-04", "Piątek", "15 Lipca", "9:45",
                "Konferencja: ks. Michał Pabiańczyk",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 4
            ),
            Event(
                "2022-07-15-05", "Piątek", "15 Lipca", "10:45", "Eucharystia",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2022-07-15-06", "Piątek", "15 Lipca", "12:00",
                "Rozesłanie i zakończenie spotkania",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            )
        )
    }
}
