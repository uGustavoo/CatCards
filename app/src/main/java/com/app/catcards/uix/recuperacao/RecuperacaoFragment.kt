package com.app.catcards.uix.recuperacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.catcards.R
import com.app.catcards.databinding.FragmentRecuperacaoBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperacaoFragment : Fragment() {

    private lateinit var binding: FragmentRecuperacaoBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecuperacaoBinding.inflate(inflater, container, false)

        // Definir ouvintes de clique para botões
        RedefinirSenha()
        setupCadastroButton()

        return binding.root
    }

    // Este método envia um e-mail de redefinição de senha
    private fun RedefinirSenha() {
        binding.buttomRecuperarSenha.setOnClickListener {
            val email = binding.getEmail.text.toString()

            if (email.isEmpty()) {
                exibirToast("Digite seu e-mail para redefinir a senha")
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        exibirToast("E-mail de redefinição de senha enviado para $email")
                        limparCampos()
                        setupLoginButton()
                    }
                    .addOnFailureListener { exception ->
                        exibirToast("Erro ao enviar e-mail de redefinição de senha: ${exception.message}")
                    }
            }
        }
    }

    // Este método configura o comportamento do botão "Cadastro"
    private fun setupCadastroButton() {
        binding.buttomNavCadastro.setOnClickListener {
            findNavController().navigate(R.id.action_recuperacaoFragment_to_cadastroFragment)
        }
    }

    // Este método levará ao fragmento de "Recuperacao"
    private fun setupLoginButton() {
        findNavController().navigate(R.id.action_recuperacaoFragment_to_loginFragment)
    }

    private fun limparCampos() {
        // Limpa os campos de entrada do usuário usando
        binding.getEmail.setText("")
    }

    private fun exibirToast(mensagem: String) {
        Toast.makeText(requireActivity(), mensagem, Toast.LENGTH_SHORT).show()
    }

}