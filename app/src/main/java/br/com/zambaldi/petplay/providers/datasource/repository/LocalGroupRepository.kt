package br.com.zambaldi.petplay.providers.datasource.repository

import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain
import com.example.myapplicationtest.bases.GenericResult

interface LocalGroupRepository {

    suspend fun addGroup(groupDomain: GroupDomain): GenericResult<String>
    suspend fun getGroups(): GenericResult<List<GroupDomain>>
    suspend fun deleteGroup(id: Int): GenericResult<String>

}