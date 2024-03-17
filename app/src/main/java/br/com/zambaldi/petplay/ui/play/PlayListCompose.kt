package br.com.zambaldi.petplay.ui.play

import android.annotation.SuppressLint
import android.os.Build
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
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.ui.groups.GroupState
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioPlayer
import br.com.zambaldi.petplay.utils.TopMessageState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PlayListScreen(
    state: PlayState,
    topMessageState: TopMessageState,
    callFetch: () -> Unit,
) {
    val applicationContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val errorSnackBarHostState = remember { SnackbarHostState() }
    val colorPlay = remember { mutableIntStateOf(android.R.color.holo_green_light) }
    val colorStop = remember { mutableIntStateOf(android.R.color.darker_gray) }

//    if(startRecord.value) {
//        startRecord.value = false
//        LaunchedEffect(Unit) {
//            scope.launch {
//                val randomNumber = System.currentTimeMillis().toString()
//                File(applicationContext.cacheDir,"audio_${randomNumber}.mp3").also {
//                    recorder.start(it)
//                    audioFile.value = it
//                }
//            }
//        }
//    }






val groupsToPlay = when (state) {
        is PlayState.Loaded -> {

            state.data.map {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                val teste = LocalDateTime.parse(it.dateStart+" "+it.timeStart, formatter) >= LocalDateTime.now() &&
                        LocalDateTime.parse(it.dateFinish+" "+it.timeFinish, formatter) <= LocalDateTime.now()}

            val teste2 = ""
        }







//            state.data.filter {
//                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
//                    LocalDateTime.parse(it.dateStart+" "+it.timeStart, formatter) >= LocalDateTime.now() &&
//                            LocalDateTime.parse(it.dateFinish+" "+it.timeFinish, formatter) <= LocalDateTime.now()}
//                }
        PlayState.Default -> {}
        PlayState.Error -> {}
        PlayState.Loading -> {}
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
                    .padding(28.dp)
                    .clickable {
                        colorPlay.intValue = android.R.color.darker_gray
                        colorStop.intValue = android.R.color.holo_red_light
                    },
                painter = painterResource(id = R.drawable.ic_play),
                tint = colorResource(colorPlay.intValue),
                contentDescription = ""
            )
            Icon(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(28.dp)
                    .clickable {
                        colorPlay.intValue = android.R.color.holo_green_light
                        colorStop.intValue = android.R.color.darker_gray
                    },
                painter = painterResource(id = R.drawable.ic_stop),
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

@SuppressLint("MutableCollectionMutableState")
@Composable
fun AudioScreenSuccess(
    applicationContext: android.content.Context,
    state: GroupState.Loaded,
) {
    val openDialog = remember { mutableStateOf(false) }
    val audioName = remember { mutableStateOf("") }
    val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }


}

