package com.app.catcards.uix.turmas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TurmasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este é o fragmento de turmas"
    }
    val text: LiveData<String> = _text
}