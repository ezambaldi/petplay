package br.com.zambaldi.petplayzam.ui.play

import br.com.zambaldi.petplayzam.models.Audio
import br.com.zambaldi.petplayzam.models.Group

sealed class PlayState {
    object Default : PlayState()
    object Loading : PlayState()
    object Error : PlayState()
    data class Loaded(
        val data: MutableList<Group>,
        val audios: MutableList<Audio>,
    ) : PlayState()
}
