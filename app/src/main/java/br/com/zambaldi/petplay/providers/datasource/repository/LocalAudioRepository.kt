package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain
import com.example.myapplicationtest.bases.GenericResult

interface LocalAudioRepository {

    suspend fun addAudio(audioDomain: AudioDomain): GenericResult<String>
    suspend fun getAudios(): GenericResult<List<AudioDomain>>
    suspend fun deleteAudio(id: Int): GenericResult<String>

}