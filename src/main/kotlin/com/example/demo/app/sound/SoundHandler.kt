package com.example.demo.app.sound

import java.io.File
import javax.sound.sampled.AudioSystem

object SoundHandler {

    val FINISHED = "data/dadum.wav"

    fun play()
    {
        val clip = AudioSystem.getClip()
        val stream = AudioSystem.getAudioInputStream(File(FINISHED))
        clip.open(stream)
        clip.loop(0)
        clip.start()
    }

}