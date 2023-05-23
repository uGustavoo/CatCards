package com.app.catcards.uix.sucesso

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.app.catcards.MainActivity
import com.app.catcards.R
import com.app.catcards.databinding.FragmentSucessoBinding
import com.google.firebase.auth.FirebaseAuth

class SucessoFragment : Fragment() {
    private lateinit var binding: FragmentSucessoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSucessoBinding.inflate(inflater, container, false)

        // Definir ouvintes de clique para botões
        binding.buttomNavLogin.setOnClickListener { navigateLogin() }

        return binding.root
    }

    private fun navigateLogin() {
        // Navega até o fragmento de login usando o componente de navegação
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_sucessoFragment_to_loginFragment)
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
    }
}