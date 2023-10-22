package br.com.zambaldi.petplay.providers.datasource.local.api

import br.com.zambaldi.petplay.providers.Resource
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

interface PetPlayLocalProviderApi {

    // AUDIO
    suspend fun addAudio(audioDomain: AudioDomain): Resource<String>
    suspend fun getAudios(): Resource<List<AudioDomain>>
    suspend fun deleteAudio(id: Int): Resource<String>

    //GROUP
    suspend fun addGroup(groupDomain: GroupDomain)
    suspend fun getGroups(): List<GroupDomain>
    suspend fun deleteGroup(id: Int)

    //AUDIO GROUP
    suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain)
    suspend fun getAudiosGroup(idGroup: Int): List<AudiosGroupDomain>
    suspend fun deleteAudioGroup(id: Int)


}