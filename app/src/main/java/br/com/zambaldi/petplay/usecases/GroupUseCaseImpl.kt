package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain
import br.com.zambaldi.petplay.providers.datasource.repository.LocalGroupRepository
import br.com.zambaldi.petplay.providers.mappers.GroupMapper
import com.example.myapplicationtest.bases.GenericResult

class GroupUseCaseImpl(
    private val localGroupRepository: LocalGroupRepository,
    private val groupMapper: GroupMapper,
    ): GroupUseCase {

    override suspend fun addGroup(group: Group): GenericResult<String> {
        return localGroupRepository.addGroup(
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

    override suspend fun getGroups(): GenericResult<List<Group>> {
        val result = localGroupRepository.getGroups()
        return when (result) {
            is GenericResult.Success -> {
                GenericResult.Success(groupMapper.toDomain(result.data))
            }
            is GenericResult.Error -> {
                GenericResult.Error(result.errorMessage, result.errorTitle)
            }
        }
    }

    override suspend fun deleteGroup(id: Int): GenericResult<String> {
        return localGroupRepository.deleteGroup(id)
    }
}