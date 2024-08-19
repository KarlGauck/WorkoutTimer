package com.example.demo.app.exercisemodel

abstract class ScheduleElement {

    var next: ScheduleElement? = null

    abstract val duration: Int
    abstract fun reduceToExercise(): Exercise?

    fun getTotalDuration(): Int = duration + (next?.getTotalDuration() ?: 0)

    companion object
    {
        fun elementFromList(elements: Array<ScheduleElement>): ScheduleElement?
        {
            if (elements.isEmpty())
                return null
            val final = elements[0]
            var last: ScheduleElement = final
            for (element in elements.slice(1 until elements.size))
            {
                last.next = element
                last = element
            }
            return final
        }

    }

}