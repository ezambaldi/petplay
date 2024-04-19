package br.com.zambaldi.petplayzam.ui.groups

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.zambaldi.petplayzam.R
import br.com.zambaldi.petplayzam.models.AudiosGroup
import br.com.zambaldi.petplayzam.models.Group
import br.com.zambaldi.petplayzam.models.InteractionType
import br.com.zambaldi.petplayzam.ui.AlertDialogWithBtn
import br.com.zambaldi.petplayzam.ui.CustomCalendarView
import br.com.zambaldi.petplayzam.ui.CustomTimeView
import br.com.zambaldi.petplayzam.ui.ScreenEmpty
import br.com.zambaldi.petplayzam.ui.ScreenLoading
import br.com.zambaldi.petplayzam.ui.TopMessage
import br.com.zambaldi.petplayzam.utils.SnackBarContainer
import br.com.zambaldi.petplayzam.utils.TopMessageState
import com.example.myapplicationtest.utils.bodyLarge
import com.example.myapplicationtest.utils.bodyLargeBold
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
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
    val selectedGroup = remember { mutableStateOf(Group()) }
    val openDialog = remember { mutableStateOf(false) }
    val groupToDelete = remember { mutableIntStateOf(0) }
    val groupName = remember { mutableStateOf("") }
    val groupInterval = remember { mutableFloatStateOf(1F) }
    val groupInteractionType = remember { mutableStateOf(InteractionType.SHAKE) }
    val showBottomSheet = remember { mutableStateOf(false) }
    val localDate = LocalDate.now()
    val localTimeStart = rememberTimePickerState(11,30, false)
    val localTimeFinish = rememberTimePickerState(11,30, false)
    val groupDateStart = remember { mutableStateOf(localDate) }
    val groupDateFinish = remember { mutableStateOf(localDate) }
    val groupTimeStart = remember { mutableStateOf(localTimeStart) }
    val groupTimeFinish = remember { mutableStateOf(localTimeFinish) }

    if(!showBottomSheet.value) {
        val editGroup = remember { mutableStateOf(Group()) }
        var onDismiss = remember { mutableStateOf(false) }
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
                var showDatePickerStart = remember { mutableStateOf(false) }
                var showDatePickerFinish = remember { mutableStateOf(false) }
                var showTimePickerStart = remember { mutableStateOf(false) }
                var showTimePickerFinish = remember { mutableStateOf(false) }
                val txtFieldError = remember { mutableStateOf("") }

                if(onDismiss.value) {

                    if(showDatePickerStart.value) {
                        CustomCalendarView(
                            onDateSelected = {
                                groupDateStart.value = it
                                showDatePickerStart.value = false
                            }
                        )
                    }

                    if(showDatePickerFinish.value) {
                        CustomCalendarView(
                            onDateSelected = {
                                groupDateFinish.value = it
                                showDatePickerFinish.value = false
                            }
                        )
                    }

                    if(showTimePickerStart.value) {
                        CustomTimeView(
                            timePickerState = localTimeStart,
                            onClose = {
                                groupTimeStart.value = it
                                showTimePickerStart.value = false
                            }
                        )
                    }

                    if(showTimePickerFinish.value) {
                        CustomTimeView(
                            timePickerState = localTimeFinish,
                            onClose = {
                                groupTimeFinish.value = it
                                showTimePickerFinish.value = false
                            }
                        )
                    }

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
                                                .semantics { contentDescription = "close" }
                                                .clickable { onDismiss.value = false }
                                                .width(30.dp)
                                                .height(30.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    TextField(
                                        modifier = Modifier
                                            .semantics { contentDescription = "Enter name" }
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
                                        value = groupName.value,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        onValueChange = {
                                            groupName.value = it.take(40)
                                        })

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.45f)
                                        ) {
                                            Button(
                                                onClick = {
                                                    showDatePickerStart.value = true
                                                },
                                                shape = RoundedCornerShape(30.dp),
                                                modifier = Modifier
                                                    .semantics { contentDescription = "Select start date" }
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                            ) {
                                                Text(
                                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                                    textAlign = TextAlign.Center,
                                                    text = stringResource(id = R.string.enter_date_start) + groupDateStart.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.83f)
                                        ) {
                                            Button(
                                                onClick = {
                                                    showTimePickerStart.value = true
                                                },
                                                shape = RoundedCornerShape(30.dp),
                                                modifier = Modifier
                                                    .semantics { contentDescription = "Select start time" }
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                            ) {
                                                Text(
                                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                                    textAlign = TextAlign.Center,
                                                    text = stringResource(id = R.string.enter_time_start) + groupTimeStart.value.hour.toString().padStart(2, "0".first()) + ":" + groupTimeStart.value.minute.toString().padEnd(2, "0".first()))
                                            }
                                        }

                                    }


                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.45f)
                                        ) {
                                            Button(
                                                onClick = {
                                                    showDatePickerFinish.value = true
                                                },
                                                shape = RoundedCornerShape(30.dp),
                                                modifier = Modifier
                                                    .semantics { contentDescription = "Select end date" }
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                            ) {
                                                Text(
                                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                                    textAlign = TextAlign.Center,
                                                    text = stringResource(id = R.string.enter_date_finish) + groupDateFinish.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.83f)
                                        ) {
                                            Button(
                                                onClick = {
                                                    showTimePickerFinish.value = true
                                                },
                                                shape = RoundedCornerShape(30.dp),
                                                modifier = Modifier
                                                    .semantics { contentDescription = "Select end time" }
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                            ) {
                                                Text(
                                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                                    textAlign = TextAlign.Center,
                                                    text = stringResource(id = R.string.enter_time_finish) + groupTimeFinish.value.hour.toString().padStart(2, "0".first()) + ":" + groupTimeFinish.value.minute.toString().padEnd(2, "0".first()))
                                            }
                                        }

                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        textAlign = TextAlign.End,
                                        text = "Select the interval between audios",
                                    )

                                    Slider(
                                        modifier = Modifier
                                            .semantics { contentDescription = "select the interval betwen audios" },
                                        steps = 60,
                                        valueRange = 3f..60f,
                                        value = groupInterval.floatValue,
                                        onValueChange = { groupInterval.floatValue = it }
                                    )
                                    Text(
                                        textAlign = TextAlign.End,
                                        text = "${groupInterval.floatValue.toInt()} seconds"
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(
                                        Modifier
                                            .align(Alignment.Start)
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {

                                        RadioButton(
                                            modifier = Modifier
                                                .semantics { contentDescription = "Shake option" },
                                        selected = groupInteractionType.value == InteractionType.SHAKE,
                                            onClick = { groupInteractionType.value = InteractionType.SHAKE }
                                        )
                                        Text(
                                            text = "Shake",
                                            modifier = Modifier.padding(start = 4.dp)
                                        )

                                        RadioButton(
                                            modifier = Modifier
                                                .semantics { contentDescription = "Sequential option" },
                                            selected = groupInteractionType.value == InteractionType.SEQUENCE,
                                            onClick = { groupInteractionType.value = InteractionType.SEQUENCE }
                                        )
                                        Text(
                                            text = "Sequence",
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    val msgError = stringResource(id = R.string.msg_empty_field)
                                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                                        Button(
                                            onClick = {
                                                if (groupName.value.isEmpty()) {
                                                    txtFieldError.value = msgError
                                                    return@Button
                                                }
                                                editGroup.value.name = groupName.value
                                                editGroup.value.dateStart = groupDateStart.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                editGroup.value.dateFinish = groupDateFinish.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                editGroup.value.timeStart = groupTimeStart.value.hour.toString().padStart(2, "0".first()) + ":" + localTimeStart.minute.toString().padEnd(2, "0".first())
                                                editGroup.value.timeFinish = groupTimeFinish.value.hour.toString().padStart(2, "0".first()) + ":" + localTimeFinish.minute.toString().padEnd(2, "0".first())
                                                editGroup.value.intervalSecond = groupInterval.floatValue.toInt()
                                                editGroup.value.interactionType = groupInteractionType.value
                                                addGroup(
                                                    editGroup.value
                                                )
                                                editGroup.value = Group()
                                                onDismiss.value = false

                                            },
                                            shape = RoundedCornerShape(50.dp),
                                            modifier = Modifier
                                                .semantics { contentDescription = "Touch to confirm" }
                                                .fillMaxWidth()
                                                .height(50.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.btn_confirm))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                FloatingActionButton(
                    onClick = {
                        editGroup.value = Group(
                            dateStart = groupDateStart.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            dateFinish = groupDateFinish.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            timeStart = groupTimeStart.value.hour.toString() + ":" + localTimeStart.minute.toString(),
                            timeFinish = groupTimeFinish.value.hour.toString() + ":" + localTimeFinish.minute.toString(),
                            intervalSecond = 3,
                            interactionType = InteractionType.SHAKE
                        )
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
                                                            text = "${group.name} (${group.audios.size})",
                                                            style = bodyLarge,
                                                            color = colorResource(id = R.color.md_theme_dark_onTertiary),
                                                            modifier = modifier
                                                                .padding(start = 4.dp)
                                                        )
                                                        Spacer(Modifier.weight(1f))

                                                        Image(
                                                            painter = painterResource(id = R.drawable.ic_edit),
                                                            contentDescription = stringResource(id = R.string.touch_for_edit),
                                                            modifier = modifier
                                                                .padding(end = 12.dp)
                                                                .clickable {
                                                                    onDismiss.value = true
                                                                    editGroup.value = group
                                                                    groupDateStart.value = LocalDate.parse(group.dateStart, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                                    groupDateFinish.value = LocalDate.parse(group.dateFinish, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                                    groupTimeStart.value = TimePickerState(group.timeStart.split(":")[0].toInt(), group.timeStart.split(":")[1].toInt(), false)
                                                                    groupTimeFinish.value = TimePickerState(group.timeFinish.split(":")[0].toInt(), group.timeFinish.split(":")[1].toInt(), false)
                                                                    groupName.value = group.name
                                                                    groupInterval.floatValue = group.intervalSecond.toFloat()
                                                                    groupInteractionType.value = group.interactionType
                                                                }
                                                        )

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
                                    }

                                    if (showBottomSheet.value) {
                                        GroupListAudiosBottomSheet(
                                            groupId = selectedGroup.value.id,
                                            groupName = selectedGroup.value.name,
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

    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(8.dp)
        ) {
            when (state) {
                is GroupState.Loaded -> {
                        val audios = state.audios
                        var setAudioGroup = listOf<AudiosGroup>()

                        if(selectedGroup.value.id > 0) {
                            setAudioGroup = state.data.filter {
                                it.id == selectedGroup.value.id
                            }.first().audios
                        }
                        val audioGroup = setAudioGroup.toMutableList()

                        GroupListAudiosBottomSheet(
                            groupId = selectedGroup.value.id,
                            groupName = selectedGroup.value.name,
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
                else -> {}
            }
        }
    }
}

