package br.com.zambaldi.petplayzam.providers.datasource.repository

import br.com.zambaldi.petplayzam.providers.CoroutineContextProvider
import br.com.zambaldi.petplayzam.providers.Status
import br.com.zambaldi.petplayzam.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplayzam.providers.datasource.local.entity.GroupDomain
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.withContext

class LocalGroupRepositoryImpl(
    private val petPlayLocalProviderApi: PetPlayLocalProviderApi,
    private val coroutineContextProvider: CoroutineContextProvider,
): LocalGroupRepository {

    private val GENERIC_ERROR_MESSAGE = "An error occurred while receiving data\\Please try again."

    override suspend fun addGroup(groupDomain: GroupDomain): GenericResult<String> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.addGroup(groupDomain)
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

    override suspend fun updateGroup(groupDomain: GroupDomain): GenericResult<String> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.updateGroup(groupDomain)
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

    override suspend fun getGroups(): GenericResult<List<GroupDomain>> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.getGroups()
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

    override suspend fun deleteGroup(id: Int): GenericResult<String> =
        withContext(coroutineContextProvider.io) {
            val result = petPlayLocalProviderApi.deleteGroup(id)
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