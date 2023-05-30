package com.app.catcards.uix.firestore

data class DecksData(
    val codDeck: String? = null,
    val nomeDeck: String? = null,
    val usuarioDeck: String? = null,
    val codTurma: Int? = null
)

data class TurmasData(
    val codTurma: Int? = null,
    val nomeTurma: String? = null,
    val responsavelTurma: String? = null,
    val decks: MutableList<DecksData> = mutableListOf()
)

data class CardsData(
    val codCard: String? = null,
    val codCategoria: String? = null,
    val nomeCard: String? = null,
    val perguntaCard: String? = null,
    val resCard_A: String? = null,
    val resCard_B: String? = null,
    val resCard_C: String? = null,
    val resCard_D: String? = null,
    val responsavelCard: String? = null,
    val card: MutableList<DecksData> = mutableListOf()
)