package com.aleksa.samaritanassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.utils.FormatDateTimeUtil

class MyTeamAdapter(private val list: List<MyTeamItem>) :
    RecyclerView.Adapter<MyTeamAdapter.MyView>() {
    private var formatDateTimeUtil = FormatDateTimeUtil()
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var namePokemon: TextView = view.findViewById(R.id.pokemon_name_tv)
        var healthPokemon: TextView = view.findViewById(R.id.pokemon_health_tv)
        var typePokemon: TextView = view.findViewById(R.id.pokemon_type_tv)
        var timeCapturedPokemon: TextView = view.findViewById(R.id.time_captured_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.myteam_layout_item,
                parent,
                false
            )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position]
        with(holder) {
            namePokemon.text = listData.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}