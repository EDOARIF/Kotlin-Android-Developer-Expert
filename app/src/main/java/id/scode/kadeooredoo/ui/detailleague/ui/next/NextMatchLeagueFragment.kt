package id.scode.kadeooredoo.ui.detailleague.ui.next

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import id.scode.kadeooredoo.R
import id.scode.kadeooredoo.data.db.entities.EventNext
import id.scode.kadeooredoo.data.db.network.ApiRepository
import id.scode.kadeooredoo.gone
import id.scode.kadeooredoo.invisible
import id.scode.kadeooredoo.ui.detailleague.adapter.RvNextMatchLeague
import id.scode.kadeooredoo.ui.detailleague.presenter.NextPresenter
import id.scode.kadeooredoo.ui.detailleague.ui.detailnextorprevandfavorite.DetailMatchLeagueActivity
import id.scode.kadeooredoo.ui.detailleague.view.NextMatchLeagueView
import id.scode.kadeooredoo.visible
import kotlinx.android.synthetic.main.fragment_next.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast

class NextMatchLeagueFragment : Fragment(), NextMatchLeagueView, AnkoLogger {

    private lateinit var nextLeagueViewModel: NextLeagueViewModel
    private var idLeague: String? = null
    /**
     * apply the TeamsPresenter and MainAdapter
     * to the this context
     */
    private var eventNextMutableList: MutableList<EventNext> = mutableListOf()
    private lateinit var nextPresenter: NextPresenter
    private lateinit var rvNextMatchLeagueAdapter: RvNextMatchLeague
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nextLeagueViewModel =
            ViewModelProviders.of(this).get(NextLeagueViewModel::class.java)

        // initialize binding
        val root = inflater.inflate(R.layout.fragment_next, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        progressBar = root.findViewById(R.id.progress_detail_next)
        recyclerView = root.findViewById(R.id.rv_next_match_leagues)

        //set the layout
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        // get pass data args
        val idLeagueKey = resources.getString(R.string.key_id_league)
        idLeague = arguments?.getString(idLeagueKey)

        // init the presenter for injecting the constructor
        val request = ApiRepository()
        val gson = Gson()
        nextPresenter = NextPresenter(this, request, gson)

        idLeague?.let { id ->
            // call the api
            nextPresenter.getNextLeagueList(id)
            info ("get data with $id")

            // test obs
            nextLeagueViewModel.text.observe(this, Observer {
                textView.text = "next $id"
            })
        }

        /**
         * declare & initialize adapter and presenter
         * for the callBack a getLeagueTeamList
         */
        rvNextMatchLeagueAdapter = activity?.applicationContext?.let { context ->
            RvNextMatchLeague(
                context,
                eventNextMutableList
            ) {
                info(
                    """
                    date : ${it.strAwayFormation}
                    """.trimIndent()
                )
                context.startActivity<DetailMatchLeagueActivity>(DETAIL_NEXT_MATCH_LEAGUE to it)
            }
        }!!
        recyclerView.adapter = rvNextMatchLeagueAdapter

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarNext = view.findViewById<Toolbar>(R.id.toolbar_next)
        ((activity as AppCompatActivity)).setSupportActionBar(toolbarNext)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_next, menu)

        // search view with
        val searchManager = activity?.applicationContext?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.option_search_next)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        searchView.queryHint = resources.getString(R.string.option_search_next)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                info("Search View onQueryTextSubmit")
                toast(query)
                resultSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                info("Search View onQueryTextChange")
//                resultAdapter.filter.filter(newText)
                return false
            }
        })
        searchView.setOnCloseListener {
            rvNextMatchLeagueAdapter.filter.filter("")
            true
        }
    }

    private fun resultSearch(query: String) {
        nextPresenter.getSearchNextLeagueList(query)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showNextLeague(data: List<EventNext>?) {
        info("try show next event past list : process")
        eventNextMutableList.clear()
        data?.let {
            eventNextMutableList.addAll(it)
        }
        rvNextMatchLeagueAdapter.notifyDataSetChanged()
        info("try show next event past list : done")

        if (eventNextMutableList.isNullOrEmpty()){
            toast(getString(R.string.exception_search_not_found))
            img_exception_search_nf_fn?.visible()
            rv_next_match_leagues?.gone()
        } else {
            img_exception_search_nf_fn?.gone()
            rv_next_match_leagues?.visible()
            info("hello prev ${eventNextMutableList[0].idHomeTeam}")

        }
    }
    override fun exceptionNullObject(msg: String) {
        toast("$msg ${getString(R.string.exception_search_not_found)}")
        img_exception_search_nf_fn.visible()
        rv_next_match_leagues.gone()
    }

    companion object{
        const val DETAIL_NEXT_MATCH_LEAGUE = "detail_next_match_legaue"
    }
}