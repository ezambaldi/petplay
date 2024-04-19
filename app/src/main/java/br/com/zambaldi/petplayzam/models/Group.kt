package br.com.zambaldi.petplayzam.models

data class Group(
    val id: Int = 0,
    var name: String = "",
    var audios: List<AudiosGroup> = listOf(),
    var dateStart: String = "",
    var dateFinish: String = "",
    var timeStart: String = "",
    var timeFinish: String = "",
    var intervalSecond: Int = 3,
    var interactionType: InteractionType = InteractionType.SHAKE
)