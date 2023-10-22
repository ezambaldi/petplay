package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain
import br.com.zambaldi.petplay.providers.datasource.repository.LocalGroupRepository
import br.com.zambaldi.petplay.providers.mappers.GroupMapper

class GroupUseCaseImpl(
    private val localGroupRepository: LocalGroupRepository,
    private val groupMapper: GroupMapper,
    ): GroupUseCase {

    override suspend fun addGroup(group: Group) {
        localGroupRepository.addGroup(
            GroupDomain(
                id = group.id,
                name = group.name,
                dateTimeStart = group.dateTimeStart,
                dateTimeFinish = group.dateTimeFinish,
                intervalSecond = group.intervalSecond,
                interactionType = group.interactionType
            )
        )
    }

    override suspend fun getGroups(): List<Group> {
        return groupMapper.toDomain(localGroupRepository.getGroups())
    }

    override suspend fun deleteGroup(id: Int) {
        localGroupRepository.deleteGroup(id)
    }
}