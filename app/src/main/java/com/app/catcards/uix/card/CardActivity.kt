package com.app.catcards.uix.card

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.catcards.databinding.ActivityCardBinding
import com.app.catcards.uix.firestore.CardsData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar!!.hide()

        val codCard = intent.getStringExtra("codCard")

        if (codCard != null) {
            db.collection("cards")
                .document(codCard)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val cardData: CardsData = document.toObject(CardsData::class.java)!!
                        binding.itemPergunta.text = cardData.perguntaCard
                        binding.itemResA.text = cardData.resCard_A
                        binding.itemResB.text = cardData.resCard_B
                        binding.itemResC.text = cardData.resCard_C
                        binding.itemResD.text = cardData.resCard_D
                    } else {
                        Toast.makeText(
                            this@CardActivity,
                            "Nenhum dado encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this@CardActivity, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}
