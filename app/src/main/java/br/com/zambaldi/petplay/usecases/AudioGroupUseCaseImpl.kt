package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioGroupRepository
import br.com.zambaldi.petplay.providers.mappers.AudioGroupMapper
import com.example.myapplicationtest.bases.GenericResult

class AudioGroupUseCaseImpl(
    private val localAudioGroupRepository: LocalAudioGroupRepository,
    private val audioGroupMapper: AudioGroupMapper,
): AudioGroupUseCase {

    override suspend fun addAudioGroup(audiosGroup: AudiosGroup) {
        localAudioGroupRepository.addAudioGroup(
            AudiosGroupDomain(
                id = audiosGroup.id,
                order = audiosGroup.order,
                idAudio = audiosGroup.idAudio,
                idGroup = audiosGroup.idGroup
            )
        )
    }

    override suspend fun getAudiosGroup(idGroup: Int): List<AudiosGroup> {
        return audioGroupMapper.toDomain(localAudioGroupRepository.getAudiosGroup(idGroup))
    }

    override suspend fun deleteAudioGroup(id: Int) {
        localAudioGroupRepository.deleteAudioGroup(id)
    }

}