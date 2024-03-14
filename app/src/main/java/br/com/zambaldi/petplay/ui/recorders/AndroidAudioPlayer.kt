package br.com.zambaldi.petplay.ui.recorders

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {

        try {
            MediaPlayer.create(context, file.toUri()).apply {
                player = this
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

}