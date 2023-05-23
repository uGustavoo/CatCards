package com.app.catcards.uix.turmas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.databinding.FragmentTurmasBinding
import com.app.catcards.uix.firestore.TurmasAdapter
import com.app.catcards.uix.firestore.TurmasData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TurmasFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentTurmasBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<TurmasData>
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTurmasBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = TurmasAdapter(ArrayList()) // Defina um adaptador vazio padrão aqui

        dataList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("turmas")
            .orderBy("registroTurma", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val data: TurmasData = document.toObject(TurmasData::class.java)
                        dataList.add(data)
                    }
                    recyclerView.adapter = TurmasAdapter(dataList)
                } else {
                    recyclerView.adapter = TurmasAdapter(ArrayList())
                }
            }
            .addOnFailureListener {
                recyclerView.adapter = TurmasAdapter(ArrayList())
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}