package br.com.zambaldi.petplay.ui

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioPlayer
import br.com.zambaldi.petplay.utils.SnackBarVisualsCustom
import br.com.zambaldi.petplay.utils.TypeMessage
import br.com.zambaldi.petplay.utils.showCustomSnackBar
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.myapplicationtest.utils.bodyLarge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Thread.sleep

@Composable
fun ScreenEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.empty_screen),
        )
        Text(
            text = stringResource(id = R.string.empty_screen),
            style = bodyLarge,
            color = colorResource(id = R.color.md_theme_dark_onTertiary),
            modifier = modifier
                .padding(start = 4.dp)
        )
    }
}

@Composable
fun AlertDialogWithBtn(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    openDialog: MutableState<Boolean> = mutableStateOf(false),
) {
    AlertDialog(
        icon = {
            Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "default")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            openDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                    openDialog.value = false
                }
            ) {
                Text(stringResource(id = R.string.btn_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }
            ) {
                Text(stringResource(id = R.string.btn_no))
            }
        }
    )
}

@Composable
fun ScreenLoading(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically)
        ) {
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.dog_cat).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(64.dp)
                ,
            )
        }
    }
}

@Composable
fun TopMessage(
    message: String,
    typeMessage: TypeMessage,
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    errorSnackBarHostState: SnackbarHostState,
) {
    LaunchedEffect(Unit) {
        scope.launch {
            errorSnackBarHostState.showCustomSnackBar(
                message = message,
                duration = SnackbarDuration.Short,
                backgroundColor = typeMessage.color,
                textColor = Color.White,
                drawableRes = typeMessage.image,
                snackBarPosition = SnackBarVisualsCustom.Companion.SnackBarPosition.TOP
            )
        }
    }
}

@Composable
fun ImagePlay(
    audio: Audio,
    player: AndroidAudioPlayer,
) {
    val scope = rememberCoroutineScope()
    val startPlay = remember { mutableStateOf(false) }
    val stopPlay = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }
    val audioPath = remember { mutableStateOf("") }

    var imagePlay = if(audio.path.isNotEmpty() && !isPlaying.value) R.drawable.ic_play else R.drawable.ic_play_gray
    if(isPlaying.value && audioPath.value == audio.path) imagePlay = R.drawable.ic_stop

    if(startPlay.value) {
        startPlay.value = false
        isPlaying.value = true
        LaunchedEffect(Unit) {
            scope.launch {
                if(audio.path.isNotEmpty()) {
                    val file = File(audioPath.value)
                    player.playFile(file)
                }
            }
        }
    }


    if(stopPlay.value) {
        stopPlay.value = false
        isPlaying.value = false
        if(audioPath.value == audio.path) imagePlay = R.drawable.ic_play
        LaunchedEffect(Unit) {
            scope.launch {
                val file = File(audioPath.value)
                player.stop(file)
            }
        }
    }

    Image(
        painter = painterResource(imagePlay),
        contentDescription = stringResource(id = R.string.touch_for_play),
        modifier = Modifier
            .clickable {
                audioPath.value = audio.path
                if(isPlaying.value) {
                    stopPlay.value = true
                } else {
                    startPlay.value = true
                }
            }
    )


}
