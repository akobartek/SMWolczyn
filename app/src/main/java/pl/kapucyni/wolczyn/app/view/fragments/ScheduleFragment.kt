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
                "2019-07-15-09", "2019-07-15-12" -> requireActivity().showMFTauDialog()
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
        days.forEach { day ->
            positions.add(scheduleList.size)
            scheduleList.add(day)
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
            if (it.sites[iconNumber] != "") context?.openWebsiteInCustomTabsService(it.sites[iconNumber])
        }
    }

    companion object {
        val events = listOf(
            Event(
                "2021-07-12-01", "Poniedziałek", "12 Lipca", "7:30", "Rejestracja uczestników",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2021-07-12-02", "Poniedziałek", "12 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.CAMPSITE, EventType.EXTRA, null
            ),
            Event(
                "2019-07-12-03", "Poniedziałek", "12 Lipca", "17:30",
                "Rozpoczęcie XXVII Spotkania Młodych w Wołczynie",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2019-07-12-04", "Poniedziałek", "12 Lipca", "18:15", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-12-05", "Poniedziałek", "12 Lipca", "19:00", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-12-06", "Poniedziałek", "12 Lipca", "20:00", "Koncert: ks. Jakub Bartczak",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 1
            ),
            Event(
                "2019-07-12-07", "Poniedziałek", "12 Lipca", "21:30",
                "Nabożeństwo rozpoczęcia: \"Tajemnica początków!\"\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-12-08", "Poniedziałek", "12 Lipca", "22:30",
                "Podsumowanie dnia\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-13-01", "Wtorek", "13 Lipca", "7:30", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-13-02", "Wtorek", "13 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-13-03", "Wtorek", "13 Lipca", "9:30",
                "Modlitwa poranna\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-13-04", "Wtorek", "13 Lipca", "10:00",
                "\"Jak wybrać\"\n- ks. Jakub Bartczak & Ewa Piwowar",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 0
            ),
            Event(
                "2019-07-13-05", "Wtorek", "13 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-13-06", "Wtorek", "13 Lipca", "11:30",
                "Eucharystia (bp Damian Bryl)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 1
            ),
            Event(
                "2019-07-13-07", "Wtorek", "13 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-13-08", "Wtorek", "13 Lipca", "14:00",
                "Mecz: Kapucyni vs. Reszta Świata",
                EventPlace.COURT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-13-09", "Wtorek", "13 Lipca", "15:45", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-13-10", "Wtorek", "13 Lipca", "16:00", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-13-11", "Wtorek", "13 Lipca", "17:45", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-13-12", "Wtorek", "13 Lipca", "19:00", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-13-13", "Wtorek", "13 Lipca", "19:30", "Koncert: KapEl'a",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 0
            ),
            Event(
                "2019-07-13-15", "Wtorek", "13 Lipca", "21:00",
                "Nabożeństwo: \"Tajemnica oblicza!\"\n- br. Rafał Pysiak OFMCap",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-13-16", "Wtorek", "13 Lipca", "22:00",
                "Podsumowanie dnia\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-14-01", "Środa", "14 Lipca", "7:30", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-14-02", "Środa", "14 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-14-03", "Środa", "14 Lipca", "9:30",
                "Modlitwa poranna\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-14-04", "Środa", "14 Lipca", "10:00",
                "\"Wybrali za mnie\"\n- Maja i Krzysztof Sowińscy",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 2
            ),
            Event(
                "2019-07-14-05", "Środa", "14 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-14-06", "Środa", "14 Lipca", "11:30",
                "Eucharystia (br. Rafał Tański OFMCap / br. Wojciech Pawłowski OFMCap)",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-14-07", "Środa", "14 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-14-08", "Środa", "14 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.CAMPSITE, EventType.EXTRA, null
            ),
            Event(
                "2019-07-14-09", "Środa", "14 Lipca", "16:15", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-14-10", "Środa", "14 Lipca", "16:30", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-14-11", "Środa", "14 Lipca", "18:00", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-14-12", "Środa", "14 Lipca", "19:00", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-14-13", "Środa", "14 Lipca", "19:30",
                "Nabożeństwo pojednania: \"Tajemnica wyboru!\"\n- br. Tomasz Łakomczyk OFMCap",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-14-14", "Środa", "14 Lipca", "22:30",
                "Podsumowanie dnia\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-15-01", "Czwartek", "15 Lipca", "7:30", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-15-02", "Czwartek", "15 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-15-03", "Czwartek", "15 Lipca", "9:30",
                "Modlitwa poranna\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-15-04", "Czwartek", "15 Lipca", "10:00",
                "\"Czy to mój wybór?\"\n- ks. Grzegorz Szczygieł MS",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 3
            ),
            Event(
                "2019-07-15-05", "Czwartek", "15 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-15-06", "Czwartek", "15 Lipca", "11:30",
                "Eucharystia (Bracia neoprezbiterzy / br. Michał Ferenc OFMCap i br. Mateusz Kaczorowski OFMCap)",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-15-07", "Czwartek", "15 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-15-08", "Czwartek", "15 Lipca", "13:30", "Taniec z gwiazdami",
                EventPlace.CAMPSITE, EventType.EXTRA, null
            ),
            Event(
                "2019-07-15-09", "Czwartek", "15 Lipca", "15:00",
                "Koronka do Bożego Miłosierdzia\n- Młodzież Franciszkańska TAU",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-15-10", "Czwartek", "15 Lipca", "16:00", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-15-11", "Czwartek", "15 Lipca", "16:15", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-15-12", "Czwartek", "15 Lipca", "18:00",
                "Kolacja || Grill z MF Tau",
                EventPlace.GARDEN, EventType.MEAL, null
            ),
            Event(
                "2019-07-15-13", "Czwartek", "15 Lipca", "19:00", "Nieszpory",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-15-14", "Czwartek", "15 Lipca", "19:30", "Koncert: LUXTORPEDA",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 2
            ),
            Event(
                "2019-07-15-15", "Czwartek", "15 Lipca", "21:30",
                "Nabożeństwo: \"Tajemnica życia!\"\n- br. Grzegorz Dziedzic OFMCap",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-15-16", "Czwartek", "15 Lipca", "22:30",
                "Podsumowanie dnia\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-16-01", "Piątek", "16 Lipca", "7:00", "Jutrznia",
                EventPlace.CHURCH, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-16-02", "Piątek", "16 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-16-03", "Piątek", "16 Lipca", "9:00",
                "Modlitwa poranna\n- br. Piotr Szaro OFMCap",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-16-04", "Piątek", "16 Lipca", "9:30",
                "Jak iść przez świat?\n- ks. Michał Olszewki SCJ",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 4
            ),
            Event(
                "2019-07-16-05", "Piątek", "16 Lipca", "10:30",
                "Eucharystia (br. Marek Miszczyński OFMCap / br. Piotr Kowalski OFMCap)",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-16-06", "Piątek", "16 Lipca", "12:00",
                "Rozesłanie i zakończenie spotkania",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            )
        )
    }
}
