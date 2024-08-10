package com.example.demo.app.exercisemodel

abstract class ScheduleElement {

    var next: ScheduleElement? = null

    abstract val duration: Int
    abstract fun reduceToElement(): Exercise?

    fun getTotalDuration(): Int = duration + (next?.getTotalDuration() ?: 0)

}