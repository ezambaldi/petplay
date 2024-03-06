package br.com.zambaldi.petplay.ui.audios

import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.utils.TypeMessage

sealed class AudioState {
    object Default : AudioState()
    object Loading : AudioState()
    object Error : AudioState()
    data class Loaded(
        val data: List<Audio>,
    ) : AudioState()
}
