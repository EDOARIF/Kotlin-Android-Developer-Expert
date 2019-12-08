package id.scode.kadeooredoo.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import id.scode.kadeooredoo.R
import id.scode.kadeooredoo.data.db.entities.Team
import id.scode.kadeooredoo.data.db.network.ApiRepository
import id.scode.kadeooredoo.gone
import id.scode.kadeooredoo.ui.detailLeague.ui.DetailLeagueActivity
import id.scode.kadeooredoo.ui.home.adapter.TeamsAdapter
import id.scode.kadeooredoo.ui.home.presenter.TeamsPresenter
import id.scode.kadeooredoo.ui.home.view.TeamsView
import id.scode.kadeooredoo.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * A simple [Fragment] subclass.
 */
class TeamsFragment : Fragment(), AnkoComponent<Context>, AnkoLogger, TeamsView {

    // declare a view
    private lateinit var recyclerViewListTeam: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayoutListTeam: SwipeRefreshLayout
    private lateinit var spinner: Spinner
    private lateinit var btnDet: Button

    /**
     * apply the TeamsPresenter and MainAdapter
     * to the this context
     */
    private var teamsMutableList: MutableList<Team> = mutableListOf()
    private lateinit var teamsPresenter: TeamsPresenter
    private lateinit var teamsAdapter: TeamsAdapter

    //declare a view for choose
    private lateinit var leagueName: String //for spinner


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // spinner config
        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spinnerItems
        )
        spinner.adapter = spinnerAdapter

        /**
         * declare & initialize adapter and presenter
         * for the callBack a getLeagueTeamList
         */
        teamsAdapter = TeamsAdapter(requireContext(), teamsMutableList) {
            info(
                """
                recycle got clicked
                id league ${it.idLeague}
                |
                id team ${it.teamId}
                |
                id Soccer XML ${it.idSoccerXML}
                |
                team name ${it.teamName}
            """.trimIndent()
            )
            debug(8)
            error(null)
            startActivity<TeamsDetailActivity>(DETAIL_KEY to it.teamId) //intent with the obj
        }
        recyclerViewListTeam.adapter = teamsAdapter


        val request = ApiRepository()
        val gson = Gson()
        teamsPresenter = TeamsPresenter(this, request, gson)

        // idLeague INIT LOKAL
        var idLeague: String

        // spinner listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                info("spinner selected ${spinner.selectedItem}")
                leagueName = spinner.selectedItem.toString()

                btnDet.setOnClickListener {
                    when (leagueName) {
                        getString(R.string.league_epl) -> {
                            idLeague = getString(R.string.league_epl_id)
                            startActivity<DetailLeagueActivity>(DETAIL_LEAGUE to idLeague)
                        }
                        getString(R.string.league_elc) -> {
                            idLeague = getString(R.string.league_elc_id)
                            startActivity<DetailLeagueActivity>(DETAIL_LEAGUE to idLeague)
                        }
                        getString(R.string.league_gb) -> {
                            idLeague = getString(R.string.league_gb_id)
                            startActivity<DetailLeagueActivity>(DETAIL_LEAGUE to idLeague)
                        }
                        getString(R.string.league_isa) -> {
                            idLeague = getString(R.string.league_isa_id)
                            startActivity<DetailLeagueActivity>(DETAIL_LEAGUE to idLeague)
                        }
                        getString(R.string.league_fl1) -> {
                            idLeague = getString(R.string.league_fl1_id)
                            startActivity<DetailLeagueActivity>(DETAIL_LEAGUE to idLeague)
                        }
                        getString(R.string.league_sll) -> {
                            idLeague = getString(R.string.league_sll_id)
                            startActivity<DetailLeagueActivity>(DETAIL_LEAGUE to idLeague)
                        }
                    }

                }

                teamsPresenter.getLeagueTeamList(leagueName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                info("onNothingSelected")
            }
        }

        // pull-down
        swipeRefreshLayoutListTeam.onRefresh {
            info("try swipe to refresh on select ${spinner.selectedItem}")
            teamsPresenter.getLeagueTeamList(spinner.selectedItem.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    @SuppressLint("ResourceAsColor")
    override fun createView(ui: AnkoContext<Context>): View = with(ui) {

        // view
        verticalLayout {

            linearLayout {
                lparams(width = matchParent, height = matchParent)
                orientation = LinearLayout.VERTICAL
                topPadding = dip(8)
                leftPadding = dip(8)
                rightPadding = dip(8)

                toolbar {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        logo =
                            resources.getDrawable(R.drawable.ic_home_green_400_24dp, context.theme)
                        title = context.getString(R.string.main_activity_title_for_layout)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            titleMarginStart = dip(32)
                        }
                        elevation = 12f
                        backgroundColor = R.color.colorPrimaryBar
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            setBackgroundColor(
                                resources.getColor(
                                    R.color.colorPrimaryBar,
                                    context.theme
                                )
                            )
                        }
                    }
                }.lparams(matchParent, wrapContent) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        margin = dip(16)
                    }
                }

                spinner = spinner {
                    id = R.id.spinner
                }

                btnDet = button("Detail League") {
                    id = R.id.btn_det_1
                    allCaps = false
                }.lparams(width = matchParent, height = wrapContent) {
                    gravity = Gravity.BOTTOM.and(Gravity.END)
                    margin = dip(8)
                }
                swipeRefreshLayoutListTeam = swipeRefreshLayout {
                    setColorSchemeColors(
                        R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light
                    )
                    relativeLayout {
                        //                    lparams(width = matchParent, height = wrapContent)

                        recyclerViewListTeam = recyclerView {
                            lparams(width = matchParent, height = dip(400))
                            layoutManager = LinearLayoutManager(context)
                        }

                        progressBar = progressBar {
                        }.lparams {
                            centerHorizontally()
                        }
                    }
                }.lparams(width = matchParent, height = wrapContent)


            }


        } //end of view

    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }

    override fun showTeamList(data: List<Team>?) {
        info("try show team list : process")
        swipeRefreshLayoutListTeam.isRefreshing = false
        teamsMutableList.clear()
        data?.let {
            teamsMutableList.addAll(it)
        }
        teamsAdapter.notifyDataSetChanged()
        info("try show team list : done")
    }

    override fun showTeamAwayList(data: List<Team>?) {
        // just for inside adapter previous and next
    }

    companion object {
        const val DETAIL_KEY = "detail_key" //PAIR key for getParcelAble data obj
        const val DETAIL_LEAGUE = "detail_league"
    }

}