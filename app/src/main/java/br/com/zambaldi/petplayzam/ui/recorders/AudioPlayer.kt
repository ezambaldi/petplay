package br.com.zambaldi.petplayzam.ui.recorders

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop(file: File)


}