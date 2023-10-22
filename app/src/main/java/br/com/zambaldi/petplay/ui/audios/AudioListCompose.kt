package br.com.zambaldi.petplay.ui.audios

import android.os.Build.VERSION.SDK_INT
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.myapplicationtest.utils.bodyLarge
import com.example.myapplicationtest.utils.bodyLargeBold
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun AudioListScreen(
    state: AudioState,
    callFetch: () -> Unit,
    deleteAudio: (id: Int) -> Unit,
    addAudio: (Audio) -> Unit,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        topBar = { },
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
                                            imageVector = Icons.Default.Build,
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
                                        txtField.value = it.take(10)
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
                                            addAudio(
                                                Audio(
                                                    name = txtField.value,
                                                    path = "test",
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
                val context = LocalContext.current
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    when (state) {
                        is AudioState.Error -> {
                            MessageScreen(state.errorMessage, Color.Red)
                        }
                        is AudioState.Loading -> {
                            AudioScreenLoading()
                        }
                        is AudioState.Loaded -> {
                            if(state.data.isNotEmpty()) {
                                AudioScreenSuccess(
                                    state = state,
                                    deleteAudio = deleteAudio,
                                )
                            } else {
                                MessageScreen(stringResource(id = R.string.empty_screen), Color.Blue)
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
fun MessageScreen(
    message: String,
    colorText: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = message,
                color = colorText,
                style = bodyLargeBold,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TopMessage(
    message: String,
    typeMessage: TypeMessage,
    modifier: Modifier = Modifier,
) {
    var isColumnVisible by remember { mutableStateOf(true) }
    if (isColumnVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .background(typeMessage.color)
                .padding(2.dp)
        ) {
            Text(
                text = message,
                color = Color.White,
                style = bodyLargeBold,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
    LaunchedEffect(Unit) {
        delay(2.seconds)
        isColumnVisible = false
    }
}

@Composable
fun AudioScreenLoading(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
                    if (SDK_INT >= 28) {
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
fun AudioScreenSuccess(
    state: AudioState.Loaded,
    deleteAudio: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = stringResource(id = R.string.touch_for_play),
                            modifier = modifier
                                .clickable {
                                    //todo
                                }
                        )
                        Text(
                            text = audio.name,
                            style = bodyLarge,
                            color = colorResource(id = R.color.md_theme_light_surfaceTint),
                            modifier = modifier
                                .padding(bottom = 16.dp, start = 4.dp)
                        )
                        Spacer(Modifier.weight(1f))

                        val openDialog = remember { mutableStateOf(false) }
                        if (openDialog.value) {
                            AlertDialogWithBtn(
                                onConfirmation = {
                                    deleteAudio(audio.id)
                                },
                                dialogTitle = audio.name,
                                dialogText = stringResource(id = R.string.msg_remove_item),
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.ic_remove),
                            contentDescription = stringResource(id = R.string.touch_for_remove),
                            modifier = modifier
                                .clickable {
                                    openDialog.value = true
                                }
                        )
                    }

                }
            }
//            if(state is AudioState.Loaded) {
                var message = ""
                if(state.isShowTopMessage) {
                    message = if(state.typeMessage == TypeMessage.SUCCESS)
                        stringResource(id = R.string.top_message_success) else
                        stringResource(id = R.string.top_message_error) + " (${state.topMessage}) "
                    TopMessage(
                        message = message,
                        typeMessage = state.typeMessage
                    )
                }
//            }

        }
    }
}

@Composable
fun AlertDialogWithBtn(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            icon = {
//                Icon(icon, contentDescription = "Example Icon")
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
}

@Composable
fun AddAudio(
    onConfirmation: (Audio) -> Unit,
) {
}