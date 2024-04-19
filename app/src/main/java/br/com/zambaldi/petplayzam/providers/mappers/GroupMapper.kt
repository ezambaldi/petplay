package br.com.zambaldi.petplayzam.providers.mappers

import br.com.zambaldi.petplayzam.models.Group
import br.com.zambaldi.petplayzam.providers.DataMapper
import br.com.zambaldi.petplayzam.providers.datasource.local.entity.GroupDomain

object GroupMapper : DataMapper<List<GroupDomain>, List<Group>>() {
    override fun toDomain(data: List<GroupDomain>): List<Group> {
        return data.let {
            it.map { group ->
                Group(
                    id = group.id,
                    name = group.name,
                    dateStart = group.dateStart,
                    dateFinish = group.dateFinish,
                    timeStart = group.timeStart,
                    timeFinish = group.timeFinish,
                    intervalSecond = group.intervalSecond,
                    interactionType = group.interactionType,
                )
            }
        }
    }
}