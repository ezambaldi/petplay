package br.com.zambaldi.petplay.providers.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.zambaldi.petplay.models.InteractionType

@Entity(tableName = "groups")
data class GroupDomain(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String = "",
    val dateTimeStart: String = "",
    val dateTimeFinish: String = "",
    val intervalSecond: Int = 1,
    val interactionType: InteractionType = InteractionType.SEQUENCE

)