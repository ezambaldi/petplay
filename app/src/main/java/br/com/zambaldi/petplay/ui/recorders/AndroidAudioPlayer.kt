package br.com.zambaldi.petplay.ui.recorders

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.constraintlayout.compose.DesignElements.map
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null
    private var players: ArrayList<Map<String, MediaPlayer>> = ArrayList()

    override fun playFile(file: File) {
        try {
            MediaPlayer().apply {
                setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())

                reset()
                try {
                    setDataSource(file.absolutePath)
                } catch (e: Exception) {
                    setDataSource(context, file.toUri())
                }
                prepare()
                start()
                players.add(mapOf(file.absolutePath to this))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop(file: File) {
        players.forEach { it ->
            if(it.containsKey(file.absolutePath)) {
                it.run {
                    this[file.absolutePath]?.stop()
                    this[file.absolutePath]?.release()
                    deletePlayer(this)
                    return
                }
            }
        }
    }

    private fun deletePlayer(player: Map<String, MediaPlayer>) {
        players.remove(player)
    }

}