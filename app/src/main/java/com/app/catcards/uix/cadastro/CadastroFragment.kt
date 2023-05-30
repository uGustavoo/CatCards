package com.app.catcards.uix.cadastro

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.app.catcards.R
import com.app.catcards.databinding.FragmentCadastroBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CadastroFragment : Fragment() {

    private lateinit var binding: FragmentCadastroBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentCadastroBinding.inflate(inflater, container, false)

        // Definir ouvintes de clique para botões
        binding.buttonNavLogin.setOnClickListener { navigateLogin() }
        binding.buttonCriarConta.setOnClickListener { cadastrarUsuario() }

        return binding.root
    }

    private fun navigateLogin() {
        // Navega até o fragmento de login usando o componente de navegação
        Navigation.findNavController(binding.root).navigate(R.id.action_cadastroFragment_to_loginFragment)
    }

    private fun navigateSucess() {
        // Navega até o fragmento de login usando o componente de navegação
        Navigation.findNavController(binding.root).navigate(R.id.action_cadastroFragment_to_sucessoFragment)
    }

    @SuppressLint("SimpleDateFormat")
    private fun cadastrarUsuario() {
        val nome = binding.getNome.text.toString()
        val email = binding.getEmail.text.toString()
        val senha = binding.getSenha.text.toString()
        val repetirSenha = binding.getRepetirSenha.text.toString()

        val preenchaTodosOsCampos = "Preencha todos os campos"
        val asSenhasNaoCoincidem = "As senhas não coincidem"
        val senhaFraca = "A senha deve ter pelo menos 6 caracteres"
        val emailInvalido = "Digite um email válido"
        val contaJaCadastrada = "Esta conta já foi cadastrada"
        val erroAoCadastrar = "Erro ao cadastrar o usuário"
        val semConexaoComInternet = "Sem conexão com a internet"

        when {
            nome.isEmpty() || email.isEmpty() || senha.isEmpty() || repetirSenha.isEmpty() -> {
                Toast.makeText(requireContext(), preenchaTodosOsCampos, Toast.LENGTH_SHORT).show()
                return
            }
            senha != repetirSenha -> {
                Toast.makeText(requireContext(), asSenhasNaoCoincidem, Toast.LENGTH_SHORT).show()
                return
            }
            senha.length < 6 -> {
                Toast.makeText(requireContext(), senhaFraca, Toast.LENGTH_SHORT).show()
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(requireContext(), emailInvalido, Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(nome)
                                    .build()

                                it.updateProfile(profileUpdates)
                                    .addOnCompleteListener { updateProfileTask ->
                                        if (updateProfileTask.isSuccessful) {
                                            salvarPerfilUsuario(nome, email)
                                            adicionarDadosAoFirestore(it.uid, nome, email)
                                            Toast.makeText(requireContext(), "Usuário registrado com sucesso", Toast.LENGTH_LONG).show()
                                            navigateSucess()
                                        } else {
                                            exibirToast(erroAoCadastrar)
                                        }
                                    }
                            } ?: exibirToast("Usuário não registrado")
                        } else {
                            when (cadastro.exception) {
                                is FirebaseAuthWeakPasswordException ->
                                    Toast.makeText(requireContext(), senhaFraca, Toast.LENGTH_SHORT).show()
                                is FirebaseAuthInvalidCredentialsException ->
                                    Toast.makeText(requireContext(), emailInvalido, Toast.LENGTH_SHORT).show()
                                is FirebaseAuthUserCollisionException ->
                                    Toast.makeText(requireContext(), contaJaCadastrada, Toast.LENGTH_SHORT).show()
                                is FirebaseNetworkException ->
                                    Toast.makeText(requireContext(), semConexaoComInternet, Toast.LENGTH_SHORT).show()
                                else ->
                                    Toast.makeText(requireContext(), erroAoCadastrar, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }
    }

    private fun adicionarDadosAoFirestore(uid: String, nome: String, email: String) {
        val data = mapOf(
            "codAluno" to "",
            "nomeAluno" to nome,
            "emailAluno" to email,
            "codTurma" to "",
            "rankingAluno" to "",
            "registroAluno" to Timestamp.now()
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("alunos")
            .document(uid)
            .set(data)
            .addOnSuccessListener {
                // Dados adicionados com sucesso ao Firestore
            }
            .addOnFailureListener {
                exibirToast("Erro ao adicionar dados ao nosso banco")
            }
    }

    // Este método salva as informações do perfil do usuário em SharedPreferences
    private fun salvarPerfilUsuario(usuario: String, email: String) {
        val sharedPref = requireActivity().getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("nome_usuario", usuario)
        editor.putString("email_usuario", email)
        editor.apply()
    }

    private fun exibirToast(mensagem: String) {
        Toast.makeText(requireActivity(), mensagem, Toast.LENGTH_SHORT).show()
    }
}