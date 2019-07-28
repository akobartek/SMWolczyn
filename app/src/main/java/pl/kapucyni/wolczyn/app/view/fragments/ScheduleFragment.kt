package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import kotlinx.android.synthetic.main.layout_days_bar.view.*
import kotlinx.android.synthetic.main.layout_links_bar.view.*
import kotlinx.android.synthetic.main.sheet_fragment_guest_details.view.*
import pl.kapucyni.wolczyn.app.R
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

class ScheduleFragment : Fragment() {

    private lateinit var mScheduleViewModel: ScheduleViewModel
    private lateinit var mAdapter: ScheduleRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var mDayViews: Array<TextView>
    private lateinit var mDaysBarLayout: View
    private var mSelectedDay = 0
    var selectedGuest: Guest? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (schedule, positions) = createList()

        mAdapter = ScheduleRecyclerAdapter(schedule, this@ScheduleFragment)
        val layoutManager = LinearLayoutManager(view.context)
        view.scheduleRecyclerView.layoutManager = layoutManager
        view.scheduleRecyclerView.itemAnimator = DefaultItemAnimator()
        view.scheduleRecyclerView.adapter = mAdapter
        view.scheduleRecyclerView.run {
            doOnNextLayout {
                if (itemDecorationCount > 0) {
                    for (i in itemDecorationCount - 1 downTo 0) {
                        removeItemDecorationAt(i)
                    }
                }
                addItemDecoration(ScheduleTimeHeadersDecoration(it.context, schedule))
            }
        }

        mScheduleViewModel = ViewModelProviders.of(this@ScheduleFragment).get(ScheduleViewModel::class.java)
        activity?.let {
            if (it.checkNetworkConnection()) mScheduleViewModel.fetchSchedule()
            else it.showNoInternetDialogDataOutOfDate()
        }
        mScheduleViewModel.eventsFromFirestore.observe(this@ScheduleFragment, androidx.lifecycle.Observer {
            it.forEach { firestoreEvent ->
                if (firestoreEvent.id.isNotEmpty()) {
                    val index = events.indexOfFirst { event -> event.id == firestoreEvent.id }
                    events[index].videoUrl = firestoreEvent.videoUrl
                    mAdapter.notifyItemChanged(index + 1)
                }
            }
        })

        view.scheduleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
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

        mDayViews = arrayOf(view.firstDay, view.secondDay, view.thirdDay, view.fourthDay, view.fifthDay)
        mDayViews.forEachIndexed { i, v ->
            v.setOnClickListener {
                layoutManager.scrollToPositionWithOffset(
                    positions[i],
                    10
                )
            }
        }
        mDaysBarLayout = view.daysBarLayout
        view.scheduleListLayout.removeView(mDaysBarLayout)
        (activity as MainActivity).addViewToAppBar(mDaysBarLayout)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        if (day in 8..12 && month == 7)
            mDayViews[day - 8].performClick()

        mBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById<View>(R.id.scheduleGuestSheet))
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                selectedGuest?.let {
                    GlideApp.with(this@ScheduleFragment)
                        .load(it.photoUrl)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(bottomSheet.guestPhoto)
                    bottomSheet.guestName.text = it.name
                    bottomSheet.guestDescription.text = it.description
                    bottomSheet.linksBarLayout.visibility = View.VISIBLE

                    bottomSheet.facebookImage.setImageResource(if (it.sites[0] != "") R.drawable.ic_facebook_color else R.drawable.ic_facebook_mono)
                    bottomSheet.instagramImage.setImageResource(if (it.sites[1] != "") R.drawable.ic_instagram_color else R.drawable.ic_instagram_mono)
                    bottomSheet.youtubeImage.setImageResource(if (it.sites[2] != "") R.drawable.ic_youtube_color else R.drawable.ic_youtube_mono)
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.scheduleRecyclerView.alpha = 1f
                    selectedGuest = null
                    bottomSheet.guestPhoto.setImageResource(android.R.color.transparent)
                    bottomSheet.guestName.text = ""
                    bottomSheet.guestDescription.text = ""
                    bottomSheet.linksBarLayout.visibility = View.INVISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    fun onItemClick(event: Event) {
        if (selectedGuest == null) when (event.eventType) {
            EventType.BREVIARY -> (requireActivity() as MainActivity).goToSelectedFragment(R.id.nav_breviary)
            EventType.CONCERT -> expandBottomSheet(GuestListFragment.concertGuests[event.guestIndex!!])
            EventType.CONFERENCE -> expandBottomSheet(GuestListFragment.conferenceGuests[event.guestIndex!!])
            EventType.MASS -> if (event.guestIndex != null) expandBottomSheet(GuestListFragment.conferenceGuests[event.guestIndex])
            else -> return
        } else hideBottomSheet()
    }

    private fun invalidateDayViews() {
        val daySelectedDrawable = view!!.context.getDrawable(R.drawable.day_selected)
        mDayViews.forEach {
            it.background = null
            it.setTextColor(view!!.context.getAttributeColor(R.attr.colorText))
        }
        mDayViews[mSelectedDay].background = daySelectedDrawable
        mDayViews[mSelectedDay].setTextColor(view!!.context.getAttributeColor(R.attr.colorBackground))
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
        view?.scheduleRecyclerView?.isEnabled = false
        view?.scheduleRecyclerView?.animate()
            ?.alpha(0.15f)
            ?.duration = 200
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
                "2019-07-08-01", "Poniedziałek", "8 Lipca", "8:30", "Rejestracja Uczestników",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2019-07-08-02", "Poniedziałek", "8 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-08-03", "Poniedziałek", "8 Lipca", "17:30", "Rozpoczęcie XXV Spotkania Młodych w Wołczynie",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2019-07-08-04", "Poniedziałek", "8 Lipca", "18:15", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-08-05", "Poniedziałek", "8 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-08-06", "Poniedziałek", "8 Lipca", "20:00", "Koncert: Wyrwani z Niewoli",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 2
            ),
            Event(
                "2019-07-08-07", "Poniedziałek", "8 Lipca", "20:00", "Tajemnica powołania - Q&A",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-08-08", "Poniedziałek", "8 Lipca", "21:30", "Nabożeństwo rozpoczęcia: \"RATUNKU!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-08-09", "Poniedziałek", "8 Lipca", "22:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-09-01", "Wtorek", "9 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-09-02", "Wtorek", "9 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-09-03", "Wtorek", "9 Lipca", "9:30", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-09-04", "Wtorek", "9 Lipca", "10:00", "\"Mój kościół zraniony\"\n- bp Edward Kawa OFMConv",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 1
            ),
            Event(
                "2019-07-09-05", "Wtorek", "9 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-09-06", "Wtorek", "9 Lipca", "11:30", "Eucharystia (bp Edward Kawa OFMConv)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 1
            ),
            Event(
                "2019-07-09-07", "Wtorek", "9 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-09-08", "Wtorek", "9 Lipca", "14:00", "Mecz: Kapucyni vs. reszta świata",
                EventPlace.COURT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-09-09", "Wtorek", "9 Lipca", "15:45", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-09-10", "Wtorek", "9 Lipca", "16:00", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-09-11", "Wtorek", "9 Lipca", "17:45", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-09-12", "Wtorek", "9 Lipca", "18:45", "Koncert: TATO",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 3
            ),
            Event(
                "2019-07-09-13", "Wtorek", "9 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-09-14", "Wtorek", "9 Lipca", "19:30", "Koncert: KapEl'a",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 0
            ),
            Event(
                "2019-07-09-15", "Wtorek", "9 Lipca", "21:00", "Urodziny",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            ),
            Event(
                "2019-07-09-16", "Wtorek", "9 Lipca", "21:40", "Nabożeństwo: \"ODKRYJ JEGO OBLICZE!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-09-17", "Wtorek", "9 Lipca", "22:40", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-10-01", "Środa", "10 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-10-02", "Środa", "10 Lipca", "8:15", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-10-03", "Środa", "10 Lipca", "9:30", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-10-04", "Środa", "10 Lipca", "10:00",
                "\"Gdzie szukać cegieł i zaprawy\"\n- o. Antonello Cadeddu",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 4
            ),
            Event(
                "2019-07-10-05", "Środa", "10 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-10-06", "Środa", "10 Lipca", "11:30",
                "Eucharystia (br. Tomasz Protasiewicz / o. Antonello Cadeddu)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 4
            ),
            Event(
                "2019-07-10-07", "Środa", "10 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-10-08", "Środa", "10 Lipca", "14:00", "Taniec z gwiazdami",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-10-09", "Środa", "10 Lipca", "15:15",
                "\"Kawalerka do wynajęcia czy dom na całe życie?\"\n- Michał \"PAX\" Bukowski",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 2
            ),
            Event(
                "2019-07-10-10", "Środa", "10 Lipca", "16:15", "Rozesłanie do fraterek",
                EventPlace.AMPHITHEATRE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-10-11", "Środa", "10 Lipca", "16:30", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-10-12", "Środa", "10 Lipca", "18:00", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-10-13", "Środa", "10 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-10-14", "Środa", "10 Lipca", "19:30", "Nabożeństwo pokutne: \"ODBUDUJ MÓJ KOŚCIÓŁ!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-10-15", "Środa", "10 Lipca", "22:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-11-01", "Czwartek", "11 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-11-02", "Czwartek", "11 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-11-03", "Czwartek", "11 Lipca", "9:00", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-11-04", "Czwartek", "11 Lipca", "10:00",
                "\"Czym umeblować żeby było ładnie?\"\n- br. Paweł Teperski",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 3
            ),
            Event(
                "2019-07-11-05", "Czwartek", "11 Lipca", "11:00", "Przygotowanie do Eucharystii",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-11-06", "Czwartek", "11 Lipca", "11:15",
                "Eucharystia (Bracia neoprezbiterzy / br. Paweł Teperski)",
                EventPlace.AMPHITHEATRE, EventType.MASS, 3
            ),
            Event(
                "2019-07-11-07", "Czwartek", "11 Lipca", "13:00", "Obiad",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-11-08", "Czwartek", "11 Lipca", "13:30", "Taniec z gwiazdami",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-11-09", "Czwartek", "11 Lipca", "15:00", "KORONKA",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-11-10", "Czwartek", "11 Lipca", "15:20",
                "\"Kiedy będzie doskonale\"\n- ks. Maciej Sarbinowski SDB",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 5
            ),
            Event(
                "2019-07-11-11", "Czwartek", "11 Lipca", "16:15", "Spotkanie we fraterkach",
                EventPlace.EVERYWHERE, EventType.GROUPS, null
            ),
            Event(
                "2019-07-11-12", "Czwartek", "11 Lipca", "18:00", "Kolacja",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-11-13", "Czwartek", "11 Lipca", "18:00", "Grill MF Tau",
                EventPlace.GARDEN, EventType.MEAL, null
            ),
            Event(
                "2019-07-11-14", "Czwartek", "11 Lipca", "19:00", "Nieszpory",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-11-15", "Czwartek", "11 Lipca", "19:30", "Koncert: niemaGOtu",
                EventPlace.AMPHITHEATRE, EventType.CONCERT, 1
            ),
            Event(
                "2019-07-11-16", "Czwartek", "11 Lipca", "19:30", "\"Klucz do ikony\"\n- br. Marcin Świąder",
                EventPlace.WHITE_TENT, EventType.EXTRA, null
            ),
            Event(
                "2019-07-11-17", "Czwartek", "11 Lipca", "21:30", "Nabożeństwo: \"DAJ SIĘ POKOCHAĆ!\"",
                EventPlace.AMPHITHEATRE, EventType.DEVOTION, null
            ),
            Event(
                "2019-07-11-18", "Czwartek", "11 Lipca", "22:30", "Podsumowanie dnia",
                EventPlace.AMPHITHEATRE, EventType.OTHER, null
            ),
            Event(
                "2019-07-12-01", "Piątek", "12 Lipca", "7:30", "Jutrznia",
                EventPlace.WHITE_TENT, EventType.BREVIARY, null
            ),
            Event(
                "2019-07-12-02", "Piątek", "12 Lipca", "8:00", "Śniadanie",
                EventPlace.CAMPSITE, EventType.MEAL, null
            ),
            Event(
                "2019-07-12-03", "Piątek", "12 Lipca", "9:30", "Modlitwa poranna/rozgrzewka",
                EventPlace.AMPHITHEATRE, EventType.PRAYER, null
            ),
            Event(
                "2019-07-12-04", "Piątek", "12 Lipca", "9:45",
                "\"Popatrzcie jak oni się miłują\"\n- Monika i Marcin Gomułkowie",
                EventPlace.AMPHITHEATRE, EventType.CONFERENCE, 0
            ),
            Event(
                "2019-07-12-05", "Piątek", "12 Lipca", "10:45", "Eucharystia (br. Tomasz Żak / br. Piotr Kowalski)",
                EventPlace.AMPHITHEATRE, EventType.MASS, null
            ),
            Event(
                "2019-07-12-06", "Piątek", "12 Lipca", "12:00", "Rozesłanie",
                EventPlace.AMPHITHEATRE, EventType.ORGANIZATION, null
            )
        )
    }
}
