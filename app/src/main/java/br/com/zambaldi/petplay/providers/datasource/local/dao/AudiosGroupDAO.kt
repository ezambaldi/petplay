package br.com.zambaldi.petplay.providers.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

@Dao
interface AudiosGroupDAO {

    @Insert
    fun add(audiosGroupDomain: AudiosGroupDomain)

    @Query("SELECT * FROM audiosGroup ORDER BY `order`")
    fun getAudiosGroup(): List<AudiosGroupDomain>

    @Query("DELETE FROM audiosGroup WHERE id = :id")
    fun deleteAudioGroup(id: Int)

}