package com.app.catcards.uix.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Este é o fragmento Home"
    }
    val text: LiveData<String> = _text
}