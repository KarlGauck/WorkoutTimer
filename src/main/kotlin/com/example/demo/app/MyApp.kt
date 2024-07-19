package com.example.demo.app

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.exercisemodel.ExerciseSet
import com.example.demo.app.io.DataLoader
import com.example.demo.app.sound.SoundHandler
import com.example.demo.m
import com.example.demo.view.MainView
import com.google.gson.GsonBuilder
import javafx.animation.AnimationTimer
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)
{
    val view: MainView by inject()
    var initialized = false

    private var currentExercise: Exercise? = Exercise.exerciseFromList(arrayOf(
        Exercise("Warmup", 5.m),
        Exercise("Handstand", 5.m),
        ExerciseSet(2, arrayOf(
            Exercise("Handstandliegestützen", 1.m),
            Exercise("Klimmzüge", 1.m),
            Exercise("Liegestützen", 1.m),
            Exercise("L-Sit Boden", 1.m),
            Exercise("Superman Rücken", 1.m),
            Exercise("Superman Bauch", 1.m),
            Exercise("Bauch Seite", 1.m),
            Exercise("Zugseil", 1.m)
        )),
        Exercise("Leg workout", 10.m)
    ))

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

            if (!initialized)
            {
                initialized = true
                view.exerciseDisplay.value = currentExercise!!.name
            }

            step()
        }
    }

    fun step()
    {
        time --
        view.time = time

        if (time == 0)
        {
            SoundHandler.play()
            paused = true
        }

        if (time > 0)
            return
        if (currentExercise == null)
            return

        currentExercise = currentExercise!!.next?.reduceToElement()
        if (currentExercise == null)
            return

        time = currentExercise!!.duration
        view.time = time
        view.exerciseDisplay.value = currentExercise!!.name
    }

}