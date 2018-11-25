package com.planets.unai.planets.Presenters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.planets.unai.planets.Logic.PlanetConentManager
import com.planets.unai.planets.Logic.PlanetItemListInteractor
import com.planets.unai.planets.Models.ClientModels.PlanetData
import com.planets.unai.planets.Models.ServerModels.PlanetServer
import com.planets.unai.planets.PlanetItemListActivity
import com.planets.unai.planets.Views.PaginationScrollListener
import com.planets.unai.planets.Views.SimpleItemRecyclerViewAdapter
import kotlinx.android.synthetic.main.planetitem_list.view.*

//This is the presenter of the main view
//where I manage the data and population of the list
class PlanetItemListPresenter(private var activity: PlanetItemListActivity, private var view: View, private val planetItemListInteractor: PlanetItemListInteractor)
    : PlanetItemListInteractor.OnFinishedListener {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var isSearching : Boolean = false

    fun initLinearLayoutManagerRecyclerView(linearLayoutManager: LinearLayoutManager)
    {
        view.planetitem_list.layoutManager = linearLayoutManager;
        if (view.planetitem_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
    }

    fun updateRecyclerView( items: ArrayList<PlanetData>)
    {
        view.planetitem_list.adapter = SimpleItemRecyclerViewAdapter(
            activity,
            items, twoPane)
    }

    fun initScrollListenerForPagination()
    {
        view.planetitem_list?.addOnScrollListener(object : PaginationScrollListener( view.planetitem_list.layoutManager as LinearLayoutManager) {
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
                    getMoreItems()
                }

            }
        })
    }

    fun initSearchView(searchView: SearchView)
    {
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.setIconifiedByDefault(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.toString().isNotEmpty()) {
                    showProgressBarSearch()
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
                    hideProgressBarSearch()
                    updateRecyclerView( PlanetConentManager.ITEMS as ArrayList<PlanetData>);
                }
                return false
            }
        })

    }

    fun getApiPlanets()
    {
        planetItemListInteractor.requestPlanets(this)
    }

    private fun addMoreData()
    {
        (view.planetitem_list.adapter as SimpleItemRecyclerViewAdapter).addData()
    }

    private fun showProgressBarPagination()
    {
        view.progressBarPagination.setVisibility(View.VISIBLE);
    }

    private fun hideProgressBarPagintation()
    {
        view.progressBarPagination.setVisibility(View.INVISIBLE);
    }

    private fun showProgressBarSearch()
    {
        view.progressBarSearch.setVisibility(View.VISIBLE);
    }

    private fun hideProgressBarSearch()
    {
        view.progressBarSearch.setVisibility(View.INVISIBLE);
    }


    private fun getMoreItems() {
        if(PlanetConentManager.NEXT_URL!="") {
            showProgressBarPagination()
            planetItemListInteractor.requestGetMorePlanets(this)
        }
        else
        {
            isLastPage = true
            hideProgressBarPagintation()
        }

    }

    private fun search(searchText : String)
    {
        planetItemListInteractor.requestSearch(this,searchText)
    }

    override fun onResultSuccessRequestPlanets(planetserver : PlanetServer)
    {
        PlanetConentManager.setPlanetsServerAndFactory(planetserver)
        updateRecyclerView(PlanetConentManager.ITEMS as ArrayList<PlanetData>);
    }

    override fun onResultSuccessRequestGetMorePlanets(planetServer: PlanetServer)
    {
        PlanetConentManager.setPlanetsServerAndFactory(planetServer)
        isLoading = false
        hideProgressBarPagintation()
        addMoreData()
    }

    override fun onResultSuccessRequestSearch(planetServer: PlanetServer)
    {
        if(planetServer!=null && planetServer.results.any())
        {
            PlanetConentManager.setPlanetsServerAndFactoryFromSearch(planetServer)
            updateRecyclerView( PlanetConentManager.ITEMS_SEARCH as ArrayList<PlanetData>);
        }
        else
        {
            Toast.makeText(activity, "No Results...", Toast.LENGTH_SHORT).show()
        }
        hideProgressBarSearch()
    }

    override fun onResultFail(strError: String) {

    }

}