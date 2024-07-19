package com.example.demo.app.exercisemodel

class ExerciseSet(val repetitions: Int, val elements: Array<ScheduleElement>): ScheduleElement() {

    override fun reduceToElement(): Exercise? {
        val list = elements.toMutableList()
        val lastElement = ExerciseSet(repetitions-1, elements)
        lastElement.next = this.next

        if (lastElement.repetitions > 0)
            list.add(lastElement)
        else
            list.last().next = this.next

        val result = Exercise.exerciseFromList(list.toTypedArray())
        return result
    }

}