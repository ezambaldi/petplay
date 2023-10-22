package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.datasource.local.api.PetPlayLocalProviderApi
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

class LocalGroupRepositoryImpl(
    private val petPlayLocalProviderApi: PetPlayLocalProviderApi,
): LocalGroupRepository {

    override suspend fun addGroup(groupDomain: GroupDomain) {
        petPlayLocalProviderApi.addGroup(groupDomain)
    }

    override suspend fun getGroups(): List<GroupDomain> {
        return petPlayLocalProviderApi.getGroups()
    }

    override suspend fun deleteGroup(id: Int) {
        petPlayLocalProviderApi.deleteGroup(id)
    }

}