package com.example.demo.app.exercisemodel

abstract class ScheduleElement {

    abstract fun reduceToElement(): Exercise?

    var next: ScheduleElement? = null

}