package br.com.zambaldi.petplay.models

data class AudiosGroup(
    val id: Int = 0,
    val order: Int = 0,
    val idAudio: Int,
    val idGroup: Int,
)
