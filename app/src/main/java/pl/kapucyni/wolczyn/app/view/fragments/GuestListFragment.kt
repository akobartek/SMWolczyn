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
                "Tymoteusz Filar",
                "Student teologii, lider wspólnoty Hesed",
                "https://glosnapustyni.pl/media/118076355_1862725263867993_8249642876235377388_o-240x250.jpg",
                arrayOf(
                    "https://www.facebook.com/tymoteusz.filar",
                    "https://www.instagram.com/tymoteusz.filar/",
                    "",
                    ""
                )
            ),
            Guest(
                "bp Damian Dryl",
                "Doktor nauk teologicznych, biskup pomocniczy poznański w latach 2013–2021, biskup diecezjalny kaliski od 2021.",
                "https://wiez.pl/wp-content/uploads/2021/01/Bp-Damian-Bryl-1408x1000.jpg"
            ),
            Guest(
                "Sebastian Kubis",
                "Fizyk, absolwent Uniwersytetu Jagiellońskiego, (specjalność: astrofizyka), doktorat i  habilitacja w Instytucie Fizyki Jądrowej PAN w Krakowie. Zatrudniony na Politechnice Krakowskiej od 2012 roku.",
                "https://imf.pk.edu.pl/download/deb8bbc9afdc584f6d78d90ed2e95283/seb2018-small.jpg"
            ),
            Guest(
                "br. Maciej Jabłoński",
                "Brat Kapucyn, misjonarz posługujący w Republice Środkowoafrykańskiej",
                "https://scontent-frx5-2.xx.fbcdn.net/v/t39.30808-1/276056693_413665843897082_2163328572252112313_n.jpg?stp=c33.0.200.200a_dst-jpg_p200x200&_nc_cat=109&ccb=1-7&_nc_sid=c6021c&_nc_ohc=Rt_-O0w0Q38AX9S7PCO&_nc_ht=scontent-frx5-2.xx&oh=00_AT-VtJQZ3faSQVbT4ceT69GBFsTZdPZuPbxQ0zwhI76l1w&oe=62AD137D",
                arrayOf(
                    "https://www.facebook.com/profile.php?id=100057610082556",
                    "",
                    "https://zrzutka.pl/3h97h3",
                    ""
                )
            ),
            Guest(
                "s. Aleksandra Szyborska",
                "Siostra Zgromadzenia Sióstr Uczennic Boskiego Mistrza, pracuje w Radiu Jasna Góra.",
                "https://www.radiojasnagora.pl/media/u/mini/x3e602a0c87fc9ebecf5ba5b985992e4c.jpg.pagespeed.ic.84MpFbc5LE.jpg",
                arrayOf(
                    "https://www.facebook.com/aleksandra.szyborska",
                    "",
                    "https://www.radiojasnagora.pl/u21-s-aleksandra-szyborska",
                    ""
                )
            ),
            Guest(
                "Magdalena Myjak",
                "Dziewica konsekrowana, wokalistka zespołu \"Mocni w Duchu\".",
                "https://profeto.pl/Image/GetAvatar?authorId=223",
                arrayOf(
                    "https://www.facebook.com/mocniwduchu",
                    "https://www.instagram.com/mocniwduchu/",
                    "https://mocni.jezuici.pl/osoby/28/90/magda-myjak",
                    "https://www.youtube.com/channel/UC9BaRiZ_E9_o2_sxwDqn5dg"
                )
            ),
            Guest(
                "ks. Michał Pabiańczyk",
                "Ojciec duchowny w WSD w Częstochowie.",
                "https://fiat.fm/wp-content/uploads/2018/02/ks_Michal_Pabiancczyk-720x480.jpg",
                arrayOf(
                    "",
                    "",
                    "http://www.seminarium.czest.pl/",
                    ""
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
                "Carrantuohill",
                "Założony w 1987 roku polski zespół, wykonuje zarówno tradycyjną muzykę celtycką rodem z Irlandii i Szkocji, jak i własne opracowania aranżacyjne oparte na \"celtyckich korzeniach\".",
                "https://scontent-frx5-1.xx.fbcdn.net/v/t1.6435-9/38638961_10155711429550665_5314941403673919488_n.png?_nc_cat=100&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=AFG0dT66bMsAX9puMe-&_nc_ht=scontent-frx5-1.xx&oh=00_AT_ceARwG-SkHfNSnxuWBVrVUJWh8bOLBF4FF4nHKsSdlg&oe=62CE32DB",
                arrayOf(
                    "https://www.facebook.com/Carrantuohill/",
                    "https://www.instagram.com/carrantuohill/",
                    "https://www.carrantuohill.pl/",
                    "https://www.youtube.com/user/carrantuohillcelt"
                )
            ),
            Guest(
                "Muode Koty",
                "Muode Koty to hip-hopowy zespół chrześcijański, który oprócz koncertów prowadzi również rekolekcje i profilaktykę w szkołach. Mimo swojego młodego wieku mają już na swoim koncie parę wygranych konkursów oraz wielkich koncertów i festiwali",
                "https://scontent-frt3-1.xx.fbcdn.net/v/t39.30808-6/242237280_3030357200566088_3465946037882297166_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=Yb6yMK6NfMYAX9-aKjb&_nc_ht=scontent-frt3-1.xx&oh=00_AT_r4795eFNqppkPdISUhI-gkpsbhYnKji33XXYIKRr5Mg&oe=62ADD8E0",
                arrayOf(
                    "https://www.facebook.com/muodekoty/",
                    "https://www.instagram.com/muodekoty/",
                    "https://www.muodekoty.com/",
                    "https://www.youtube.com/channel/UCkH1V5MITxoiCkw5QJg44ew/"
                )
            ),
            Guest(
                "SOWINSKY",
                "Krzysztof i Maja Sowińscy. Należą do wspólnoty uwielbienia „Głos Pana”, są założycielami fundacji SOWINSKY, której celami jest ewangelizacja, działalność profilaktyczna oraz twórczorść i jej promowanie.",
                "https://www.sowinsky.pl/wp-content/uploads/2019/12/razem1-600x400.png",
                arrayOf(
                    "https://www.facebook.com/sowinsky7/",
                    "https://www.instagram.com/so.win.sky/",
                    "https://www.sowinsky.pl/",
                    "https://www.youtube.com/channel/UCuRLbbAPGvPMR2OJYDmgJjg"
                )
            )
        )
    }
}
