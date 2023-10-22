package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

interface LocalGroupRepository {

    suspend fun addGroup(groupDomain: GroupDomain)
    suspend fun getGroups(): List<GroupDomain>
    suspend fun deleteGroup(id: Int)

}