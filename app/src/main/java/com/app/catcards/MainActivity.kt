package com.app.catcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
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
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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