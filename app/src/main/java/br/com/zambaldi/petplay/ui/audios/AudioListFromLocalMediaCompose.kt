package br.com.zambaldi.petplay.ui.audios

import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import br.com.zambaldi.petplay.R
import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.ui.ImagePlay
import br.com.zambaldi.petplay.ui.recorders.AndroidAudioPlayer

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AudioListFromMediaScreen(
    addAudio: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val applicationContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    val audioList = ArrayList<Audio>()
    val strings = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DATA // This will get the file path
    )

    val cursor: Cursor? = applicationContext.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        strings,
        null,
        null,
        null
    )

    cursor?.use {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val path = cursor.getString(dataColumn)

            audioList.add(
                Audio(
                    id = id.toInt(),
                    name = name,
                    path = path

                )
            )
        }
    }

    BasicAlertDialog(
        onDismissRequest = { onClose() },
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
                        onClick = { onClose() },
                        modifier = Modifier
                            .align(Alignment.End)
                            .semantics { contentDescription = "Close" }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        audioList.forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {

                                ImagePlay(
                                    audio = it,
                                    player = player,
                                )

                                Text(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .padding(start = 8.dp),
                                    text = it.name,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        lineHeight = 40.sp,
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF1B2852),
                                    )
                                )
                                IconButton(
                                    onClick = {
                                        addAudio(it.path)
                                        onClose()
                                              },
                                    modifier = Modifier
                                        .semantics { contentDescription = "Select audio" }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "Select Audio",
                                        tint = Color.Black,
                                        modifier = Modifier
                                            .padding(8.dp)
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

