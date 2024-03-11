package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.AudiosGroup
import com.example.myapplicationtest.bases.GenericResult

interface AudioGroupUseCase {

    suspend fun addAudioGroup(audiosGroup: AudiosGroup): GenericResult<String>
    suspend fun getAudiosGroup(idGroup: Int): GenericResult<List<AudiosGroup>>
    suspend fun deleteAudioGroup(id: Int): GenericResult<String>

}