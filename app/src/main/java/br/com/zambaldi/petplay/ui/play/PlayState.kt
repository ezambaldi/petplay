package br.com.zambaldi.petplay.ui.play

import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.models.Group

sealed class PlayState {
    object Default : PlayState()
    object Loading : PlayState()
    object Error : PlayState()
    data class Loaded(
        val data: MutableList<Group>,
        val audios: MutableList<Audio>,
    ) : PlayState()
}
