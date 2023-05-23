package com.app.catcards.uix.decks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.databinding.FragmentDecksBinding
import com.app.catcards.uix.firestore.DecksAdapter
import com.app.catcards.uix.firestore.DecksData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DecksFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentDecksBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DecksData>
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDecksBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = DecksAdapter(ArrayList()) // Defina um adaptador vazio padrão aqui

        dataList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("decks")
            .orderBy("registroDeck", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val data: DecksData = document.toObject(DecksData::class.java)
                        dataList.add(data)
                    }
                    recyclerView.adapter = DecksAdapter(dataList)
                } else {
                    recyclerView.adapter = DecksAdapter(ArrayList())
                }
            }
            .addOnFailureListener {
                recyclerView.adapter = DecksAdapter(ArrayList())
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}