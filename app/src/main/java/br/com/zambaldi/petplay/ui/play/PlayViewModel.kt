package br.com.zambaldi.petplay.ui.play

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is play Fragment"
    }
    val text: LiveData<String> = _text
}