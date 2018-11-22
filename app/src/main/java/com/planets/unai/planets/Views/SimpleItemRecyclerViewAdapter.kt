package com.planets.unai.planets.Views

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.planets.unai.planets.Models.ClientModels.PlanetData
import com.planets.unai.planets.PlanetItemDetailActivity
import com.planets.unai.planets.PlanetItemDetailFragment
import com.planets.unai.planets.PlanetItemListActivity
import com.planets.unai.planets.R
import kotlinx.android.synthetic.main.planetitem_list_content.view.*

class SimpleItemRecyclerViewAdapter(
    private val parentActivity: PlanetItemListActivity,
    private val values: ArrayList<PlanetData>,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as PlanetData
            if (twoPane) {
                val fragment = PlanetItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(PlanetItemDetailFragment.ARG_ITEM_ID, item.name)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.planetitem_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, PlanetItemDetailActivity::class.java).apply {
                    putExtra(PlanetItemDetailFragment.ARG_ITEM_ID, item.name)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.planetitem_list_content, parent, false)
        return ViewHolder(view)
    }

    fun addData() {
        var size = this.values.size
        //this.values.addAll(listItems)
        var sizeNew = this.values.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.cardview_text
    }
}