package com.app.catcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.catcards.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Definir a navegação e visualizações do menu
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val bottomView: BottomNavigationView = binding.appBarMain.bottomNavigationView

        // o menu deve ser considerado como destinos de nível superior.

        // Obter preferências compartilhadas do usuário
        val sharedPref = getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)
        val nomeUsuario = sharedPref.getString("nome_usuario", null)
        val emailUsuario = sharedPref.getString("email_usuario", null)

        // Define a visualização do cabeçalho com as informações do usuário
        navView.getHeaderView(0).apply {
            findViewById<TextView>(R.id.header_usuario).text = nomeUsuario
            findViewById<TextView>(R.id.header_email).text = emailUsuario
        }

        // Configurar barra de ação e navegação com configuração
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_turmas, R.id.nav_decks, R.id.nav_cards),
            drawerLayout
        )

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_turmas, R.id.nav_decks),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomView.setupWithNavController(navController)

        // Configura o item para sair
        navView.menu.findItem(R.id.menu_logout).setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        when (item.itemId) {
            R.id.menu_add -> {
                val currentDestination = navController.currentDestination?.id
                if (currentDestination == R.id.nav_turmas) {
                    exibirDialogoAdicionarTurma(this)
                } else if (currentDestination == R.id.nav_decks) {
                    exibirDialogoAdicionarDeck(this)
                }else if (currentDestination == R.id.nav_cards) {
                    exibirDialogoAdicionarCard(this)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun exibirDialogoAdicionarTurma(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Adicionar Turma")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val inputNome = EditText(context)
        inputNome.hint = "Nome da Turma"
        val layoutParamsNome = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParamsNome.setMargins(16, 16, 16, 16)
        inputNome.layoutParams = layoutParamsNome
        layout.addView(inputNome)

        val inputDescricao = EditText(context)
        inputDescricao.hint = "Descrição da Turma"
        layout.addView(inputDescricao)
        inputDescricao.layoutParams = layoutParamsNome

        builder.setView(layout)

        builder.setPositiveButton("Adicionar") { _, _ ->
            val nomeTurma = inputNome.text.toString().trim()
            val descricaoTurma = inputDescricao.text.toString().trim()

            if (nomeTurma.isNotEmpty() && descricaoTurma.isNotEmpty()) {
                val sharedPref = getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)
                val codigoTurma = gerarRandomCode("turmas")
                val responsavelTurma = sharedPref.getString("nome_usuario", null)

                val mapTurma = hashMapOf(
                    "codTurma" to codigoTurma,
                    "nomeTurma" to nomeTurma,
                    "descricaoTurma" to descricaoTurma,
                    "registroTurma" to Timestamp.now(),
                    "responsavelTurma" to responsavelTurma
                )

                db.collection("turmas")
                    .add(mapTurma)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Turma adicionada: $nomeTurma", Toast.LENGTH_LONG).show()
                        recreate()
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun exibirDialogoAdicionarDeck(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Adicionar Deck")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val inputNome = EditText(context)
        inputNome.hint = "Nome do Deck"
        val layoutParamsNome = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParamsNome.setMargins(16, 16, 16, 16)
        inputNome.layoutParams = layoutParamsNome
        layout.addView(inputNome)

        val inputDescricao = EditText(context)
        inputDescricao.hint = "Descrição do Deck"
        layout.addView(inputDescricao)
        inputDescricao.layoutParams = layoutParamsNome

        builder.setView(layout)

        builder.setPositiveButton("Adicionar") { _, _ ->
            val nomeDeck = inputNome.text.toString().trim()
            val descricaoDeck = inputDescricao.text.toString().trim()

            if (nomeDeck.isNotEmpty() && descricaoDeck.isNotEmpty()) {
                val sharedPref = getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)
                val codigoDeck = gerarRandomCode("decks")
                val responsavelDeck = sharedPref.getString("nome_usuario", null)

                val mapDeck = hashMapOf(
                    "codDeck" to codigoDeck,
                    "nomeDeck" to nomeDeck,
                    "descricaoDeck" to descricaoDeck,
                    "registroDeck" to Timestamp.now(),
                    "responsavelDeck" to responsavelDeck
                )

                db.collection("decks")
                    .add(mapDeck)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Deck adicionado: $nomeDeck", Toast.LENGTH_LONG).show()
                        recreate()
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun exibirDialogoAdicionarCard(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Adicionar Card")

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val inputNome = EditText(context)
        inputNome.hint = "Nome do Card"
        val layoutParamsNome = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParamsNome.setMargins(16, 16, 16, 16)
        inputNome.layoutParams = layoutParamsNome
        layout.addView(inputNome)

        val inputPergunta = EditText(context)
        inputPergunta.hint = "Pergunta"
        layout.addView(inputPergunta)
        inputPergunta.layoutParams = layoutParamsNome

        val inputRespostaA = EditText(context)
        inputRespostaA.hint = "Resposta A"
        layout.addView(inputRespostaA)
        inputRespostaA.layoutParams = layoutParamsNome

        val inputRespostaB = EditText(context)
        inputRespostaB.hint = "Resposta B"
        layout.addView(inputRespostaB)
        inputRespostaB.layoutParams = layoutParamsNome

        val inputRespostaC = EditText(context)
        inputRespostaC.hint = "Resposta C"
        layout.addView(inputRespostaC)
        inputRespostaC.layoutParams = layoutParamsNome

        val inputRespostaD = EditText(context)
        inputRespostaD.hint = "Resposta D"
        layout.addView(inputRespostaD)
        inputRespostaD.layoutParams = layoutParamsNome

        builder.setView(layout)

        builder.setPositiveButton("Adicionar") { _, _ ->
            val nomeCard = inputNome.text.toString().trim()
            val perguntaCard = inputPergunta.text.toString().trim()
            val resCardA = inputRespostaA.text.toString().trim()
            val resCardB = inputRespostaB.text.toString().trim()
            val resCardC = inputRespostaC.text.toString().trim()
            val resCardD = inputRespostaD.text.toString().trim()

            if (nomeCard.isNotEmpty() && perguntaCard.isNotEmpty() &&
                resCardA.isNotEmpty() && resCardB.isNotEmpty() && resCardC.isNotEmpty() && resCardD.isNotEmpty()
            ) {
                val sharedPref = getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)
                val codigoCard = gerarRandomCode("cards")
                val responsavelCard = sharedPref.getString("nome_usuario", null)

                val mapCard = hashMapOf(
                    "codCard" to codigoCard,
                    "nomeCard" to nomeCard,
                    "perguntaCard" to perguntaCard,
                    "resCard_A" to resCardA,
                    "resCard_B" to resCardB,
                    "resCard_C" to resCardC,
                    "resCard_D" to resCardD,
                    "responsavelCard" to responsavelCard,
                    "registroCard" to Timestamp.now(),
                )

                db.collection("cards")
                    .add(mapCard)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Card adicionado: $nomeCard", Toast.LENGTH_LONG).show()
                        recreate()
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }


    private fun gerarRandomCode(collection: String): String {
        val random = Random()
        val codeLength = 6
        val characters = "0123456789"
        var code: String

        do {
            code = ""
            repeat(codeLength) {
                val randomIndex = random.nextInt(characters.length)
                code += characters[randomIndex]
            }
        } while (verificarCode(db, collection, code))

        return code
    }


    private fun verificarCode(db: FirebaseFirestore, collection: String, code: String): Boolean {
        var exists = false

        val query = db.collection(collection)
            .whereEqualTo("codigoTurma", code)
            .limit(1)

        query.get()
            .addOnSuccessListener { documents ->
                exists = !documents.isEmpty
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error checking code existence: $exception")
            }

        return exists
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflar o menu; isso adiciona itens à barra de ação, se estiver presente.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}