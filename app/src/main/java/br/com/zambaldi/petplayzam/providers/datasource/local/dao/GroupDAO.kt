package br.com.zambaldi.petplayzam.providers.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.zambaldi.petplayzam.models.InteractionType
import br.com.zambaldi.petplayzam.providers.datasource.local.entity.GroupDomain

@Dao
interface GroupDAO {

    @Insert
    fun add(groupDomain: GroupDomain)

    @Query("UPDATE groups SET " +
            "name = :name, " +
            "dateStart = :dateStart, " +
            "dateFinish = :dateFinish, " +
            "timeStart = :timeStart, " +
            "timeFinish = :timeFinish, " +
            "intervalSecond = :intervalSecond, " +
            "interactionType = :interactionType WHERE id = :id")
    fun update(id: Int, name: String, dateStart: String, dateFinish: String, timeStart: String, timeFinish: String, intervalSecond: Int, interactionType: InteractionType)

    @Query("SELECT * FROM groups ORDER BY name")
    fun getGroups(): List<GroupDomain>

    @Query("DELETE FROM groups WHERE id = :id")
    fun deleteGroup(id: Int)
}