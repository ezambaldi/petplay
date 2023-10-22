package br.com.zambaldi.petplay.models

data class Group(
    val id: Int = 0,
    val name: String = "",
    val dateTimeStart: String = "",
    val dateTimeFinish: String = "",
    val intervalSecond: Int = 1,
    val interactionType: InteractionType = InteractionType.SEQUENCE
)