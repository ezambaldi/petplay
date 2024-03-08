package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioGroupRepository
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioRepository
import br.com.zambaldi.petplay.providers.datasource.repository.LocalGroupRepository
import br.com.zambaldi.petplay.providers.mappers.AudioGroupMapper
import br.com.zambaldi.petplay.providers.mappers.GroupMapper
import com.example.myapplicationtest.bases.GenericResult

class GroupUseCaseImpl(
    private val localGroupRepository: LocalGroupRepository,
    private val localAudioGroupRepository: LocalAudioGroupRepository,
    private val groupMapper: GroupMapper,
    private val audiosGroupMapper: AudioGroupMapper
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

                val groupDomain = groupMapper.toDomain(result.data)
                groupDomain.map { group ->
                    val audios = localAudioGroupRepository.getAudiosGroup(group.id)
                    when (audios) {
                        is GenericResult.Success -> {
                            group.audios = audiosGroupMapper.toDomain(audios.data)
                        }
                        is GenericResult.Error -> {
                            group.audios = listOf()
                        }
                    }
                }

                GenericResult.Success(groupDomain)

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