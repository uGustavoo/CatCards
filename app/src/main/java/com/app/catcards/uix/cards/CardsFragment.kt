package com.app.catcards.uix.cards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.catcards.databinding.FragmentCardsBinding
import com.app.catcards.uix.card.CardActivity
import com.app.catcards.uix.firestore.CardsAdapter
import com.app.catcards.uix.firestore.CardsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CardsFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentCardsBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<CardsData>
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        recyclerView.adapter = CardsAdapter(ArrayList()) { cardData ->
            val intent = Intent(requireActivity(), CardActivity::class.java)
            intent.putExtra("codCard", cardData.codCard)
            startActivity(intent)
        }

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
                    recyclerView.adapter = CardsAdapter(dataList) { cardData ->
                        val intent = Intent(requireActivity(), CardActivity::class.java)
                        intent.putExtra("codCard", cardData.codCard)
                        startActivity(intent)
                    }
                } else {
                    recyclerView.adapter = CardsAdapter(ArrayList()) { cardData ->
                        val intent = Intent(requireActivity(), CardActivity::class.java)
                        intent.putExtra("codCard", cardData.codCard)
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener {
                recyclerView.adapter = CardsAdapter(ArrayList()) { cardData ->
                    val intent = Intent(requireActivity(), CardActivity::class.java)
                    intent.putExtra("codCard", cardData.codCard)
                    startActivity(intent)
                }
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}