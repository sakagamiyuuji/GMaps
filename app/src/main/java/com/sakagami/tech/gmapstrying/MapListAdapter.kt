package com.sakagami.tech.gmapstrying

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_list_map.view.*

class MapListAdapter(): RecyclerView.Adapter<MapListAdapter.ViewHolder>() {

    var onItemClick: ((mapLocation: MapLocation) -> Unit)? = null

    var dataList: ArrayList<MapLocation> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_map, parent, false))
    }

    override fun onBindViewHolder(holder: MapListAdapter.ViewHolder, position: Int) {
        holder.bindView()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.apply {
                setOnClickListener {
                    val data = dataList[adapterPosition]
                    onItemClick?.invoke(data)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val data = dataList[adapterPosition]
            itemView.apply {
                tvMarkerName.text = data.markerName
                tvMarkerLocationGeo.text = "${data.langitude}, ${data.longitude}"
            }
        }

    }
}