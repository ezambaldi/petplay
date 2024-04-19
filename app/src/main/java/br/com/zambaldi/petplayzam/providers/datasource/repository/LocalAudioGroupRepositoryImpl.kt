package br.com.zambaldi.petplayzam.providers.datasource.repository

import br.com.zambaldi.petplayzam.providers.CoroutineContextProvider
import br.com.zambaldi.petplayzam.providers.Status
import br.com.zambaldi.petplayzam.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplayzam.providers.datasource.local.entity.AudiosGroupDomain
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.withContext

class LocalAudioGroupRepositoryImpl(
    private val petPlayLocalProviderApi: PetPlayLocalProviderApi,
    private val coroutineContextProvider: CoroutineContextProvider,
    ): LocalAudioGroupRepository {
    private val GENERIC_ERROR_MESSAGE = "An error occurred while receiving data\\Please try again."
    override suspend fun addAudioGroup(audiosGroupDomain: AudiosGroupDomain): GenericResult<String> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.addAudioGroup(audiosGroupDomain)
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

    override suspend fun getAudiosGroup(idGroup: Int): GenericResult<List<AudiosGroupDomain>> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.getAudiosGroup(idGroup)
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

    override suspend fun deleteAudioGroup(id: Int): GenericResult<String> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.deleteAudioGroup(id)
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