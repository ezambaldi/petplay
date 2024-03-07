package br.com.zambaldi.petplay.ui.groups

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
import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.utils.TopMessageState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupsFragment : Fragment(R.layout.fragment_groups) {

    private val groupsViewModel by viewModel<GroupsViewModel>()

    private fun updateCompose(
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                groupsViewModel.state.collectAsState().let { state ->
                    SetScreen(
                        state = state.value.groupState,
                        topMessageState = state.value.topMessageState,
                        callFetch = ::fetchGroupList,
                        deleteGroup = ::deleteGroup,
                        addGroup = ::addGroup,
                    )
                }
            }
        }
    }

    private fun addViewStateObservables() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            groupsViewModel.state.apply {
                launch {
                    map { it.groupState }.distinctUntilChanged().collect {
                        when (it) {
                            is GroupState.Default -> {
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
                groupsViewModel.viewEffect.collect { effect ->

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
        fetchGroupList()
        return updateCompose()
    }

    private fun fetchGroupList() {
        groupsViewModel.intent(GroupsViewModel.ViewIntent.FetchGroupList)
    }

    private fun deleteGroup(id: Int = 0) {
        groupsViewModel.intent(GroupsViewModel.ViewIntent.DeleteGroup(id))
    }

    private fun addGroup(group: Group) {
        groupsViewModel.intent(GroupsViewModel.ViewIntent.AddGroup(group))
    }

    private fun setTopMessageDefault() {
        groupsViewModel.intent(GroupsViewModel.ViewIntent.SetTopMessageDefault)
    }

    @Composable
    private fun SetScreen(
        state: GroupState,
        topMessageState: TopMessageState,
        callFetch: () -> Unit,
        deleteGroup: (id: Int) -> Unit,
        addGroup: (Group) -> Unit,
    ) {
        GroupListScreen(
            state = state,
            topMessageState = topMessageState,
            callFetch = callFetch,
            deleteGroup = deleteGroup,
            addGroup = addGroup,
        )
    }

}