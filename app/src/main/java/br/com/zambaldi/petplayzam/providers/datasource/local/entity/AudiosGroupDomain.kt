package br.com.zambaldi.petplayzam.providers.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audiosGroup")
data class AudiosGroupDomain(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val order: Int = 0,
    val idAudio: Int,
    val idGroup: Int,
)
