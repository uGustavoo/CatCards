package com.app.catcards.uix.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()

    val text: LiveData<String>
        get() = _text

    init {
        _text.value = "Esse é um fragmento de Cards"
    }
}