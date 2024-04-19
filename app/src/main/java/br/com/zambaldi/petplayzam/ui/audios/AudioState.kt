package br.com.zambaldi.petplayzam.ui.audios

import br.com.zambaldi.petplayzam.models.Audio

sealed class AudioState {
    object Default : AudioState()
    object Loading : AudioState()
    object Error : AudioState()
    data class Loaded(
        val data: List<Audio>,
    ) : AudioState()
}
