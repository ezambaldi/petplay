package br.com.zambaldi.petplay.ui.audios

import android.media.Image
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import br.com.zambaldi.petplay.R

enum class TypeMessage(val color: Color, @DrawableRes val image: Int = R.drawable.ic_launcher_foreground) {
    SUCCESS(Color(0xFF1B5E20), R.drawable.ic_success),
    INFO(Color(0xFF0288D1), R.drawable.ic_info),
    ERROR(Color(0xFFB71C1C), R.drawable.ic_error)
}