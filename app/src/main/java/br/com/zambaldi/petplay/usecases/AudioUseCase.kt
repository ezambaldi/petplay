package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.Audio
import com.example.myapplicationtest.bases.GenericResult

interface AudioUseCase {

    suspend fun addAudio(audio: Audio): GenericResult<String>
    suspend fun getAudios(): GenericResult<List<Audio>>
    suspend fun deleteAudio(id: Int): GenericResult<String>

}