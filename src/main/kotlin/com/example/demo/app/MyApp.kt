package com.example.demo.app

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.exercisemodel.ScheduleElement
import com.example.demo.app.io.DataLoader
import com.example.demo.app.sound.SoundHandler
import com.example.demo.view.MainView
import javafx.animation.AnimationTimer
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)
{
    private val view: MainView by inject()
    private var initialized = false

    private var currentExercise: Exercise? = DataLoader.getDefaultExercise()?.reduceToExercise()
    private var exerciseStart: ScheduleElement? = DataLoader.getDefaultExercise()
    private var exerciseCounter = 0

    private var time = currentExercise?.duration ?: 0
        set(value)
        {
            field = value
            view.time = time
        }

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
        val list = listOf(
            "# Name",
            "description: blabla",
            "duration: 10",
            "## repeat 3 {",
            "# Name 2",
            "duration: 3",
            "}"
        )

        DataLoader.parseWorkout(list).forEach(::println)

        if (initialized)
            return
        initialized = true
        view.exerciseDisplay.value = currentExercise!!.name
    }

    fun step()
    {
        if (time > 0)
        {
            time --
            if (time == 0)
            {
                SoundHandler.play()
                if (currentExercise!!.next == null)
                    view.congratulation()
            }
            return
        }
        if (currentExercise == null)
            return

        next()
    }

    fun loadWorkout(workout: String)
    {
        paused = true
        currentExercise = DataLoader.getWorkout(workout).reduceToExercise()
        setOptionalProperties()
        exerciseStart = currentExercise
        resetState()
    }

    fun loadDefaultWorkout()
    {
        paused = true
        currentExercise = DataLoader.getDefaultExercise()?.reduceToExercise()
        setOptionalProperties()
        exerciseStart = currentExercise
        resetState()
    }

    fun next()
    {
        if (currentExercise?.next == null)
            return
        currentExercise = currentExercise!!.next?.reduceToExercise()
        if (currentExercise == null)
            return
        exerciseCounter++

        setOptionalProperties()

        time = currentExercise!!.duration
        resetState()
        paused = true
    }

    fun backToStart()
    {
        currentExercise = exerciseStart?.reduceToExercise()
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
                iteratorExercise = iteratorExercise.next?.reduceToExercise()
        }
        currentExercise = iteratorExercise?.reduceToExercise()

        setOptionalProperties()

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

    private fun setOptionalProperties()
    {
        if (currentExercise!!.imageSource == null)
            view.hideImageView()
        else {
            view.imageSource.value = "file:${DataLoader.dataPath}${currentExercise!!.imageSource}"
            view.showImageView()
        }

        if (currentExercise!!.description == null)
            view.hideDescriptionLabel()
        else {
            view.descriptionString.value = currentExercise!!.description
            view.showDescriptionLabel()
        }
    }

}