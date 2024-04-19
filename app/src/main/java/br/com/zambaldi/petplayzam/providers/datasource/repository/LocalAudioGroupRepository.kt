package br.com.zambaldi.petplayzam.providers.datasource.repository

import br.com.zambaldi.petplayzam.providers.datasource.local.entity.AudiosGroupDomain
import com.example.myapplicationtest.bases.GenericResult

interface LocalAudioGroupRepository {

    suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain): GenericResult<String>
    suspend fun getAudiosGroup(idGroup: Int): GenericResult<List<AudiosGroupDomain>>
    suspend fun deleteAudioGroup(id: Int): GenericResult<String>

}