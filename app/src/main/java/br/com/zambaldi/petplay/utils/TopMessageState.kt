package br.com.zambaldi.petplay.utils

import br.com.zambaldi.petplay.models.Audio

sealed class TopMessageState {
    object Default : TopMessageState()
    data class Show(
        val message: String = "",
        val typeMessage: TypeMessage = TypeMessage.INFO,
        val setAsDefault: () -> Unit = { }
    ) : TopMessageState()
}