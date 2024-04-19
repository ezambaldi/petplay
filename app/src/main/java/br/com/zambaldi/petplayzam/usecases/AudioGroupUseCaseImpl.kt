package br.com.zambaldi.petplayzam.usecases

import br.com.zambaldi.petplayzam.models.AudiosGroup
import br.com.zambaldi.petplayzam.providers.datasource.local.entity.AudiosGroupDomain
import br.com.zambaldi.petplayzam.providers.datasource.repository.LocalAudioGroupRepository
import br.com.zambaldi.petplayzam.providers.mappers.AudioGroupMapper
import com.example.myapplicationtest.bases.GenericResult

class AudioGroupUseCaseImpl(
    private val localAudioGroupRepository: LocalAudioGroupRepository,
    private val audioGroupMapper: AudioGroupMapper,
): AudioGroupUseCase {

    override suspend fun addAudioGroup(audiosGroup: AudiosGroup): GenericResult<String> {
        return localAudioGroupRepository.addAudioGroup(
            AudiosGroupDomain(
                id = audiosGroup.id,
                order = audiosGroup.order,
                idAudio = audiosGroup.idAudio,
                idGroup = audiosGroup.idGroup
            )
        )
    }

    override suspend fun getAudiosGroup(idGroup: Int): GenericResult<List<AudiosGroup>>  {
        val result = localAudioGroupRepository.getAudiosGroup(idGroup)
        return when (result) {
            is GenericResult.Success -> {
                GenericResult.Success(audioGroupMapper.toDomain(result.data))
            }
            is GenericResult.Error -> {
                GenericResult.Error(result.errorMessage, result.errorTitle)
            }
        }
    }

    override suspend fun deleteAudioGroup(id: Int): GenericResult<String> {
        return localAudioGroupRepository.deleteAudioGroup(id)
    }

}