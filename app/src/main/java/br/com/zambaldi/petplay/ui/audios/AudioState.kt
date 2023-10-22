package br.com.zambaldi.petplay.ui.audios

import br.com.zambaldi.petplay.models.Audio

sealed class AudioState {
    object Default : AudioState()
    object Loading : AudioState()
    data class Loaded(
        val data: List<Audio>,
        val isShowTopMessage: Boolean = false,
        val topMessage: String = "",
        val typeMessage: TypeMessage = TypeMessage.INFO
    ) : AudioState()
    data class Error(val errorMessage: String = "") : AudioState()
}
