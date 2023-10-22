package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.AudiosGroup

interface AudioGroupUseCase {

    suspend fun addAudioGroup(audiosGroup: AudiosGroup)
    suspend fun getAudiosGroup(idGroup: Int): List<AudiosGroup>
    suspend fun deleteAudioGroup(id: Int)

}