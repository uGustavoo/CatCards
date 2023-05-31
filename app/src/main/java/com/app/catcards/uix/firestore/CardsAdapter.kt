package com.app.catcards.uix.firestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.R

class CardsAdapter(
    private val dataList: ArrayList<CardsData>,
    private val onItemClick: (CardsData) -> Unit
) : RecyclerView.Adapter<CardsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCard: TextView = itemView.findViewById(R.id.item_card)
        val tvUsuario: TextView = itemView.findViewById(R.id.item_usuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_cards, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cardData = dataList[position]

        holder.tvCard.text = cardData.nomeCard
        holder.tvUsuario.text = cardData.responsavelCard

        holder.itemView.setOnClickListener {
            onItemClick(cardData)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
