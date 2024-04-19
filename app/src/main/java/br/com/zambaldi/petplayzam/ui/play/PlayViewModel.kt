package br.com.zambaldi.petplayzam.ui.play

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zambaldi.petplayzam.models.Audio
import br.com.zambaldi.petplayzam.models.Group
import br.com.zambaldi.petplayzam.usecases.AudioUseCase
import br.com.zambaldi.petplayzam.usecases.GroupUseCase
import br.com.zambaldi.petplayzam.utils.TopMessageState
import br.com.zambaldi.petplayzam.utils.TypeMessage
import com.example.myapplicationtest.bases.BaseMviViewModel
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.launch

class PlayViewModel(
    private val groupUseCase: GroupUseCase,
    private val audioUseCase: AudioUseCase,
) : BaseMviViewModel<PlayViewModel.ViewIntent, PlayViewModel.ViewState, PlayViewModel.ViewEffect>() {

    private val audioState = MutableLiveData<List<Audio>?>()

    private fun fetchGroupList() {
        setState { copy(playState = PlayState.Loading) }
        val groups: MutableList<Group> = listOf<Group>().toMutableList()
        viewModelScope.launch {
            when (val result = groupUseCase.getGroups()) {
                is GenericResult.Success -> {
                    result.data.let {
                        groups.addAll(it)
                    }
                    setState {
                        copy(
                            playState = PlayState.Loaded(
                                groups,
                                audios = audioState.value?.toMutableList()?: mutableListOf(),
                            ),
                        )
                    }
                }
                is GenericResult.Error -> {
                    groups.addAll(emptyList())
                    setState {
                        copy(
                            playState = PlayState.Error,
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

    private fun fetchAudioList() {
        viewModelScope.launch {
            when (val result = audioUseCase.getAudios()) {
                is GenericResult.Success -> {
                    audioState.value =  result.data
                }
                is GenericResult.Error -> {
                    setState {
                        copy(
                            playState = PlayState.Error,
                            topMessageState = TopMessageState.Show(
                                message = "${result.errorTitle} - ${result.errorMessage}",
                                typeMessage = TypeMessage.ERROR,
                            ),
                        )
                    }
                    audioState.value = emptyList()
                }
            }

        }
    }


    private fun setStateDefault() {
        setState { copy(playState = PlayState.Default) }
    }

    private fun setTopMessageStateDefault() {
        setState { copy(topMessageState = TopMessageState.Default) }
    }

    data class ViewState(
        var groupList: MutableList<Group> = mutableStateListOf(),
        val playState: PlayState = PlayState.Default,
        val topMessageState: TopMessageState = TopMessageState.Default,
    ) : BaseViewState

    sealed class ViewEffect : BaseViewEffect {
    }

    sealed class ViewIntent : BaseViewIntent {
        object FetchAudioList : ViewIntent()
        object FetchGroupList : ViewIntent()
        object SetStateDefault: ViewIntent()
        object SetTopMessageDefault: ViewIntent()
    }

    override fun inicialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        when (intent) {
            is ViewIntent.FetchGroupList -> { fetchGroupList() }
            is ViewIntent.FetchAudioList -> { fetchAudioList() }
            is ViewIntent.SetStateDefault -> { setStateDefault() }
            is ViewIntent.SetTopMessageDefault -> { setTopMessageStateDefault() }
        }
    }

}