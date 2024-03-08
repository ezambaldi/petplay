package br.com.zambaldi.petplay.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.zambaldi.petplay.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListAudiosBottomSheet(
    groupId: Int,
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    onNegativeButtonOrCloseClick: () -> Unit
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

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}