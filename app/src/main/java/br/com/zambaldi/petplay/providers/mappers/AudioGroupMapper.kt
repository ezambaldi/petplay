package br.com.zambaldi.petplay.providers.mappers

import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.providers.DataMapper
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain

object AudioGroupMapper : DataMapper<List<AudiosGroupDomain>, List<AudiosGroup>>() {
    override fun toDomain(data: List<AudiosGroupDomain>): List<AudiosGroup> {
        return data.let {
            it.map { audioGroup ->
                AudiosGroup(
                    id = audioGroup.id,
                    order = audioGroup.order,
                    idAudio = audioGroup.idAudio,
                    idGroup = audioGroup.idGroup
                )
            }
        }
    }
}