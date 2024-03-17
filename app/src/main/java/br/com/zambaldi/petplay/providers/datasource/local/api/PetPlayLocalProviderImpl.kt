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
            audiosGroupDAO.deleteAudioGroupAllByAudioId(id)
            audioDAO.deleteAudio(id)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }

    }

    override suspend fun addGroup(groupDomain: GroupDomain): Resource<String> {
        return try {
            groupDAO.add(groupDomain)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }
    }

    override suspend fun updateGroup(groupDomain: GroupDomain): Resource<String> {
        return try {
            groupDAO.update(groupDomain.id, groupDomain.name, groupDomain.dateStart, groupDomain.dateFinish, groupDomain.timeStart, groupDomain.timeFinish, groupDomain.intervalSecond, groupDomain.interactionType)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }
    }

    override suspend fun getGroups(): Resource<List<GroupDomain>> {
        return  try {
            Resource.success(groupDAO.getGroups())
        } catch (e: Exception) {
            Resource.error("Error. Try again ($e)", listOf()) }
    }

    override suspend fun deleteGroup(id: Int): Resource<String> {
        return try {
            audiosGroupDAO.deleteAudioGroupAllByGroupId(id)
            groupDAO.deleteGroup(id)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }

    }

    override suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain): Resource<String> {
        return try {
            audiosGroupDAO.add(audiosGroupDomain)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }
    }

    override suspend fun getAudiosGroup(idGroup: Int): Resource<List<AudiosGroupDomain>> {
        return  try {
            Resource.success(audiosGroupDAO.getAudiosGroup(idGroup))
        } catch (e: Exception) {
            Resource.error("Error. Try again ($e)", listOf()) }
    }

    override suspend fun deleteAudioGroup(id: Int): Resource<String> {
        return try {
            audiosGroupDAO.deleteAudioGroup(id)
            Resource.success("")
        } catch (e: Exception) {
            Resource.error(e.toString(), "")
        }
    }

}