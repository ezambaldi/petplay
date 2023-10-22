package br.com.zambaldi.petplay.providers.datasource.local.api

import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.providers.Resource
import br.com.zambaldi.petplay.providers.datasource.local.dao.AudioDAO
import br.com.zambaldi.petplay.providers.datasource.local.dao.AudiosGroupDAO
import br.com.zambaldi.petplay.providers.datasource.local.dao.GroupDAO
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

class PetPlayLocalProviderImpl(
    private val audioDAO: AudioDAO,
    private val groupDAO: GroupDAO,
    private val audiosGroupDAO: AudiosGroupDAO
): PetPlayLocalProviderApi {

    override suspend fun addAudio(audioDomain: AudioDomain): Resource<String> {
        return try {
            audioDAO.add(audioDomain)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }
    }

    override suspend fun getAudios(): Resource<List<AudioDomain>> {
        return  try {
            Resource.success(audioDAO.getAudios())
        } catch (e: Exception) {
            Resource.error("Error. Try again ($e)", listOf()) }
    }

    override suspend fun deleteAudio(id: Int): Resource<String> {
        return try {
            audioDAO.deleteAudio(id)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }

    }

    override suspend fun addGroup(groupDomain: GroupDomain) {
        groupDAO.add(groupDomain)
    }

    override suspend fun getGroups(): List<GroupDomain> {
        return groupDAO.getGroups()
    }

    override suspend fun deleteGroup(id: Int) {
        groupDAO.deleteGroup(id)
    }

    override suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain) {
        audiosGroupDAO.add(audiosGroupDomain)
    }

    override suspend fun getAudiosGroup(idGroup: Int): List<AudiosGroupDomain> {
        return audiosGroupDAO.getAudiosGroup()
    }

    override suspend fun deleteAudioGroup(id: Int) {
        audiosGroupDAO.deleteAudioGroup(id)
    }

}