package br.com.zambaldi.petplayzam.ui.play

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.zambaldi.petplayzam.MainActivity
import br.com.zambaldi.petplayzam.utils.TopMessageState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayFragment : Fragment() {

    private val playViewModel by viewModel<PlayViewModel>()

    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateCompose(
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                playViewModel.state.collectAsState().let { state ->
                    SetScreen(
                        state = state.value.playState,
                        topMessageState = state.value.topMessageState,
                        callFetch = ::fetchGroupList,
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun addViewStateObservables() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            playViewModel.state.apply {
                launch {
                    map { it.playState }.distinctUntilChanged().collect {
                        when (it) {
                            is PlayState.Default -> {
                            }
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
                playViewModel.viewEffect.collect { effect ->

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addViewStateObservables()
        addViewEffectObservables()
        fetchAudioList()
        fetchGroupList()
        return updateCompose()
    }

    private fun fetchGroupList() {
        playViewModel.intent(PlayViewModel.ViewIntent.FetchGroupList)
    }

    private fun fetchAudioList() {
        playViewModel.intent(PlayViewModel.ViewIntent.FetchAudioList)
    }

    private fun setTopMessageDefault() {
        playViewModel.intent(PlayViewModel.ViewIntent.SetTopMessageDefault)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    private fun SetScreen(
        state: PlayState,
        topMessageState: TopMessageState,
        callFetch: () -> Unit,
    ) {
        PlayListScreen(
            state = state,
            mainActivity = requireActivity() as MainActivity,
        )
    }

}