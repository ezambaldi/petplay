package br.com.zambaldi.petplay.providers.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain

@Dao
interface AudioDAO {

    @Insert
    fun add(audioDomain: AudioDomain)

    @Query("SELECT * FROM audios ORDER BY name")
    fun getAudios(): List<AudioDomain>

    @Query("DELETE FROM audios WHERE id = :id")
    fun deleteAudio(id: Int)
}