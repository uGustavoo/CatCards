package com.app.catcards.uix.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.catcards.R
import com.app.catcards.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val imageView: ImageView = binding.welcome
        val icWelcome = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_welcome)
        imageView.setImageDrawable(icWelcome)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}