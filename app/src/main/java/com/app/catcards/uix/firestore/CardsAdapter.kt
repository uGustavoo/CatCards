package com.app.catcards.uix.firestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.R

class CardsAdapter(
    private val datalist: ArrayList<CardsData>) :
    RecyclerView.Adapter<CardsAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val tvCard:TextView = itemView.findViewById(R.id.item_card)
        val tvUsuario:TextView = itemView.findViewById(R.id.item_usuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_cards, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvCard.text = datalist[position].nomeCard
        holder.tvUsuario.text = datalist[position].responsavelCard
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}