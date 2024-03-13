package br.com.zambaldi.petplay.ui.recorders

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}