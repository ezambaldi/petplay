package br.com.zambaldi.petplay.ui.groups

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.ui.ImagePlay
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioPlayer
import com.example.myapplicationtest.utils.bodyLarge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListAudiosBottomSheet(
    groupId: Int,
    groupName: String,
    coroutineScope: CoroutineScope,
    onNegativeButtonOrCloseClick: () -> Unit,
    audios: MutableList<Audio>,
    audioGroup: MutableList<AudiosGroup>,
    deleteAudioGroup: (id: Int) -> Unit,
    addAudioGroup: (AudiosGroup) -> Unit,
    callFetch: () -> Unit,
    applicationContext: android.content.Context,
) {

//    val onClose: () -> Unit = {
//        coroutineScope.launch {
//            sheetState.hide()
//        }.invokeOnCompletion {
//            if (!sheetState.isVisible) {
//                onNegativeButtonOrCloseClick()
//            }
//        }
//    }

    val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    BasicAlertDialog(
        onDismissRequest = { onNegativeButtonOrCloseClick() },
        modifier = Modifier
            .wrapContentHeight()
            .padding(28.dp)
            .fillMaxWidth(),
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFC6CDE0)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                        .padding(16.dp)
                ) {

                    val checkIcon = remember { mutableStateOf(R.drawable.ic_success) }

                    IconButton(
                        onClick = { onNegativeButtonOrCloseClick() },
                        modifier = Modifier
                            .align(Alignment.End)
                            .semantics { contentDescription = "fechar" }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Fechar",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Select the audios to associate with the group\n$groupName",
                        style = bodyLarge,
                        color = colorResource(id = R.color.md_theme_dark_onTertiary),
                        modifier = Modifier
                            .padding(start = 4.dp)
                    )
                    Spacer(Modifier.height(12.dp))


                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            Column {
                                audios.forEach { audio ->

                                    val isCheck = audioGroup.filter {
                                        it.idAudio == audio.id
                                    }
                                    checkIcon.value =
                                        if (isCheck.isNotEmpty()) R.drawable.ic_check else R.drawable.ic_add_gray

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {

                                        ImagePlay(
                                            audio = audio,
                                            player = player,
                                        )

                                        Text(
                                            text = audio.name,
                                            style = bodyLarge,
                                            color = colorResource(id = R.color.md_theme_dark_onTertiary),
                                            modifier = Modifier
                                                .padding(start = 4.dp)
                                        )
                                        Spacer(Modifier.weight(1f))

                                        Image(
                                            painter = painterResource(id = checkIcon.value),
                                            contentDescription = stringResource(id = R.string.touch_for_select),
                                            modifier = Modifier
                                                .clickable {
                                                    val isAudio =
                                                        audioGroup.filter { it.idAudio == audio.id }
                                                            .let {
                                                                if (it.isNotEmpty()) it[0].id else null
                                                            }

                                                    if (isAudio != null) {
                                                        deleteAudioGroup(
                                                            isAudio
                                                        )
                                                    } else {
                                                        addAudioGroup(
                                                            AudiosGroup(
                                                                idGroup = groupId,
                                                                idAudio = audio.id,
                                                            )
                                                        )
                                                    }
                                                    callFetch()
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}