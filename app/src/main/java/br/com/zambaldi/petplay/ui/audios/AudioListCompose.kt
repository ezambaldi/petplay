package br.com.zambaldi.petplay.ui.audios

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.ui.AlertDialogWithBtn
import br.com.zambaldi.petplay.ui.ScreenEmpty
import br.com.zambaldi.petplay.ui.ScreenLoading
import br.com.zambaldi.petplay.ui.TopMessage
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioPlayer
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioRecorder
import br.com.zambaldi.petplay.utils.SnackBarContainer
import br.com.zambaldi.petplay.utils.TopMessageState
import com.example.myapplicationtest.utils.bodyLarge
import com.example.myapplicationtest.utils.bodyLargeBold
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.io.File
import kotlin.concurrent.timerTask
import kotlin.random.Random

@Composable
fun AudioListScreen(
    applicationContext: android.content.Context,
    state: AudioState,
    topMessageState: TopMessageState,
    callFetch: () -> Unit,
    deleteAudio: (id: Int) -> Unit,
    addAudio: (Audio) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val errorSnackBarHostState = remember { SnackbarHostState() }
    val colorIconRecord = remember {
        mutableStateOf(android.R.color.holo_green_light)
    }
    val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }
    var audioF: File? = null
    val audioFile = remember {
        mutableStateOf(audioF)
    }
    val stopEnabled = remember { mutableStateOf(false) }
    val startRecord = remember { mutableStateOf(false) }
    val stopRecord = remember { mutableStateOf(false) }

    val generatByRandom = Random(System.currentTimeMillis())
    val randomNumber= generatByRandom.nextInt(0, 10)

    if(startRecord.value) {
        startRecord.value = false
        LaunchedEffect(Unit) {
            scope.launch {
                File(applicationContext.cacheDir,"audio${randomNumber}.mp3").also {
                    recorder.start(it)
                    audioFile.value = it
                }
            }
        }
    }

    if(stopRecord.value) {
        stopRecord.value = false
        LaunchedEffect(Unit) {
            scope.launch {
                recorder.stop()
                stopEnabled.value = false
            }
        }
    }




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
                                        text = stringResource(id = R.string.title_add_audio),
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

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically

                                ) {
                                        Button(
                                            onClick = {
                                                stopEnabled.value = true
                                                startRecord.value = true
                                            },
                                            enabled = !stopEnabled.value,
                                            shape = RoundedCornerShape(50.dp),
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp)
                                                .height(60.dp)
                                        ) {
                                            Text(
                                                textAlign = TextAlign.Center,
                                                text = stringResource(id = R.string.btn_start_recording)
                                            )
                                        }

                                    if(audioFile.value != null) colorIconRecord.value = android.R.color.holo_green_light else colorIconRecord.value = android.R.color.darker_gray
                                    Icon(
                                        painterResource(id = R.drawable.ic_launcher_foreground),
                                        contentDescription = "",
                                        tint = colorResource(colorIconRecord.value),
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(30.dp)
                                    )

                                        Button(
                                            onClick = {
                                                stopRecord.value = true
                                            },
                                            enabled = stopEnabled.value,
                                            shape = RoundedCornerShape(50.dp),
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp)
                                                .height(60.dp)
                                        ) {
                                            Text(
                                                textAlign = TextAlign.Center,
                                                text = stringResource(id = R.string.btn_stop_recording)
                                            )
                                        }
                                }


                                Spacer(modifier = Modifier.height(20.dp))

                                val msgError = stringResource(id = R.string.msg_empty_field)
                                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                                    Button(
                                        onClick = {
                                            if (txtField.value.isEmpty()) {
                                                txtFieldError.value = msgError
                                                return@Button
                                            }
                                            addAudio(
                                                Audio(
                                                    name = txtField.value,
                                                    path = audioFile.value?.path?: "",
                                                )
                                            )
                                            onDismiss.value = false
                                            txtField.value = ""
                                        },
                                        enabled = audioFile.value != null,
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

                val context = LocalContext.current
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    when (state) {
                        is AudioState.Loading -> {
                            ScreenLoading()
                        }
                        is AudioState.Loaded -> {
                            if(state.data.isNotEmpty()) {
                                AudioScreenSuccess(
                                    state = state,
                                    deleteAudio = deleteAudio,
                                    applicationContext = applicationContext,
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

@Composable
fun AudioScreenSuccess(
    applicationContext: android.content.Context,
    state: AudioState.Loaded,
    deleteAudio: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val startPlay = remember { mutableStateOf(false) }
    val stopPlay = remember { mutableStateOf(false) }
    val audioToDelete = remember { mutableIntStateOf(0) }
    val audioName = remember { mutableStateOf("") }
    val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }




    val audios: List<Audio> = state.data
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
                audios.forEach { audio ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        val imagePlay = if(audio.path.isNotEmpty()) R.drawable.ic_play else R.drawable.ic_play_gray

                        if(startPlay.value) {
                            LaunchedEffect(Unit) {
                                scope.launch {
                                    if(audio.path.isNotEmpty()) {
                                        val file = File(audio.path)
                                        player.playFile(file)
                                    }
                                }
                            }
                        }

                        if(stopPlay.value) {
                            stopPlay.value = false
                            LaunchedEffect(Unit) {
                                scope.launch {
                                    player.stop()
                                }
                            }
                        }

                        Image(
                            painter = painterResource(imagePlay),
                            contentDescription = stringResource(id = R.string.touch_for_play),
                            modifier = modifier
                                .clickable {
                                    if(startPlay.value) {
                                        stopPlay.value = true
                                    } else startPlay.value = true
                                }
                        )
                        Text(
                            text = audio.name,
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
                                    audioToDelete.value = audio.id
                                    audioName.value = audio.name
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
                deleteAudio(audioToDelete.intValue)
            },
            dialogTitle = audioName.value,
            dialogText = stringResource(id = R.string.msg_remove_item),
            openDialog = openDialog,
        )
    }

}

