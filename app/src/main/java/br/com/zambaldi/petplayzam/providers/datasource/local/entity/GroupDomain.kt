package br.com.zambaldi.petplayzam.providers.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.zambaldi.petplayzam.models.InteractionType

@Entity(tableName = "groups")
data class GroupDomain(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String = "",
    val dateStart: String = "",
    val dateFinish: String = "",
    val timeStart: String = "",
    val timeFinish: String = "",
    val intervalSecond: Int = 1,
    val interactionType: InteractionType = InteractionType.SHAKE

)