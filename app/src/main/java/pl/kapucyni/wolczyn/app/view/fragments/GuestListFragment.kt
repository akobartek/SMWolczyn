package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_guest_list.view.*
import kotlinx.android.synthetic.main.layout_links_bar.view.*
import kotlinx.android.synthetic.main.sheet_fragment_guest_details.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Guest
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
import pl.kapucyni.wolczyn.app.view.adapters.GuestsRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class GuestListFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mAdapter: GuestsRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    var selectedGuest: Guest? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_guest_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val guestType = arguments!!.getInt("guestType")
        mAdapter = GuestsRecyclerAdapter(
            if (guestType == 0) conferenceGuests else concertGuests,
            this@GuestListFragment
        )
        view.guestsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        view.guestsRecyclerView.itemAnimator = DefaultItemAnimator()
        view.guestsRecyclerView.adapter = mAdapter
        view.guestsRecyclerView.scheduleLayoutAnimation()

        activity?.let {
            mViewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }

        mBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById<View>(R.id.guestSheet))
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                selectedGuest?.let {
                    GlideApp.with(this@GuestListFragment)
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
                    view.guestsRecyclerView.alpha = 1f
                    selectedGuest = null
                    bottomSheet.guestPhoto.setImageResource(android.R.color.transparent)
                    bottomSheet.guestName.text = ""
                    bottomSheet.guestDescription.text = ""
                    bottomSheet.linksBarLayout.visibility = View.INVISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        view.guestsListLayout.setOnClickListener { if (selectedGuest != null) hideBottomSheet() }
    }

    fun expandBottomSheet(guest: Guest) {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        selectedGuest = guest
        view?.guestsRecyclerView?.isEnabled = false
        view?.guestsRecyclerView?.animate()
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
        fun newInstance(guestType: Int): GuestListFragment {
            return GuestListFragment().apply {
                arguments = Bundle().apply {
                    putInt("guestType", guestType)
                }
            }
        }

        val conferenceGuests = arrayOf(
            Guest(
                "Monika i Marcin Gomułkowie",
                "Niesamowite małżeństwo, które swoim przykładem pokazuje jak budować rodzinę opartą na Panu Bogu. Ich radość oraz szczery uśmiech potrafią nakłonić nie jedno ucho do słuchania.",
                "https://www.solideo.pl/Strona/2017/%5B2017.10.19%5D%20Wiara%20jak%20Ziarnko%20Gorczycy%20Gomu%C5%82kowie/monika-marcin-gomulkowie.jpg",
                arrayOf(
                    "https://www.facebook.com/malzenstwojestboskie/",
                    "https://www.instagram.com/poczatekwiecznosci.pl/?hl=pl",
                    "https://www.youtube.com/channel/UC6t3U_MVHFvHHMLmwHXfYow"
                )
            ),
            Guest(
                "bp Edward Kawa",
                "Najmłodszy biskup rzymskokatolicki w Europie, biskup pomocniczy archidiecezji lwowskiej.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/bp-kawa-285x300.jpg"
            ),
            Guest(
                "Michał PAX Bukowski",
                "Swoim bezpośrednim i stanowczym przekazem skierowanym do młodzieży, inspiruje oraz wskazuje drogę nie tylko przez słowo, ale także muzykę.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/michal-bukowski-300x200.jpg",
                arrayOf(
                    "https://www.facebook.com/PAXVOBISCUM777/",
                    "",
                    "https://www.youtube.com/channel/UCSXsUhLnX-Eev4iW0EH_Y8Q"
                )
            ),
            Guest(
                "br. Paweł Teperski",
                "Wśród prelegentów nie może zabraknąć “jednego z naszych”. Przyleci do nas prosto z Rzymu, aby głosić Słowo Boże.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/brat-pawel-teperski-300x300.jpg",
                arrayOf("", "", "https://www.youtube.com/user/KapucyniPR")
            ),
            Guest(
                "Pe Antonello Cadeddu",
                "Nasze Spotkanie z roku na rok nabiera międzynarodowego charakteru. \uD83C\uDDEE\uD83C\uDDF9 Specjalnie dla nas wygłosi konferencję w Wołczynie.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/61357616_10156011304755706_6392214090223190016_n-225x300.jpg",
                arrayOf("https://www.facebook.com/padreantonello/", "", "")
            ),
            Guest(
                "ks. Maciej Sarbinowski",
                "Salezjanin, rekolekcjonista, prowadzący stronę\nhttp://e-rekolekcje.pl/\nKapłan który poprzez proste tłumaczenie dociera do młodzieży, aby ta mogła zrozumieć i pójść za przykładem Chrystusa.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/ksi%C4%85d%C5%BA-Maciej-sarbinowski-300x300.jpg",
                arrayOf(
                    "https://www.facebook.com/sarbinowskimaciej",
                    "",
                    "https://www.youtube.com/channel/UCztHzRRchTbb6vYQUahfGLw"
                )
            )
        )

        val concertGuests = arrayOf(
            Guest(
                "KapEl'a",
                "Bez nich nie byłoby Wołczyna. Oczywiście w tym roku też ich nie zabraknie. Wszyscy ich znają i kochają. W jakim składzie zagrają w tym roku?  Niespodzianka! Przyjedź i przekonaj się sam!",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/P7110796-768x512.jpg",
                arrayOf("", "", "https://www.youtube.com/user/WolczynSpotkanie")
            ),
            Guest(
                "niemaGOtu",
                "Twórcy hymnu ŚDM Kraków 2016 Błogosławieni miłosierni. Zespół, który powstał w 2015r.  i zdobył już tysiące fanów. Ich hitem okazała się piosenka Nie mądrość świata tego [Marana tha!]. Swoim talentem głoszą radość płynącą z Ewangelii.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/nmgt.png",
                arrayOf(
                    "https://www.facebook.com/niemaGOtu/",
                    "https://www.instagram.com/niemagotu/?hl=pl",
                    "https://www.youtube.com/channel/UCECTIQn6hrLymIrLcCnIRVg"
                )
            ),
            Guest(
                "Wyrwani z niewoli",
                "Zachwycają oraz inspirują młodych ludzi swoim nowatorskim podejściem do ewangelizacji. Ich przekaz dociera do wielu serc. Najważniejsze dla nich jest głoszenie dobrej nowiny i żywego świadectwa, które niosą do młodzieży z całej Polski.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/wznw-768x740.png",
                arrayOf(
                    "https://www.facebook.com/wyrwanizniewoli/",
                    "https://www.instagram.com/hereswzn/?hl=pl",
                    "https://www.youtube.com/channel/UCdYVlDLZEAMpLGnRbCoAt5w"
                )
            ),
            Guest(
                "tato",
                "Duet grający muzykę około elektroniczną tworzony przez młodych mężczyzn. Mają wspólny cel – dostać się do Nieba i wciągnąć tam jak najwięcej osób. Ich złotym środkiem jest pasja do muzyki, którą pragną się dzielić.",
                "https://wolczyn.kapucyni.pl/wp-content/uploads/2019/05/logo-TATO--768x768.jpg",
                arrayOf(
                    "https://www.facebook.com/TATO-123488635158999/",
                    "",
                    "https://www.youtube.com/channel/UCppDQpN_zoqZATwPT5YBSlA"
                )
            )
        )
    }
}
