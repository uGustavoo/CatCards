package com.app.catcards.uix.decks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DecksViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este é um fragmento de Decks"
    }
    val text: LiveData<String> = _text
}