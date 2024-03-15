package br.com.zambaldi.petplay.ui.groups

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.usecases.AudioGroupUseCase
import br.com.zambaldi.petplay.usecases.AudioUseCase
import br.com.zambaldi.petplay.usecases.GroupUseCase
import br.com.zambaldi.petplay.utils.TopMessageState
import br.com.zambaldi.petplay.utils.TypeMessage
import com.example.myapplicationtest.bases.BaseMviViewModel
import com.example.myapplicationtest.bases.GenericResult
import kotlinx.coroutines.launch

class GroupsViewModel(
    private val groupUseCase: GroupUseCase,
    private val audioUseCase: AudioUseCase,
    private val audioGroupUseCase: AudioGroupUseCase,
) : BaseMviViewModel<GroupsViewModel.ViewIntent, GroupsViewModel.ViewState, GroupsViewModel.ViewEffect>() {

    private val audioState = MutableLiveData<List<Audio>?>()

    private fun fetchGroupList(setLoading: Boolean) {
        if(setLoading) setState { copy(groupState = GroupState.Loading) }
        val groups: MutableList<Group> = listOf<Group>().toMutableList()
        viewModelScope.launch {
            when (val result = groupUseCase.getGroups()) {
                is GenericResult.Success -> {
                    result.data.let {
                        groups.addAll(it)
                    }
                    setState {
                        copy(
                            groupState = GroupState.Loaded(
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
                            groupState = GroupState.Error,
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
                            groupState = GroupState.Error,
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
        setState { copy(groupState = GroupState.Default) }
    }

    private fun setTopMessageStateDefault() {
        setState { copy(topMessageState = TopMessageState.Default) }
    }

    private fun deleteGroup(id: Int) {
        viewModelScope.launch {
            when (val result = groupUseCase.deleteGroup(id)) {
                is GenericResult.Success -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Group removed successfully!",
                        typeMessage = TypeMessage.SUCCESS,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchGroupList(true)
                }
                is GenericResult.Error -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Error: ${result.errorMessage}",
                        typeMessage = TypeMessage.ERROR,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchGroupList(true)
                }
            }
        }
    }

    private fun deleteAudioGroup(id: Int) {
        viewModelScope.launch {
            when (val result = audioGroupUseCase.deleteAudioGroup(id)) {
                is GenericResult.Success -> {
                    fetchGroupList(false)
                }
                is GenericResult.Error -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Error: ${result.errorMessage}",
                        typeMessage = TypeMessage.ERROR,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchGroupList(false)
                }
            }
        }
    }

    private fun addGroup(group: Group) {
        viewModelScope.launch {
            when (val result = groupUseCase.addGroup(group)) {
                is GenericResult.Success -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Group added successfully!",
                        typeMessage = TypeMessage.SUCCESS,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchGroupList(true)
                }
                is GenericResult.Error -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Error: ${result.errorMessage}",
                        typeMessage = TypeMessage.ERROR,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchGroupList(true)
                }
            }
        }
    }

    private fun addAudioGroup(audioGroup: AudiosGroup) {
        viewModelScope.launch {
            when (val result = audioGroupUseCase.addAudioGroup(
                audioGroup
                )
            ) {
                is GenericResult.Success -> {
                    fetchGroupList(false)
                }
                is GenericResult.Error -> {
                    setState { copy(topMessageState = TopMessageState.Show(
                        message = "Error: ${result.errorMessage}",
                        typeMessage = TypeMessage.ERROR,
                        setAsDefault = { setTopMessageStateDefault() }
                    )) }
                    fetchGroupList(false)
                }
            }
        }
    }

    data class ViewState(
        var groupList: MutableList<Group> = mutableStateListOf(),
        val groupState: GroupState = GroupState.Default,
        val topMessageState: TopMessageState = TopMessageState.Default,
    ) : BaseViewState

    sealed class ViewEffect : BaseViewEffect {
    }

    sealed class ViewIntent : BaseViewIntent {
        object FetchGroupList : ViewIntent()
        object FetchAudioList : ViewIntent()
        data class DeleteGroup(val id: Int): ViewIntent()
        data class AddGroup(val group: Group): ViewIntent()
        data class AddAudioGroup(val audioGroup: AudiosGroup): ViewIntent()
        data class DeleteAudioGroup(val id: Int): ViewIntent()
        object SetStateDefault: ViewIntent()
        object SetTopMessageDefault: ViewIntent()
    }

    override fun inicialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        when (intent) {
            is ViewIntent.FetchGroupList -> { fetchGroupList(false) }
            is ViewIntent.FetchAudioList -> { fetchAudioList() }
            is ViewIntent.AddGroup -> { addGroup(intent.group) }
            is ViewIntent.DeleteGroup -> { deleteGroup(intent.id) }
            is ViewIntent.AddAudioGroup -> { addAudioGroup(intent.audioGroup) }
            is ViewIntent.DeleteAudioGroup -> { deleteAudioGroup(intent.id) }
            is ViewIntent.SetStateDefault -> { setStateDefault() }
            is ViewIntent.SetTopMessageDefault -> { setTopMessageStateDefault() }
        }
    }

}