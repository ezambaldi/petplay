package br.com.zambaldi.petplayzam.usecases

import br.com.zambaldi.petplayzam.models.Group
import com.example.myapplicationtest.bases.GenericResult

interface GroupUseCase {

    suspend fun addGroup(group: Group): GenericResult<String>
    suspend fun updateGroup(group: Group): GenericResult<String>
    suspend fun getGroups(): GenericResult<List<Group>>
    suspend fun deleteGroup(id: Int): GenericResult<String>

}