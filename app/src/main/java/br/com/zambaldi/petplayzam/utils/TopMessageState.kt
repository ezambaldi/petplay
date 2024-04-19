package br.com.zambaldi.petplayzam.utils

sealed class TopMessageState {
    object Default : TopMessageState()
    data class Show(
        val message: String = "",
        val typeMessage: TypeMessage = TypeMessage.INFO,
        val setAsDefault: () -> Unit = { }
    ) : TopMessageState()
}