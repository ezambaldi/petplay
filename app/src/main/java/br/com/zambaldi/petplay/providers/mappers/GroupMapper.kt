package br.com.zambaldi.petplay.providers.mappers

import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.providers.DataMapper
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

object GroupMapper : DataMapper<List<GroupDomain>, List<Group>>() {
    override fun toDomain(data: List<GroupDomain>): List<Group> {
        return data.let {
            it.map { group ->
                Group(
                    id = group.id,
                    name = group.name,
                    dateTimeStart = group.dateTimeStart,
                    dateTimeFinish = group.dateTimeFinish,
                    intervalSecond = group.intervalSecond,
                    interactionType = group.interactionType,
                )
            }
        }
    }
}