package com.aleksa.samaritanassignment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.models.Captured
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.MyTeam
import com.aleksa.samaritanassignment.models.Pokemon

class CapturedAdapter(private val list: List<CapturedItem>) :
    RecyclerView.Adapter<CapturedAdapter.MyView>() {
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var pokemonImage: ImageView = view.findViewById(R.id.pokemon_image_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.captured_layout_item,
                parent,
                false
            )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position]
        with(holder) {
            pokemonImage.setOnClickListener {
                Toast.makeText(pokemonImage.context, listData.id.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}