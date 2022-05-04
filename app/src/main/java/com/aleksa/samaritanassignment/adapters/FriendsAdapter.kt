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
import com.aleksa.samaritanassignment.models.Friends
import com.aleksa.samaritanassignment.utils.Constants
import com.aleksa.samaritanassignment.utils.FormatDateTimeUtil

class FriendsAdapter(private val list: List<Friends>) :
    RecyclerView.Adapter<FriendsAdapter.MyView>() {
    private var formatDateTimeUtil = FormatDateTimeUtil()
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var nameTrainer: TextView = view.findViewById(R.id.trainer_name_tv)
        var namePokemon: TextView = view.findViewById(R.id.pokemon_name_tv)
        var timeCaptured: TextView = view.findViewById(R.id.time_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.community_layout_item,
                parent,
                false
            )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position]
        val formattedDate = formatDateTimeUtil.parseSimpleDate(listData.pokemon.captured_at)
        with(holder) {
            nameTrainer.text = listData.name
            namePokemon.text = listData.pokemon.name
            timeCaptured.text = holder.itemView.context.getString(R.string.time_captured_placeholder, formattedDate)
            itemView.setOnClickListener{
                val activity = holder.itemView.context as Activity
                val intent = Intent(activity, PokemonDetailActivity::class.java)
                intent.apply {
                    putExtra(Constants.SHARED_PREFERENCES_POKEMON_NAME, listData.pokemon.id.toString())
                    putExtra(Constants.SHARED_PREFERENCES_SCREEN_TYPE, PokemonDetailActivity.CAPTURED_BY_OTHER)
                    putExtra(Constants.SHARED_PREFERENCES_POKEMON_CAPTURED_AT, formattedDate)
                    putExtra(Constants.SHARED_PREFERENCES_POKEMON_TRAINER_NAME, listData.name)
                }
                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

