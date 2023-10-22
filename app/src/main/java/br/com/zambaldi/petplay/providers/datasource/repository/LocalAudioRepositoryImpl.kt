package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.CoroutineContextProvider
import br.com.zambaldi.petplay.providers.Status
import br.com.zambaldi.petplay.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.withContext

class LocalAudioRepositoryImpl(
    private val petPlayLocalProviderApi: PetPlayLocalProviderApi,
    private val coroutineContextProvider: CoroutineContextProvider,
): LocalAudioRepository {

    private val GENERIC_ERROR_MESSAGE = "An error occurred while receiving data\\Please try again."

    override suspend fun addAudio(audioDomain: AudioDomain): GenericResult<String> =
    withContext(coroutineContextProvider.io) {
        val result = petPlayLocalProviderApi.addAudio(audioDomain)
        when (result.status) {
            Status.SUCCESS -> {
                GenericResult.Success(result.data?: "")
            }
            Status.ERROR -> {
                GenericResult.Error(result.message?: GENERIC_ERROR_MESSAGE)
            }
            else -> GenericResult.Error(GENERIC_ERROR_MESSAGE)
        }
    }

    override suspend fun getAudios(): GenericResult<List<AudioDomain>> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.getAudios()
            when (result.status) {
                Status.SUCCESS -> {
                    GenericResult.Success(result.data?: listOf())
                }
                Status.ERROR -> {
                    GenericResult.Error(result.message?: GENERIC_ERROR_MESSAGE)
                }
                else -> GenericResult.Error(GENERIC_ERROR_MESSAGE)
            }
        }

    override suspend fun deleteAudio(id: Int): GenericResult<String> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.deleteAudio(id)
            when (result.status) {
                Status.SUCCESS -> {
                    GenericResult.Success(result.data?: "")
                }
                Status.ERROR -> {
                    GenericResult.Error(result.message?: GENERIC_ERROR_MESSAGE)
                }
                else -> GenericResult.Error(GENERIC_ERROR_MESSAGE)
            }
        }

}