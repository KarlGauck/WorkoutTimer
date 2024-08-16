package com.example.demo.app

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.io.DataLoader
import com.example.demo.app.sound.SoundHandler
import com.example.demo.view.EditorView
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
        println(paused)
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
        exerciseStart = currentExercise
        resetState()
    }

    fun loadDefaultWorkout()
    {
        paused = true
        currentExercise = DataLoader.getDefaultExercise()
        exerciseStart = currentExercise
        resetState()
    }

    fun next()
    {
        if (currentExercise?.next == null)
            return
        currentExercise = currentExercise!!.next?.reduceToElement()
        if (currentExercise == null)
            return
        exerciseCounter++
        if (currentExercise!!.imageSource == null)
            view.hideImageView()
        else {
            view.imageSource.value = "file:data/${currentExercise!!.imageSource}"
            view.showImageView()
        }
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

    private fun goto(index: Int)
    {
        var iteratorExercise = exerciseStart
        for (i in 0 .. index)
        {
            if (iteratorExercise == null)
                return
            if (i != 0)
                iteratorExercise = iteratorExercise.next?.reduceToElement()
        }
        currentExercise = iteratorExercise
        if (currentExercise!!.imageSource == null)
            view.hideImageView()
        else {
            view.imageSource.value = "file:data/${currentExercise!!.imageSource}"
            view.showImageView()
        }
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