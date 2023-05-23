package com.app.catcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
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
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun exibirDialogoAdicionarTurma(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Adicionar Turma")

        val input = EditText(context)
        builder.setView(input)

        builder.setPositiveButton("Adicionar") { _, _ ->
            val sharedPref = getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)

            val codigoTurma = gerarRandomCode("turmas")
            val nomeTurma = input.text.toString().trim()
            val responsavelTurma = sharedPref.getString("nome_usuario", null)


            val mapTurma = hashMapOf(
                "codigoTurma" to codigoTurma,
                "nomeTurma" to nomeTurma,
                "registroTurma" to Timestamp.now(),
                "responsavelTurma" to responsavelTurma
            )

            db.collection("turmas")
                .add(mapTurma)
                .addOnCompleteListener {
                    Toast.makeText(this, "Turma adicionada: $nomeTurma", Toast.LENGTH_LONG).show()
                    recreate()
                }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun exibirDialogoAdicionarDeck(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Adicionar Deck")

        val input = EditText(context)
        builder.setView(input)

        builder.setPositiveButton("Adicionar") { _, _ ->
            val sharedPref = getSharedPreferences("perfil_usuario", Context.MODE_PRIVATE)

            val codigoDeck = gerarRandomCode("decks")
            val nomeDeck = input.text.toString().trim()
            val responsavelDeck = sharedPref.getString("nome_usuario", null)


            val mapDeck = hashMapOf(
                "codigoDeck" to codigoDeck,
                "nomeDeck" to nomeDeck,
                "registroDeck" to Timestamp.now(),
                "usuarioDeck" to responsavelDeck
            )

            db.collection("decks")
                .add(mapDeck)
                .addOnCompleteListener {
                    Toast.makeText(this, "Deck adicionado: $nomeDeck", Toast.LENGTH_LONG).show()
                    recreate()
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