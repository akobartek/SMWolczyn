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
        val guestType = requireArguments().getInt("guestType")
        mAdapter = GuestsRecyclerAdapter(
            if (guestType == 0) conferenceGuests else concertGuests,
            this@GuestListFragment
        )
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

        val conferenceGuests = arrayOf(
            Guest(
                "ks. Jakub Bartczak",
                "Duszpasterz parafii Sulistrowice  (Święcenia kapłańskie 2007r.). Niegdyś znany również jako „Mane\", członek hip-hopowych ekip z Wrocławia (Drugi komplet, Drutz). W czasie kapłaństwa autor czterech albumów: \"Powołanie\", \"Po prostu wierze\", \"Bóg jest działa\", \"#Siemodle\".",
                "https://www.niedziela.pl/gifs/portaln/624x400/1602229833.jpg",
                arrayOf(
                    "https://www.facebook.com/KsiadzJakubBartczak",
                    "https://www.instagram.com/ksjakubbartczak/",
                    "https://kskubabartczak.pl/",
                    "https://www.youtube.com/user/ksjakubbartczak"
                )
            ),
            Guest(
                "bp Damian Dryl",
                "Doktor nauk teologicznych, biskup pomocniczy poznański w latach 2013–2021, biskup diecezjalny kaliski od 2021.",
                "https://wiez.pl/wp-content/uploads/2021/01/Bp-Damian-Bryl-1408x1000.jpg"
            ),
            Guest(
                "Maja i Krzysztof Sowińscy",
                "Należą do wspólnoty uwielbienia „Głos Pana”, są założycielami fundacji SOWINSKY, której celami jest ewangelizacja, działalność profilaktyczna oraz twórczorść i jej promowanie.",
                "https://www.sowinsky.pl/wp-content/uploads/2019/12/razem1-600x400.png",
                arrayOf(
                    "https://www.facebook.com/sowinsky7/",
                    "https://www.instagram.com/so.win.sky/",
                    "https://www.sowinsky.pl/",
                    "https://www.youtube.com/channel/UCuRLbbAPGvPMR2OJYDmgJjg"
                )
            ),
            Guest(
                "ks. Grzegorz Szczygieł MS",
                "Saletyn, redaktor naczelny „La Salette” Posłaniec Matki Bożej Saletyńskiej.",
                "https://saletyni.pl/wp-content/uploads/2018/09/szcz-150x150.jpg",
                arrayOf(
                    "", "",
                    "https://saletyni.pl/author/g-szczygiel/",
                    "https://www.youtube.com/channel/UCEAr1mHybyZb4-QvlX108Sw"
                )
            ),
            Guest(
                "ks. Michał Olszewki SCJ",
                "Sercanin, rekolekcjonista, dyrektor Grupy Profeto, rzecznik Zespołu KEP ds. Nowej Ewangelizacji.",
                "https://www.niedziela.pl/gifs/portaln/624x400/1454942603.jpg",
                arrayOf(
                    "https://www.facebook.com/KsMichalOlszewski",
                    "https://www.instagram.com/profeto_pl",
                    "https://profeto.pl/",
                    "https://www.youtube.com/channel/UCCNzrPbGCa252PGRsRwnmOA"
                )
            )
        )

        val concertGuests = arrayOf(
            Guest(
                "KapEl'a",
                "Bez nich nie byłoby Wołczyna. Oczywiście w tym roku też ich nie zabraknie. Wszyscy ich znają i kochają. W jakim składzie zagrają w tym roku?  Niespodzianka! Przyjedź i przekonaj się sam!",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/P7110796-768x512.jpg",
                arrayOf("", "", "", "https://www.youtube.com/user/WolczynSpotkanie")
            ),
            Guest(
                "ks. Jakub Bartczak",
                "Duszpasterz parafii Sulistrowice  (Święcenia kapłańskie 2007r.). Niegdyś znany również jako „Mane”, członek hip-hopowych ekip z Wrocławia (Drugi komplet, Drutz). W czasie kapłaństwa autor czterech albumów: \"Powołanie\", \"Po prostu wierze\", \"Bóg jest działa\", \"#Siemodle\".",
                "https://www.niedziela.pl/gifs/portaln/624x400/1602229833.jpg",
                arrayOf(
                    "https://www.facebook.com/KsiadzJakubBartczak",
                    "https://www.instagram.com/ksjakubbartczak/",
                    "https://kskubabartczak.pl/",
                    "https://www.youtube.com/user/ksjakubbartczak"
                )
            ),
            Guest(
                "LUXTORPEDA",
                "Polska grupa wykonująca szeroko pojętą muzykę rockową. Powstała w 2010 roku z inicjatywy gitarzysty i wokalisty Roberta Friedricha, znanego z zespołów: Acid Drinkers, Arka Noego, Kazik na Żywo i 2Tm2,3. Muzyk do współpracy zaprosił związanych z zespołem 2Tm2,3 gitarzystę Roberta Drężka i basistę Krzysztofa Kmiecika oraz ówczesnego perkusistę Turbo i zespołu Armia Tomasza Krzyżaniaka. W 2011 roku w trakcie nagrywania albumu skład uzupełnił wokalista Przemysław „Hans” Frencel, raper znany z duetu Pięć Dwa.",
                "https://staryklasztor.com.pl/wp-content/uploads/2018/12/lux-900x804.jpg",
                arrayOf(
                    "https://www.facebook.com/LUXTORPEDA",
                    "", "https://www.luxtorpeda.eu/",
                    "https://www.youtube.com/user/OFFICIALLUXTORPEDA/"
                )
            )
        )
    }
}
