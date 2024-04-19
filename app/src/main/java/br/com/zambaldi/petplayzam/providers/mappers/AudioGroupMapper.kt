package br.com.zambaldi.petplayzam.providers.mappers

import br.com.zambaldi.petplayzam.models.AudiosGroup
import br.com.zambaldi.petplayzam.providers.DataMapper
import br.com.zambaldi.petplayzam.providers.datasource.local.entity.AudiosGroupDomain

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