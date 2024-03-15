package br.com.zambaldi.petplay.usecases

import android.os.SystemClock.sleep
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.providers.CoroutineContextProvider
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain
import br.com.zambaldi.petplay.providers.datasource.repository.LocalAudioRepository
import br.com.zambaldi.petplay.providers.mappers.AudioMapper
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.withContext

class AudioUseCaseImpl(
    private val localAudioRepository: LocalAudioRepository,
    private val audioMapper: AudioMapper,
): AudioUseCase {

    override suspend fun addAudio(audio: Audio): GenericResult<String> {
        return localAudioRepository.addAudio(
            AudioDomain(
                id = audio.id,
                name = audio.name,
                path = audio.path
            )
        )
    }

    override suspend fun getAudios(): GenericResult<List<Audio>> {
        val result = localAudioRepository.getAudios()
        val result2 = localAudioRepository.getAudios()

        return when (result) {
            is GenericResult.Success -> {
                val audioDomain = audioMapper.toDomain(result.data)
                GenericResult.Success(audioDomain)
            }
            is GenericResult.Error -> {
                GenericResult.Error(result.errorMessage, result.errorTitle)
            }
        }
    }

    override suspend fun deleteAudio(id: Int): GenericResult<String> {
        return localAudioRepository.deleteAudio(id)
    }

}