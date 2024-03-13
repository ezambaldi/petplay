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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.models.AudiosGroup
import br.com.zambaldi.petplay.models.Group
import br.com.zambaldi.petplay.ui.AlertDialogWithBtn
import br.com.zambaldi.petplay.ui.ScreenEmpty
import br.com.zambaldi.petplay.ui.ScreenLoading
import br.com.zambaldi.petplay.ui.TopMessage
import br.com.zambaldi.petplay.ui.audios.AudioScreenSuccess
import br.com.zambaldi.petplay.ui.audios.AudioState
import br.com.zambaldi.petplay.utils.SnackBarContainer
import br.com.zambaldi.petplay.utils.TopMessageState
import com.example.myapplicationtest.utils.bodyLarge
import com.example.myapplicationtest.utils.bodyLargeBold
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListAudiosBottomSheet(
    groupId: Int,
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    onNegativeButtonOrCloseClick: () -> Unit,
    audios: MutableList<Audio>,
    audioGroup: MutableList<AudiosGroup>,
    deleteAudioGroup: (id: Int) -> Unit,
    addAudioGroup: (AudiosGroup) -> Unit,
    callFetch: () -> Unit,
) {
    val onClose: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onNegativeButtonOrCloseClick()
            }
        }
    }

    ModalBottomSheet(
        containerColor = Color.White,
        onDismissRequest = { onClose() },
        sheetState = sheetState,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(16.dp)
        ) {

            val checkIcon = remember { mutableStateOf(R.drawable.ic_success) }

            IconButton(
                onClick = { onClose() },
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
                text = "Select the audios to associate with the group",
                style = bodyLarge,
                color = colorResource(id = R.color.md_theme_dark_onTertiary),
                modifier = Modifier
                    .padding(start = 4.dp)
            )
            Spacer(Modifier.height(8.dp))


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
                            checkIcon.value = if(isCheck.isNotEmpty()) R.drawable.ic_check else R.drawable.ic_add_gray

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {

                                Image(
                                    painter = painterResource(id = R.drawable.ic_play),
                                    contentDescription = stringResource(id = R.string.touch_for_play),
                                    modifier = Modifier
                                        .clickable {
                                            //TODO: play audio
                                        }
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
                                            val isAudio = audioGroup.filter { it.idAudio == audio.id }.let{
                                                if(it.isNotEmpty()) it[0].id else null
                                            }

                                            if(isAudio != null) {
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