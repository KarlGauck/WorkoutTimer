package com.example.demo.app.exercisemodel

class ExerciseSet(val repetitions: Int, val elements: Array<ScheduleElement>): ScheduleElement() {

    override val duration: Int
        get() = elements.fold(0) { value, it ->
            value + it.getTotalDuration()
        } * repetitions

    override fun reduceToExercise(): Exercise? {
        val list = elements.toMutableList()
        val lastElement = ExerciseSet(repetitions-1, elements)
        lastElement.next = this.next

        if (lastElement.repetitions > 0)
            list.add(lastElement)
        else
            list.last().next = this.next

        val result = ScheduleElement.elementFromList(list.toTypedArray())
        return if (result is Exercise) result else result?.reduceToExercise()
    }

}