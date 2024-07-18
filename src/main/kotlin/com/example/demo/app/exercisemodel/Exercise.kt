package com.example.demo.app.exercisemodel

class Exercise (
    val name: String,
    val duration: Int
): ScheduleElement() {
    override fun reduceToElement(next: ScheduleElement?): Exercise {
        return this
    }

    companion object {

        fun exerciseFromList(elements: Array<ScheduleElement>): Exercise?
        {
            if (elements.isEmpty())
                return null
            val final = elements[0].reduceToElement(if (elements.size >= 2) elements[1] else null)!!
            var last: ScheduleElement = final
            for (element in elements.slice(1 until elements.size))
            {
                last.next = element
                last = element
            }
            return final
        }

        fun printExercise(exercise: Exercise)
        {
            var current: ScheduleElement? = exercise
            while (current != null)
            {
                if (current is Exercise)
                    println(current.name)
                else if (current is ExerciseSet)
                    println("scheduled: ${current.repetitions}")
                current = current.next
            }
        }

    }

}
