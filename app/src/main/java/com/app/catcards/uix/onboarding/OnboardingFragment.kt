package com.app.catcards.uix.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.app.catcards.MainActivity
import com.app.catcards.R
import com.app.catcards.databinding.FragmentOnboardingBinding
import com.google.firebase.auth.FirebaseAuth

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        // Definir ouvintes de clique para botões
        binding.buttomNavLogin.setOnClickListener { navigateLogin() }
        binding.buttomNavCadastro.setOnClickListener { navigateCadastro() }

        return binding.root
    }

    private fun navigateLogin() {
        // Navega até o fragmento de login usando o componente de navegação
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_onboardingFragment_to_loginFragment)
    }

    private fun navigateCadastro() {
        // Navega até o fragmento de login usando o componente de navegação
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_onboardingFragment_to_cadastroFragment)
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
    }
}
