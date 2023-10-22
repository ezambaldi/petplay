package br.com.zambaldi.petplay.providers.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

@Dao
interface GroupDAO {

    @Insert
    fun add(groupDomain: GroupDomain)

    @Query("SELECT * FROM groups ORDER BY name")
    fun getGroups(): List<GroupDomain>

    @Query("DELETE FROM groups WHERE id = :id")
    fun deleteGroup(id: Int)
}