package br.com.zambaldi.petplay.ui.audios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudiosFragment : Fragment(R.layout.fragment_audios) {

    private val audiosViewModel by viewModel<AudiosViewModel>()

    private fun updateCompose(
        ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                audiosViewModel.state.collectAsState().let { state ->
                    SetScreen(
                        state = state.value.audioState,
                        callFetch = ::fetchAudioList,
                        deleteAudio = ::deleteAudio,
                        addAudio = ::addAudio,
                    )
                }
            }
        }
    }

    private fun addViewStateObservables() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            audiosViewModel.state.apply {
                launch {
                    map { it.audioState }.distinctUntilChanged().collect {
                        when (it) {
                            is AudioState.Default -> {}
                            else -> {
                                updateCompose()
                            }

                        }
                    }
                }
            }
        }
    }

    private fun addViewEffectObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                audiosViewModel.viewEffect.collect { effect ->
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addViewStateObservables()
        addViewEffectObservables()

//        for (i in 1..10) {
//            audiosViewModel.intent((AudiosViewModel.ViewIntent.AddAudio(
//                Audio(
//                    name = "Audio " + i,
//                    path = ""
//                )
//            )))
//        }

        fetchAudioList()
        return updateCompose()

    }

    private fun fetchAudioList(
        isShowTopMessage: Boolean = false,
        topMessage: String = "",
        typeMessage: TypeMessage = TypeMessage.INFO
    ) {
        audiosViewModel.intent(AudiosViewModel.ViewIntent.FetchAudioList(
            isShowTopMessage = isShowTopMessage,
            topMessage = topMessage,
            typeMessage = typeMessage
        ))
    }

    private fun deleteAudio(id: Int = 0) {
        audiosViewModel.intent(AudiosViewModel.ViewIntent.DeleteAudio(id))
    }

    private fun addAudio(audio: Audio) {
        audiosViewModel.intent(AudiosViewModel.ViewIntent.AddAudio(audio))
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    @Composable
    private fun SetScreen(
        state: AudioState,
        callFetch: () -> Unit,
        deleteAudio: (id: Int) -> Unit,
        addAudio: (Audio) -> Unit,
    ) {
        AudioListScreen(
            state = state,
            callFetch = callFetch,
            deleteAudio = deleteAudio,
            addAudio = addAudio,
        )
    }

}