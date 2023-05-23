package com.app.catcards.uix.firestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.R


class TurmasAdapter(
    private val datalist: ArrayList<TurmasData>) :
    RecyclerView.Adapter<TurmasAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val tvTurma:TextView = itemView.findViewById(R.id.item_deck)
        val tvUsuario:TextView = itemView.findViewById(R.id.item_usuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_decks, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvTurma.text = datalist[position].nomeTurma
        holder.tvUsuario.text = datalist[position].responsavelTurma
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}
