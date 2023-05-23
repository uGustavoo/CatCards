package com.app.catcards.uix.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.app.catcards.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {

    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cardsViewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        _binding = FragmentCardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery

        cardsViewModel.text.observe(viewLifecycleOwner) { text ->
            textView.text = text
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}