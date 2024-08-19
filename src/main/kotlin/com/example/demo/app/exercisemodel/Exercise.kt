package com.example.demo.app.exercisemodel

class Exercise (
    val name: String,
    val description: String?,
    override val duration: Int,
    val imageSource: String?
): ScheduleElement() {
    override fun reduceToExercise(): Exercise {
        return this
    }

    constructor(name: String, duration: Int): this(name, null, duration, null)
    constructor(name: String, description: String?, duration: Int): this(name, description, duration, null)
    constructor(name: String, duration: Int, imageSource: String?): this(name, null, duration, imageSource)

    companion object {



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
