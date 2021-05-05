package com.sakagami.tech.gmapstrying

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_map_list.*

class MapListActivity: AppCompatActivity() {

    private val adapter by lazy { MapListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_list)

        initAdapter()

    }

    private fun initAdapter() {
        rvMapList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMapList.adapter = adapter

        val listMap = arrayListOf(
            MapLocation("Pasar Moder BSD City", -6.29935819886601, 106.68321341012944),
            MapLocation("UNPAM", -6.343949200907425, 106.73671036827447),
            MapLocation("Office",-6.302123315476662, 106.65387028361779)
        )

        adapter.dataList = listMap

        adapter.onItemClick = {
            MapActivity.launchIntentSingleMap(this, it)
        }

        btnShowFullMap.setOnClickListener {
            MapActivity.launchIntentFullMap(this, listMap)
        }
    }
}