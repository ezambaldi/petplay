package br.com.zambaldi.petplay.usecases

import br.com.zambaldi.petplay.models.Group

interface GroupUseCase {

    suspend fun addGroup(group: Group)
    suspend fun getGroups(): List<Group>
    suspend fun deleteGroup(id: Int)

}