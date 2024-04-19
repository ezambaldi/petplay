package br.com.zambaldi.petplayzam.ui.recorders

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}