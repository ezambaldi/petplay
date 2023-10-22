package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain

class LocalAudioGroupRepositoryImpl(
    private val petPlayLocalProviderApi: PetPlayLocalProviderApi,
): LocalAudioGroupRepository {

    override suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain) {
        petPlayLocalProviderApi.addAudioGroup(audiosGroupDomain)
    }

    override suspend fun getAudiosGroup(idGroup: Int): List<AudiosGroupDomain> {
        return petPlayLocalProviderApi.getAudiosGroup(idGroup)
    }

    override suspend fun deleteAudioGroup(id: Int) {
        petPlayLocalProviderApi.deleteAudioGroup(id)
    }

}