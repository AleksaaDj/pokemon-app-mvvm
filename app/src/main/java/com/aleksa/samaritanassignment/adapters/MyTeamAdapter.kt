package com.aleksa.samaritanassignment.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.activities.PokemonDetailActivity
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.utils.Constants

class MyTeamAdapter(private val list: List<MyTeamItem>) :
    RecyclerView.Adapter<MyTeamAdapter.MyView>() {
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var namePokemon: TextView = view.findViewById(R.id.pokemon_name_tv)
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
            namePokemon.text = holder.itemView.context.getString(R.string.name_place_holder, listData.name)
            itemView.setOnClickListener {
                val activity = holder.itemView.context as Activity
                val intent = Intent(activity, PokemonDetailActivity::class.java)
                intent.apply {
                    putExtra(Constants.SHARED_PREFERENCES_POKEMON_NAME, listData.id.toString())
                    putExtra(Constants.SHARED_PREFERENCES_SCREEN_TYPE, PokemonDetailActivity.CAPTURED)
                    putExtra(Constants.SHARED_PREFERENCES_POKEMON_LONGITUDE, listData.capturedLongAt.toDouble())
                    putExtra(Constants.SHARED_PREFERENCES_POKEMON_LATITUDE, listData.capturedLatAt.toDouble())
                }
                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}