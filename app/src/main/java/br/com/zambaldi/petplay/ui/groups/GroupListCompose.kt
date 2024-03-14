package br.com.zambaldi.petplay.ui.groups

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.ui.AlertDialogWithBtn
import br.com.zambaldi.petplay.ui.ScreenEmpty
import br.com.zambaldi.petplay.ui.ScreenLoading
import br.com.zambaldi.petplay.ui.TopMessage
import br.com.zambaldi.petplay.utils.SnackBarContainer
import br.com.zambaldi.petplay.utils.TopMessageState
import com.example.myapplicationtest.utils.bodyLarge
import com.example.myapplicationtest.utils.bodyLargeBold
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun GroupListScreen(
    state: GroupState,
    topMessageState: TopMessageState,
    callFetch: () -> Unit,
    deleteGroup: (id: Int) -> Unit,
    addGroup: (Group) -> Unit,
    deleteAudioGroup: (id: Int) -> Unit,
    addAudioGroup: (AudiosGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val errorSnackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            if(topMessageState is TopMessageState.Show) {
                TopMessage(
                    message = topMessageState.message,
                    typeMessage = topMessageState.typeMessage,
                    scope = scope,
                    errorSnackBarHostState = errorSnackBarHostState,
                )
                topMessageState.setAsDefault()
            }
            SnackBarContainer(snackBarHostState = errorSnackBarHostState)
        },
        floatingActionButton = {
            var onDismiss = remember { mutableStateOf(false) }
            val txtFieldError = remember { mutableStateOf("") }
            val txtField = remember { mutableStateOf("") }
            if(onDismiss.value) {
                Dialog(onDismissRequest = { } ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.title_add_group),
                                        style = bodyLargeBold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "",
                                        tint = colorResource(android.R.color.darker_gray),
                                        modifier = Modifier
                                            .clickable { onDismiss.value = false }
                                            .width(30.dp)
                                            .height(30.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            BorderStroke(
                                                width = 2.dp,
                                                color = colorResource(id = if (txtFieldError.value.isEmpty()) android.R.color.holo_green_light else android.R.color.holo_red_dark)
                                            ),
                                            shape = RoundedCornerShape(50)
                                        ),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    leadingIcon = {
                                        Icon(
                                            painterResource(id = R.drawable.ic_launcher_foreground),
                                            contentDescription = "",
                                            tint = colorResource(android.R.color.holo_green_light),
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(20.dp)
                                        )
                                    },
                                    placeholder = { Text(text = stringResource(id = R.string.enter_name)) },
                                    value = txtField.value,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    onValueChange = {
                                        txtField.value = it.take(40)
                                    })

                                Spacer(modifier = Modifier.height(20.dp))

                                val msgError = stringResource(id = R.string.msg_empty_field)
                                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                                    Button(
                                        onClick = {
                                            if (txtField.value.isEmpty()) {
                                                txtFieldError.value = msgError
                                                return@Button
                                            }
                                            addGroup(
                                                Group(
                                                    name = txtField.value,
                                                )
                                            )
                                            onDismiss.value = false
                                            txtField.value = ""
                                        },
                                        shape = RoundedCornerShape(50.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                    ) {
                                        Text(text = stringResource(id = R.string.btn_confirm))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    onDismiss.value = true
                }
                ,
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.md_theme_dark_tertiaryContainer),
                modifier = Modifier
                    .padding(end = 32.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_add),
                    "Floating action button."
                )
            }
        },
        content = { paddingValues ->
            Log.d("Padding values", "$paddingValues")
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = callFetch,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(
                        vertical = 1.dp,
                    )
            ) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    when (state) {
                        is GroupState.Loading -> {
                            ScreenLoading()
                        }
                        is GroupState.Loaded -> {
                            if(state.data.isNotEmpty()) {
                                GroupScreenSuccess(
                                    state = state,
                                    deleteGroup = deleteGroup,
                                    deleteAudioGroup = deleteAudioGroup,
                                    addAudioGroup = addAudioGroup,
                                    callFetch = callFetch,
                                )
                            } else {
                                ScreenEmpty(
                                    modifier = modifier
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    )


}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreenSuccess(
    state: GroupState.Loaded,
    deleteGroup: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    deleteAudioGroup: (id: Int) -> Unit,
    addAudioGroup: (AudiosGroup) -> Unit,
    callFetch: () -> Unit,
) {
    val selectedGroup = remember { mutableStateOf(Group()) }
    val openDialog = remember { mutableStateOf(false) }
    val groupToDelete = remember { mutableIntStateOf(0) }
    val groupName = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }

    val audios = state.audios
    var setAudioGroup = listOf<AudiosGroup>()

    if(selectedGroup.value.id > 0) {
        setAudioGroup = state.data.filter {
            it.id == selectedGroup.value.id
        }.first().audios
    }

    val audioGroup = setAudioGroup.toMutableList()

    val groups: List<Group> = state.data
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                groups.forEach { group ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = stringResource(id = R.string.touch_for_include_audios),
                            modifier = modifier
                                .clickable {
                                    selectedGroup.value = group
                                    showBottomSheet.value = true

                                }
                        )
                        Text(
                            text = group.name,
                            style = bodyLarge,
                            color = colorResource(id = R.color.md_theme_dark_onTertiary),
                            modifier = modifier
                                .padding(start = 4.dp)
                        )
                        Spacer(Modifier.weight(1f))

                        Image(
                            painter = painterResource(id = R.drawable.ic_remove),
                            contentDescription = stringResource(id = R.string.touch_for_remove),
                            modifier = modifier
                                .clickable {
                                    groupToDelete.value = group.id
                                    groupName.value = group.name
                                    openDialog.value = true
                                }
                        )
                    }
                }
            }
        }
    }

    if (openDialog.value) {
        AlertDialogWithBtn(
            onConfirmation = {
                deleteGroup(groupToDelete.intValue)
            },
            dialogTitle = groupName.value,
            dialogText = stringResource(id = R.string.msg_remove_item),
            openDialog = openDialog,
        )
    }

    if (showBottomSheet.value) {
        GroupListAudiosBottomSheet(
            groupId = selectedGroup.value.id,
            groupName = selectedGroup.value.name,
            sheetState = sheetState,
            coroutineScope = scope,
            onNegativeButtonOrCloseClick = {
                showBottomSheet.value = false
            },
            audios = audios.toMutableList(),
            audioGroup = audioGroup.toMutableList(),
            deleteAudioGroup = deleteAudioGroup,
            addAudioGroup = addAudioGroup,
            callFetch = callFetch,
            applicationContext = LocalContext.current
        )
    }

}
