package br.com.zambaldi.petplayzam.providers.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audios")
data class AudioDomain(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String = "",
    val path: String = "",
    var isSelected: Boolean = false

)
