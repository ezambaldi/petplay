package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.Group
import com.example.myapplicationtest.bases.GenericResult

interface GroupUseCase {

    suspend fun addGroup(group: Group): GenericResult<String>
    suspend fun getGroups(): GenericResult<List<Group>>
    suspend fun deleteGroup(id: Int): GenericResult<String>

}