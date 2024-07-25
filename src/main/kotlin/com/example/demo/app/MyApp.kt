package com.example.demo.app

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.io.DataLoader
import com.example.demo.app.sound.SoundHandler
import com.example.demo.view.MainView
import javafx.animation.AnimationTimer
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)
{
    private val view: MainView by inject()
    private var initialized = false

    private var currentExercise: Exercise? = DataLoader.getDefaultExercise()

    private var time = currentExercise?.duration ?: 0

    var paused: Boolean = true
        set(value)
        {
            field = value
            if (field)
                timer.stop()
            else
                timer.start()
        }

    private val timer = object : AnimationTimer() {
        var lastTime = System.currentTimeMillis()
        override fun handle(now: Long) {
            val time = System.currentTimeMillis()
            if (time - lastTime < 1000)
                return
            lastTime = time

            initialize()
            step()
        }
    }

    fun initialize()
    {
        if (initialized)
            return
        initialized = true
        view.exerciseDisplay.value = currentExercise!!.name
    }

    fun step()
    {
        time --
        view.time = time

        if (time > 0)
            return
        if (currentExercise == null)
            return

        currentExercise = currentExercise!!.next?.reduceToElement()
        if (currentExercise == null)
            return

        SoundHandler.play()
        time = currentExercise!!.duration
        resetState()
        paused = true
    }

    fun loadWorkout(workout: String)
    {
        paused = true
        currentExercise = DataLoader.getWorkout(workout)
        resetState()
    }

    fun loadDefaultWorkout()
    {
        paused = true
        currentExercise = DataLoader.getDefaultExercise()
        resetState()
    }

    private fun resetState()
    {
        time = currentExercise?.duration ?: 0
        view.time = time
        view.exerciseDisplay.value = currentExercise!!.name
    }

}