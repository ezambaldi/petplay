package br.com.zambaldi.petplayzam.utils

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Snackbar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.zambaldi.petplayzam.R

data class SnackBarVisualsCustom(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,

    val backgroundColor: Color = Color.Blue,
    val textColor: Color = Color.White,
    val iconPosition: IconPosition = IconPosition.END,
    val snackBarPosition: SnackBarPosition = SnackBarPosition.TOP,
    @DrawableRes val drawableRes: Int
) : SnackbarVisuals {
    companion object {
        enum class SnackBarPosition {
            TOP, BOTTOM
        }

        enum class IconPosition {
            START, END
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun SnackbarHostState.showCustomSnackBar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    backgroundColor: Color = Color.Blue,
    textColor: Color = Color.White,
    iconPosition: SnackBarVisualsCustom.Companion.IconPosition = SnackBarVisualsCustom.Companion.IconPosition.END,
    snackBarPosition: SnackBarVisualsCustom.Companion.SnackBarPosition = SnackBarVisualsCustom.Companion.SnackBarPosition.TOP,
    @DrawableRes drawableRes: Int = R.drawable.ic_launcher_foreground
) {
    this.showSnackbar(
        SnackBarVisualsCustom(
            message = message,
            duration = duration,
            drawableRes = drawableRes,
            backgroundColor = backgroundColor,
            textColor = textColor,
            iconPosition = iconPosition,
            snackBarPosition = snackBarPosition
        )
    )
}

@Composable
fun SnackBarContainer(snackBarHostState: SnackbarHostState) {
    val snackBarPosition = snackBarHostState.currentSnackbarData?.visuals?.let {
        it as? SnackBarVisualsCustom
    }?.snackBarPosition ?: SnackBarVisualsCustom.Companion.SnackBarPosition.TOP

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = if (snackBarPosition == SnackBarVisualsCustom.Companion.SnackBarPosition.TOP) Alignment.TopCenter else Alignment.BottomCenter
    ) {
        SnackbarHost(hostState = snackBarHostState) {
            val customVisuals = it.visuals as? SnackBarVisualsCustom
            customVisuals?.let { cvs ->
                CustomSnackBar(customVisuals = cvs)
            }
        }
    }
}

@Composable
fun CustomSnackBar(customVisuals: SnackBarVisualsCustom) {
    Snackbar(
        backgroundColor = customVisuals.backgroundColor,
        modifier = Modifier.padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .background(color = customVisuals.backgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start)
        ) {
            val iconAtStart = customVisuals.iconPosition == SnackBarVisualsCustom.Companion.IconPosition.START
            val iconAtEnd = customVisuals.iconPosition == SnackBarVisualsCustom.Companion.IconPosition.END

            if (iconAtStart) {
                Image(
                    painter = painterResource(id = customVisuals.drawableRes),
                    contentDescription = "",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = customVisuals.message,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = customVisuals.textColor
                ),
                textAlign = TextAlign.Start,
            )
            if (iconAtEnd) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = customVisuals.drawableRes),
                    contentDescription = "",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        }

    }
}