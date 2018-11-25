package com.planets.unai.planets

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.widget.Toast
import com.planets.unai.planets.Logic.PlanetConentManager
import com.planets.unai.planets.Logic.PlanetItemListInteractor
import com.planets.unai.planets.Models.ClientModels.PlanetData
import com.planets.unai.planets.Presenters.PlanetItemListPresenter
import kotlinx.android.synthetic.main.activity_planetitem_list.*


/**
 * An activity representing a list of Planets. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PlanetItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PlanetItemListActivity : AppCompatActivity() {

    var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    var planetItemListPresenter : PlanetItemListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planetitem_list)

        setSupportActionBar(toolbar)
        toolbar.title = title
        planetItemListPresenter= PlanetItemListPresenter(this,window.decorView.rootView, PlanetItemListInteractor())

        planetItemListPresenter!!.initLinearLayoutManagerRecyclerView(linearLayoutManager)


        if(!PlanetConentManager.ITEMS.any()) {
            if(isOnline())
            {
               planetItemListPresenter!!.getApiPlanets();
            }
            else
            {
                Toast.makeText(this, "Check your network status...", Toast.LENGTH_SHORT).show()
            }

        }
        else
        {
                planetItemListPresenter!!.updateRecyclerView(PlanetConentManager.ITEMS as ArrayList<PlanetData>);
        }

       planetItemListPresenter!!.initScrollListenerForPagination()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search = menu?.findItem(R.id.action_search)?.actionView as android.support.v7.widget.SearchView
        val si = manager.getSearchableInfo(componentName)
        search.setSearchableInfo(si)
        planetItemListPresenter!!.initSearchView(search)
        return true
    }

    fun isOnline(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.getActiveNetworkInfo()
        return netInfo != null && netInfo.isConnectedOrConnecting()
    }

}
