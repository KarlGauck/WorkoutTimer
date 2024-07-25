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
    private var exerciseStart: Exercise? = DataLoader.getDefaultExercise()
    private var exerciseCounter = 0

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

        SoundHandler.play()
        next()
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

    fun next()
    {
        if (currentExercise?.next == null)
            return
        currentExercise = currentExercise!!.next?.reduceToElement()
        exerciseCounter++
        if (currentExercise == null)
            return
        time = currentExercise!!.duration
        resetState()
        paused = true
    }

    fun backToStart()
    {
        currentExercise = exerciseStart
        resetState()
        paused = true
    }

    fun back()
    {
        goto(exerciseCounter-1)
    }

    fun goto(index: Int)
    {
        if (index == 0)
        {
            resetState()
            paused = true
            return
        }

        var iteratorExercise = exerciseStart
        for (i in 0 until index)
        {
            if (iteratorExercise == null)
                return
            iteratorExercise = iteratorExercise.next?.reduceToElement()
        }
        currentExercise = iteratorExercise
        exerciseCounter = index
        resetState()
        paused = true
    }

    private fun resetState()
    {
        time = currentExercise?.duration ?: 0
        view.time = time
        view.exerciseDisplay.value = currentExercise!!.name
    }

}