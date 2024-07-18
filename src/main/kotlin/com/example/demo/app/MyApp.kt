package com.example.demo.app

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.exercisemodel.ExerciseSet
import com.example.demo.view.MainView
import javafx.animation.AnimationTimer
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)
{
    val view: MainView by inject()
    var initialized = false

    private var currentExercise: Exercise? = Exercise.exerciseFromList(arrayOf(
        Exercise("Workout", 2),
        Exercise("Handstand", 2),
        ExerciseSet(4, arrayOf(
            Exercise("Klimmzüge", 2),
            Exercise("Liegestützen", 2)
        )),
        Exercise("Leg workout", 2)
    ))

    private var time = currentExercise?.duration ?: 0

    var paused: Boolean = false
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

    init {
        timer.start()
    }

    fun step()
    {
        time --
        view.time = time

        if (time > 0)
            return
        if (currentExercise == null)
            return

        val next = currentExercise!!.next?.next
        currentExercise = currentExercise!!.next?.reduceToElement(next)
        if (currentExercise == null)
            return
        time = currentExercise!!.duration
        view.time = time
        view.exerciseDisplay.value = currentExercise!!.name
    }

}