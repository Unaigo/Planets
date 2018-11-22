package com.planets.unai.planets

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.planets.unai.planets.Logic.PlanetConentManager
import com.planets.unai.planets.Models.ClientModels.PlanetData
import kotlinx.android.synthetic.main.activity_planetitem_detail.*
import kotlinx.android.synthetic.main.planetitem_detail.view.*

/**
 * A fragment representing a single PlanetItem detail screen.
 * This fragment is either contained in a [PlanetItemListActivity]
 * in two-pane mode (on tablets) or a [PlanetItemDetailActivity]
 * on handsets.
 */
class PlanetItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: PlanetData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = PlanetConentManager.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.name
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.planetitem_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.rotation_period.text = "Rotation Period : "+ it.rotation_period
            rootView.orbital_period.text = "Orbital Period : "+ it.orbital_period
            rootView.diameter.text = "Diameter : "+it.diameter
            rootView.climate.text = "Climate : "+ it.climate
            rootView.gravity.text = "Gravity : "+it.gravity
            rootView.terrain.text ="Terrain : "+ it.terrain
            rootView.surface_water.text = "Surface Water : "+it.surface_water
            rootView.population.text = "Population : "+ it.population
            rootView.residents.text = "Total Residents : "+it.residents.count().toString()
            rootView.films.text = "Films : "+ it.films.count().toString()
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
