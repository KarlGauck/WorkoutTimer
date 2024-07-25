package com.example.demo.app.io

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.exercisemodel.ExerciseSet
import com.example.demo.app.exercisemodel.ScheduleElement
import com.example.demo.m
import com.google.gson.*
import tornadofx.getProperty
import java.io.File
import java.lang.reflect.Type

object DataLoader {

    fun getDefaultExercise() = Exercise.exerciseFromList(arrayOf(
        Exercise("Warmup", 5),
        Exercise("Handstand", 5.m),
        ExerciseSet(2, arrayOf(
            Exercise("Handstandliegestützen", 1.m),
            Exercise("Klimmzüge", 1.m),
            Exercise("Liegestützen", 1.m),
            Exercise("L-Sit Boden", 1.m),
            Exercise("Superman Rücken", 1.m),
            Exercise("Superman Bauch", 1.m),
            Exercise("Bauch Seite", 1.m),
            Exercise("Zugseil", 1.m)
        )),
        Exercise("Leg workout", 10.m)
    ))

    private val gson = GsonBuilder().registerTypeAdapter(ScheduleElement::class.java, ScheduleElementSerializer()).setPrettyPrinting().create()

    fun workoutList(): List<String> = File("data/")
            .listFiles()
            ?.filter { ".json" in it.name }
            ?.map { it.name.replace(".json", "").replace("data/", "") }
            ?.toList()
        ?: emptyList()

    fun getWorkout(name: String): Exercise
    {
        return gson.fromJson(File("data/$name.json").readText(), Exercise::class.java)
    }

    fun storeWorkout(name: String, exercise: Exercise)
    {
        File("data/$name.json").writeText(gson.toJson(exercise))
    }

}

private class ScheduleElementSerializer: JsonDeserializer<ScheduleElement>
{

    override fun deserialize(src: JsonElement?, type: Type?, context: JsonDeserializationContext?): ScheduleElement? {
        val element = src?.asJsonObject
        return if (element?.has("repetitions") == true)
            context?.deserialize(element, ExerciseSet::class.java)
        else context?.deserialize(element, Exercise::class.java)
    }

}