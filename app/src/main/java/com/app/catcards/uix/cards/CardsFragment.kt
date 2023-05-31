package com.app.catcards.uix.cards

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.R
import com.app.catcards.databinding.FragmentCardsBinding
import com.app.catcards.uix.card.CardActivity
import com.app.catcards.uix.firestore.CardsAdapter
import com.app.catcards.uix.firestore.CardsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CardsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var dataList: ArrayList<CardsData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cards, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        populateData()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))

        val adapter = CardsAdapter(dataList) { cardData ->
            val intent = Intent(activity, CardActivity::class.java)
            intent.putExtra("codCard", cardData.codCard)
            // Adicione outros extras necessários
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    private fun populateData() {
        val db = Firebase.firestore
        val cardsCollection = db.collection("cards")

        cardsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                dataList.clear()

                for (document in querySnapshot) {
                    val cardData = document.toObject(CardsData::class.java)
                    dataList.add(cardData)
                }

                recyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Tratar erros ao obter os dados do Firestore
                Log.e(TAG, "Error getting cards data: $exception")
            }
    }
}