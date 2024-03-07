package br.com.zambaldi.petplay.ui.audios

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.usecases.AudioUseCase
import br.com.zambaldi.petplay.utils.TopMessageState
import br.com.zambaldi.petplay.utils.TypeMessage
import com.example.myapplicationtest.bases.BaseMviViewModel
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.launch

class AudiosViewModel(
    private val audioUseCase: AudioUseCase
) : BaseMviViewModel<AudiosViewModel.ViewIntent, AudiosViewModel.ViewState, AudiosViewModel.ViewEffect>() {

    private fun fetchAudioList() {
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
                            ),
                        )
                    }
                }
                is GenericResult.Error -> {
                    audios.addAll(emptyList())
                    setState {
                        copy(
                            audioState = AudioState.Error,
                            topMessageState = TopMessageState.Show(
                                message = "${result.errorTitle} - ${result.errorMessage}",
                                typeMessage = TypeMessage.ERROR,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun setStateDefault() {
        setState { copy(audioState = AudioState.Default) }
    }

    private fun setTopMessageStateDefault() {
        setState { copy(topMessageState = TopMessageState.Default) }
    }

    private fun deleteAudio(id: Int) {
        viewModelScope.launch {
            when (val result = audioUseCase.deleteAudio(id)) {
                is GenericResult.Success -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Audio removed successfully!",
                        typeMessage = TypeMessage.SUCCESS,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchAudioList()
                }
                is GenericResult.Error -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Error: ${result.errorMessage}",
                        typeMessage = TypeMessage.ERROR,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchAudioList()
                }
            }
        }
    }

    private fun addAudio(audio: Audio) {
        viewModelScope.launch {
            when (val result = audioUseCase.addAudio(audio)) {
                is GenericResult.Success -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Audio added successfully!",
                        typeMessage = TypeMessage.SUCCESS,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchAudioList()
                }
                is GenericResult.Error -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Error: ${result.errorMessage}",
                        typeMessage = TypeMessage.ERROR,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchAudioList()
                }
            }
        }
    }

    data class ViewState(
        var audioList: MutableList<Audio> = mutableStateListOf(),
        val audioState: AudioState = AudioState.Default,
        val topMessageState: TopMessageState = TopMessageState.Default,
    ) : BaseViewState

    sealed class ViewEffect : BaseViewEffect {
//        data class SetSelectedItem(val id: String) : ViewEffect()
//        data class ShowTopMessage(val topMessageState: TopMessageState) : ViewEffect()
    }

    sealed class ViewIntent : BaseViewIntent {
        object FetchAudioList : ViewIntent()
        data class DeleteAudio(val id: Int): ViewIntent()
        data class AddAudio(val audio: Audio): ViewIntent()
        object SetStateDefault: ViewIntent()
        object SetTopMessageDefault: ViewIntent()
    }

    override fun inicialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        when (intent) {
            is ViewIntent.FetchAudioList -> { fetchAudioList() }
            is ViewIntent.AddAudio -> { addAudio(intent.audio) }
            is ViewIntent.DeleteAudio -> { deleteAudio(intent.id) }
            is ViewIntent.SetStateDefault -> { setStateDefault() }
            is ViewIntent.SetTopMessageDefault -> { setTopMessageStateDefault() }
        }
    }

}
