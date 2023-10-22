package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain

interface LocalAudioGroupRepository {

    suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain)
    suspend fun getAudiosGroup(idGroup: Int): List<AudiosGroupDomain>
    suspend fun deleteAudioGroup(id: Int)

}