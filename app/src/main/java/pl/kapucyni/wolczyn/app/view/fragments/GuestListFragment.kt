package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentGuestListBinding
import pl.kapucyni.wolczyn.app.model.Guest
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
import pl.kapucyni.wolczyn.app.view.adapters.GuestsRecyclerAdapter

class GuestListFragment : BindingFragment<FragmentGuestListBinding>() {

    private lateinit var mAdapter: GuestsRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    var selectedGuest: Guest? = null

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGuestListBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        mAdapter = GuestsRecyclerAdapter(guests, this@GuestListFragment)
        binding.guestsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
            scheduleLayoutAnimation()
        }

        mBottomSheetBehavior = BottomSheetBehavior.from(binding.guestSheet)
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val guestDetailsFragment =
                    childFragmentManager.findFragmentById(R.id.guestSheet) as GuestDetailsFragment
                selectedGuest?.let {
                    guestDetailsFragment.setViewsValues(it)
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.guestsRecyclerView.alpha = 1f
                    selectedGuest = null
                    guestDetailsFragment.hideViews()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.guestsListLayout.setOnClickListener { if (selectedGuest != null) hideBottomSheet() }
    }

    fun expandBottomSheet(guest: Guest) {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        selectedGuest = guest
        binding.guestsRecyclerView.isEnabled = false
        binding.guestsRecyclerView.animate().alpha(0.15f).duration = 200
    }

    fun hideBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun onBackPressed(): Boolean =
        if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            true
        } else {
            hideBottomSheet()
            false
        }

    fun onIconClick(iconNumber: Int) {
        selectedGuest?.let {
            if (it.sites[iconNumber] != "")
                requireContext().openWebsiteInCustomTabsService(it.sites[iconNumber])
        }
    }

    companion object {
        fun newInstance(guestType: Int): GuestListFragment {
            return GuestListFragment().apply {
                arguments = Bundle().apply {
                    putInt("guestType", guestType)
                }
            }
        }

        val guests = arrayOf(
            Guest(
                "KapEl'a",
                "Bez nich nie byłoby Wołczyna. Oczywiście w tym roku też ich nie zabraknie. Wszyscy ich znają i kochają. W jakim składzie zagrają w tym roku? Niespodzianka! Przyjedź i przekonaj się sam!",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/P7110796-768x512.jpg",
                arrayOf("", "", "", "", "https://www.youtube.com/user/WolczynSpotkanie/")
            ),
            Guest(
                "ks. Sebastian Kosecki",
                "Ksiądz pochodzący z Częstochowy, który żyje swoją wiarą i dzieli się nią poprzez media społeczniościowe.",
                "https://scontent.fktw5-1.fna.fbcdn.net/v/t39.30808-6/306150193_5519048851518025_2112752686275974803_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=okiUJFvQwQkAX_YqZFU&_nc_ht=scontent.fktw5-1.fna&oh=00_AfAzt2MFbgJ3krm_iA40ZzrZccwEYvgbY25aIKUIh45Bwg&oe=649D7509",
                arrayOf(
                    "https://www.facebook.com/sebastian.kosecki.35/",
                    "https://www.instagram.com/sebq918/",
                    "https://www.tiktok.com/@ks.sebastian.kosecki/",
                    "",
                    "https://www.youtube.com/@ks.sebastian.kosecki/"
                )
            ),
            Guest(
                "Sezon na czereśnie",
                "Sezon na czereśnie to taki czas, gdzie wszystko wydaje się być piękniejsze. Drzewa zaczynają kwitnąć, trawa staje się bardziej zielona, docierają do nas pierwsze ciepłe promienie słońca. To wszystko sprawia, że zaczynamy mieć więcej energii do działania, stajemy się szczęśliwi. Dlatego też każdym koncertem chcą otwierać sezon na czereśnie i uwielbiać Boga.",
                "https://scontent.fktw5-1.fna.fbcdn.net/v/t39.30808-6/344080776_916063042781682_1156101957148802007_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=XpvbmobhLXMAX9Ym3fU&_nc_ht=scontent.fktw5-1.fna&oh=00_AfBzTtmakXt8G9gOlrQKnRkYTGjoodml6ZyHA6kT1su5ag&oe=649D1C5A",
                arrayOf(
                    "https://www.facebook.com/sezon.na.czeresnie/",
                    "https://www.instagram.com/sezonnaczeresnie/",
                    "",
                    "https://sezonnaczeresnie.pl/",
                    "https://www.youtube.com/@sezonnaczeresnie/"
                )
            ),
            Guest(
                "Wyrwani z niewoli",
                "Zachwycają oraz inspirują młodych ludzi swoim nowatorskim podejściem do ewangelizacji. Ich przekaz dociera do wielu serc. Najważniejsze dla nich jest głoszenie dobrej nowiny i żywego świadectwa, które niosą do młodzieży z całej Polski.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/wznw-768x740.png",
                arrayOf(
                    "https://www.facebook.com/hereswzn/",
                    "https://www.instagram.com/hereswzn/",
                    "https://www.tiktok.com/@heres_wyrwanizniewoli/",
                    "",
                    "https://www.youtube.com/c/WYRWANIZNIEWOLItv/"
                )
            ),
            Guest(
                "Barbara Turek",
                "Pomimo poruszania się na wózku, stara się prowadzić aktywne życie, tak aby żyć \"pełnym garściami\". Z wykształcenia pedagog i coach.",
                "https://scontent.fktw5-1.fna.fbcdn.net/v/t39.30808-6/293256232_413865477452430_8426853892457880830_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=YE3n3J5_Gi0AX8wE2IZ&_nc_ht=scontent.fktw5-1.fna&oh=00_AfBwW2hfOMbr9B9fDeeY13s8CyItPmsyYJ0AeYh7AJ-JGw&oe=649E41E6",
                arrayOf(
                    "https://www.facebook.com/basiapelnymigarsciami/",
                    "https://www.instagram.com/barbaraturek/",
                    "",
                    "https://www.fundacjaavalon.pl/nasi_beneficjenci/barbara_turek_16860/",
                    "https://www.youtube.com/@penymigarsciami6511/"
                )
            ),
            Guest(
                "Anna Madej",
                "Śpiewa oraz dyryguje w wielu miejscach, chwaląc Pana. Jej wokal jest znany każdemu w Wołczynie, gdzie od lat pilnuje Spotkania Młodych od strony muzycznej. Tym razem wraz z miejscową młodzieżą przygotują nam kolejne muzyczne doznania.",
                "https://scontent.fktw5-1.fna.fbcdn.net/v/t39.30808-6/346774290_277047971325472_8219792603232167613_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=V0boSwELfmIAX9HZViR&_nc_ht=scontent.fktw5-1.fna&oh=00_AfBDdOP1kRZWO-KHoKOz9KuAzchppA1lV1baD1OZx6kM1Q&oe=649E0795",
                arrayOf(
                    "https://www.facebook.com/madej.ann/",
                    "https://www.instagram.com/madejan.ka/",
                    "",
                    "",
                    "https://www.youtube.com/@madejanka"
                )
            ),
        )
    }
}
