package com.app.catcards.uix.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.databinding.FragmentCardBinding
import com.app.catcards.uix.firestore.CardAdapter
import com.app.catcards.uix.firestore.CardsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CardFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentCardBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CardsData>
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = CardAdapter(ArrayList()) // Defina um adaptador vazio padrão aqui

        dataList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("cards")
            .orderBy("registroCard", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val data: CardsData = document.toObject(CardsData::class.java)
                        dataList.add(data)
                    }
                    recyclerView.adapter = CardAdapter(dataList)
                } else {
                    recyclerView.adapter = CardAdapter(ArrayList())
                }
            }
            .addOnFailureListener {
                recyclerView.adapter = CardAdapter(ArrayList())
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}