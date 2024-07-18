package com.example.demo.app.exercisemodel

abstract class ScheduleElement {

    abstract fun reduceToElement(next: ScheduleElement?): Exercise?

    var next: ScheduleElement? = null

}