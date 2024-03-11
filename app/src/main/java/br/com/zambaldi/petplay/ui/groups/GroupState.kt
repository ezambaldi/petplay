package br.com.zambaldi.petplay.ui.groups

import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.models.Group

sealed class GroupState {
    object Default : GroupState()
    object Loading : GroupState()
    object Error : GroupState()
    data class Loaded(
        val data: List<Group>,
        val audios: List<Audio>,
    ) : GroupState()
}
