package br.com.zambaldi.petplay.ui.play

import android.os.Build
import android.os.SystemClock.sleep
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.zambaldi.petplay.MainActivity
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.models.InteractionType
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioPlayer
import br.com.zambaldi.petplay.utils.TopMessageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PlayListScreen(
    state: PlayState,
    mainActivity: MainActivity,
    topMessageState: TopMessageState,
    callFetch: () -> Unit,
) {
    val applicationContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val colorPlay = remember { mutableIntStateOf(android.R.color.holo_green_light) }
    val colorStop = remember { mutableIntStateOf(android.R.color.darker_gray) }
    val groupList = remember { mutableStateOf<List<Group>>(emptyList()) }
    val audioList = remember { mutableStateOf<List<Audio>>(emptyList()) }
    val startPlayList = remember { mutableStateOf(false) }

    if(startPlayList.value) {
        when (state) {
            is PlayState.Loaded -> {
                audioList.value = state.audios
                groupList.value =  state.data.filter {
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val dateStartString = it.dateStart+" "+it.timeStart
                    val dateFinishString = it.dateFinish + " " + it.timeFinish
                    val dateStart = LocalDateTime.parse(dateStartString, formatter)
                    val dateFinish = LocalDateTime.parse(dateFinishString, formatter)

                    LocalDateTime.now() in dateStart..dateFinish
                }

            }
            is PlayState.Default -> {}
            is PlayState.Error -> {}
            is PlayState.Loading -> {}
        }

        val audioPlayer = AndroidAudioPlayer(applicationContext)

        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.IO) {

                while (startPlayList.value) {
                    val audios = ArrayList<AudiosGroup>()
                    var currentGroupIndex = -1
                    var currentAudioIndex = -1
                    var totalGroups = groupList.value.size

                    while (totalGroups > 0) {
                        if(!startPlayList.value) { break }
                        totalGroups--
                        currentGroupIndex++
                        audios.clear()
                        currentAudioIndex = -1
                        audios.addAll(groupList.value[currentGroupIndex].audios)
                        var totalAudios = audios.size
                        while (totalAudios > 0) {
                            if(!startPlayList.value) { break }
                            totalAudios--
                            currentAudioIndex++
                            val audio = audioList.value.find { it.id == audios[currentAudioIndex].idAudio }
                            val audioFile = File("", audio?.path ?: "")
                            audioPlayer.playFile(audioFile)
                            var isFinish = false
                            while (!isFinish) {
                                if(!startPlayList.value) { break }

                                if(groupList.value[currentGroupIndex].interactionType == InteractionType.SHAKE) {
                                    if(mainActivity.isShakeDetected) {
                                        isFinish = audioPlayer.playerInfo == true
                                        mainActivity.isShakeDetected = false
                                    }
                                } else {
                                    sleep(groupList.value[currentGroupIndex].intervalSecond.toLong() * 1000L)
                                    isFinish = audioPlayer.playerInfo == true
                                }
                            }
                        }
                    }
                }
            }
        }
    }








    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()

        ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(start = 32.dp, top = 32.dp)
                    .clickable {
                        startPlayList.value = true
                        colorPlay.intValue = android.R.color.darker_gray
                        colorStop.intValue = android.R.color.holo_red_light
                    },
                painter = painterResource(id = R.drawable.ic_play_draw),
                tint = colorResource(colorPlay.intValue),
                contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(end = 32.dp, top = 32.dp)
                    .clickable {
                        startPlayList.value = false
                        colorPlay.intValue = android.R.color.holo_green_light
                        colorStop.intValue = android.R.color.darker_gray

                    },
                painter = painterResource(id = R.drawable.ic_pause_draw),
                tint = colorResource(colorStop.intValue),
                contentDescription = ""
            )
        }


        Image(
            modifier = Modifier
                .padding(bottom = 330.dp),
            painter = painterResource(id = R.drawable.ic_corda),
            contentDescription = stringResource(id = R.string.touch_for_remove),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .width(80.dp)
                .padding(top = 2.dp),

            ) {
            Image(
                modifier = Modifier
                    .width(80.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.ic_celular),
                contentDescription = stringResource(id = R.string.touch_for_remove),
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cat),
            contentDescription = stringResource(id = R.string.touch_for_remove),
        )
        Image(
            painter = painterResource(id = R.drawable.ic_dog),
            contentDescription = stringResource(id = R.string.touch_for_remove),
        )
    }
}



