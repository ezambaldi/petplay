package br.com.zambaldi.petplay.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is groups Fragment"
    }
    val text: LiveData<String> = _text
}