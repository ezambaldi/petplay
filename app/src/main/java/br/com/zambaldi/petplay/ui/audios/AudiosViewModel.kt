package br.com.zambaldi.petplay.ui.audios

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.usecases.AudioUseCase
import com.example.myapplicationtest.bases.BaseMviViewModel
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudiosViewModel(
    private val audioUseCase: AudioUseCase
) : BaseMviViewModel<AudiosViewModel.ViewIntent, AudiosViewModel.ViewState, AudiosViewModel.ViewEffect>() {

    private fun fetchAudioList(
        isShowTopMessage: Boolean = false,
        topMessage: String = "",
        typeMessage: TypeMessage = TypeMessage.INFO
    ) {
        setState { copy(audioState = AudioState.Loading) }
        val audios: MutableList<Audio> = listOf<Audio>().toMutableList()
        viewModelScope.launch {
            when (val result = audioUseCase.getAudios()) {
                is GenericResult.Success -> {
                    result.data.let {
                        audios.addAll(it)
                    }
                    setState {
                        copy(
                            audioState = AudioState.Loaded(
                                audios,
                                isShowTopMessage = isShowTopMessage,
                                topMessage = topMessage,
                                typeMessage = typeMessage
                            ),
                        )
                    }
                }
                is GenericResult.Error -> {
                    audios.addAll(emptyList())
                }
            }
        }
    }

    private fun setStateDefault() {
        setState { copy(audioState = AudioState.Default) }
    }

    private fun deleteAudio(id: Int) {
        viewModelScope.launch {
            when (val result = audioUseCase.deleteAudio(id)) {
                is GenericResult.Success -> {
                    fetchAudioList(
                        isShowTopMessage = true,
                        topMessage = "",
                        typeMessage = TypeMessage.SUCCESS
                    )
                }
                is GenericResult.Error -> {
                    fetchAudioList(
                        isShowTopMessage = true,
                        topMessage = result.errorMessage,
                        typeMessage = TypeMessage.ERROR
                    )
                }
            }
        }
    }

    private fun addAudio(audio: Audio) {
        viewModelScope.launch {
            when (val result = audioUseCase.addAudio(audio)) {
                is GenericResult.Success -> {
                    fetchAudioList(
                        isShowTopMessage = true,
                        topMessage = "",
                        typeMessage = TypeMessage.SUCCESS
                    )
                }
                is GenericResult.Error -> {
                    fetchAudioList(
                        isShowTopMessage = true,
                        topMessage = result.errorMessage,
                        typeMessage = TypeMessage.ERROR
                    )
                }
            }


        }
    }

    data class ViewState(
        var audioList: MutableList<Audio> = mutableStateListOf(),
        val audioState: AudioState = AudioState.Default
    ) : BaseViewState

    sealed class ViewEffect : BaseViewEffect {
//        data class SetSelectedItem(val id: String) : ViewEffect()
//        data class ShowTopMessage(val message: String, val typeMessage: TypeMessage) : ViewEffect()
    }

    sealed class ViewIntent : BaseViewIntent {
        data class FetchAudioList(
            val isShowTopMessage: Boolean,
            val topMessage: String,
            val typeMessage: TypeMessage
        ) : ViewIntent()
        data class DeleteAudio(val id: Int): ViewIntent()
        data class AddAudio(val audio: Audio): ViewIntent()
        object setStateDefault: ViewIntent()
    }

    override fun inicialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        when (intent) {
            is ViewIntent.FetchAudioList -> { fetchAudioList() }
            is ViewIntent.AddAudio -> { addAudio(intent.audio) }
            is ViewIntent.DeleteAudio -> { deleteAudio(intent.id) }
            is ViewIntent.setStateDefault -> { setStateDefault() }
        }
    }

}
