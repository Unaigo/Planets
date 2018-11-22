package com.planets.unai.planets

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.planets.unai.planets.Logic.PlanetConentManager
import com.planets.unai.planets.Models.ClientModels.PlanetData
import com.planets.unai.planets.ServerConnection.INetworkAPI
import com.planets.unai.planets.Views.PaginationScrollListener
import com.planets.unai.planets.Views.SimpleItemRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_planetitem_list.*
import kotlinx.android.synthetic.main.planetitem_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PlanetItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PlanetItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var isSearching : Boolean = false;
    var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planetitem_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        planetitem_list.layoutManager= linearLayoutManager;

        if (planetitem_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        if(!PlanetConentManager.ITEMS.any()) {
            if(isOnline())
            {
                callServerApi()
            }
            else
            {
                Toast.makeText(this, "Check your network status...", Toast.LENGTH_SHORT).show()
            }

        }
        else
        {
            planetitem_list.adapter = SimpleItemRecyclerViewAdapter(this,
                PlanetConentManager.ITEMS as ArrayList<PlanetData>, twoPane)
        }

        planetitem_list?.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if(!isSearching)
                {
                    isLoading = true
                    //you have to call loadmore items to get more data
                    getMoreItems()
                }

            }
        })


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search = menu?.findItem(R.id.action_search)?.actionView as android.support.v7.widget.SearchView
        val si = manager.getSearchableInfo(componentName)

        var opt : Int = search.imeOptions
        search.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        search.setSearchableInfo(si)
        search.setIconifiedByDefault(false)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.toString().isNotEmpty()) {
                    progressBarSearch.setVisibility(View.VISIBLE);
                    search(query)
                    isSearching = true;
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.toString().isNotEmpty()) {


                }
                else {
                    isSearching = false;
                    progressBarSearch.setVisibility(View.INVISIBLE);
                    //RESET SEARCH AND SHOW INITIAL LIST
                    planetitem_list.adapter = SimpleItemRecyclerViewAdapter(
                        this@PlanetItemListActivity,
                       PlanetConentManager.ITEMS as ArrayList<PlanetData>, twoPane)
                }
                return false
            }
        })


        return true
    }

    fun isOnline(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.getActiveNetworkInfo()
        return netInfo != null && netInfo.isConnectedOrConnecting()
    }

    fun callServerApi()
    {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://swapi.co/api/").build()

        val postsApi = retrofit.create(INetworkAPI::class.java)

        var response = postsApi.getAllPlanets()

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            PlanetConentManager.setPlanetsServerAndFactory(it)
            planetitem_list.adapter = SimpleItemRecyclerViewAdapter(this,
                PlanetConentManager.ITEMS as ArrayList<PlanetData>, twoPane)
        }
    }

    fun getMoreItems() {
        if(PlanetConentManager.NEXT_URL!="") {
            progressBarPagination.setVisibility(View.VISIBLE);
            var retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://swapi.co/api/").build()

            var postsApi = retrofit.create(INetworkAPI::class.java)

            var response = postsApi.getNext(PlanetConentManager.NEXT_URL)

            response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
                PlanetConentManager.setPlanetsServerAndFactory(it)
                isLoading = false
                progressBarPagination.setVisibility(View.INVISIBLE);
                (planetitem_list.adapter as SimpleItemRecyclerViewAdapter).addData()
            }
        }
        else
        {
            isLastPage = true
            progressBarPagination.setVisibility(View.INVISIBLE);

        }

    }

    fun search(searchText : String)
    {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://swapi.co/api/").build()

        val postsApi = retrofit.create(INetworkAPI::class.java)

        var response = postsApi.search(searchText)

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            if(it!=null&&it.results.any())
            {

                PlanetConentManager.setPlanetsServerAndFactoryFromSearch(it)
                planetitem_list.adapter = SimpleItemRecyclerViewAdapter(this,
                    PlanetConentManager.ITEMS_SEARCH as ArrayList<PlanetData>, twoPane)
            }
            else
            {
                Toast.makeText(this, "No Results...", Toast.LENGTH_SHORT).show()
            }
            progressBarSearch.setVisibility(View.INVISIBLE);
        }
    }

}
