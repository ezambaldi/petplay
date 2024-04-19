package br.com.zambaldi.petplayzam.ui.groups

import br.com.zambaldi.petplayzam.models.Audio
import br.com.zambaldi.petplayzam.models.Group

sealed class GroupState {
    object Default : GroupState()
    object Loading : GroupState()
    object Error : GroupState()
    data class Loaded(
        val data: MutableList<Group>,
        val audios: MutableList<Audio>,
    ) : GroupState()
}
