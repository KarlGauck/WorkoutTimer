package com.example.demo.app

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.view.MainView
import javafx.animation.AnimationTimer
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)
{
    val view: MainView by inject()

    val exercises = arrayOf(
        Exercise("Warmup", 5*60),
        Exercise("Handstand", 5*60),
    )

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
            view.time ++
        }
    }

    init {
        timer.start()
    }

}