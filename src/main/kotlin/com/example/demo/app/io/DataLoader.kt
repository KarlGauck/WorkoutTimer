package com.example.demo.app.io

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.exercisemodel.ExerciseSet
import com.example.demo.app.exercisemodel.ScheduleElement
import com.example.demo.m
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.io.File
import java.lang.reflect.Type

object DataLoader {

    val dataPath = "data/"
    val customFilePostfix = "schedule" // used with . (.schedule)

    fun getDefaultExercise() = ScheduleElement.elementFromList(arrayOf(
        Exercise("Warmup", 5.m),
        Exercise("Handstand", 5.m, "image.png"),
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

    fun workoutList(): List<String> = File(dataPath)
            .listFiles()
            ?.filter { ".json" in it.name || ".$customFilePostfix" in it.name }
            ?.map { it.name
                .replace(".json", "")
                .replace(".$customFilePostfix", "")
                .replace(dataPath, "")
            }
            ?.toList()
        ?: emptyList()

    fun getWorkout(name: String): ScheduleElement
    {
        if (File(dataPath).listFiles()?.map { it.name }?.contains("$name.json") == true)
            return gson.fromJson(File("$dataPath$name.json").readText(), ScheduleElement::class.java)
        return ExerciseSet(1, parseWorkout(File("$dataPath$name.$customFilePostfix").readLines()).toTypedArray())
    }

    fun storeWorkout(name: String, exercise: Exercise) =
        File("data/$name.json").writeText(gson.toJson(exercise))

    fun parseWorkout(lines: List<String>): List<ScheduleElement>
    {
        val elements = mutableListOf<ScheduleElement>()

        var name: String? = null
        var description: String? = null
        var imageSource: String? = null
        var duration: Int = 0

        fun addElement()
        {
            if (name == null)
                return
            val element = Exercise(name!!, description, duration, imageSource)
            elements.add(element)
            name = null
            description = null
            imageSource = null
            duration = 0
        }

        var lineIndex = 0
        while (lineIndex < lines.size) {
            val line = lines[lineIndex]
            lineIndex ++
            when
            {
                Regex("^ *# *[^#]+ *$") in line -> {
                    addElement()
                    name = line.replace(Regex("^ *#"), "")
                        .replace(Regex(" *$"), "")
                }
                Regex("^ *description: *.+ *$") in line -> {
                    description = line.replace(Regex("^ *description: *"), "")
                        .replace(Regex(" *$"), "")
                }
                Regex("^ *image: *.+ *$") in line -> {
                    imageSource = line.replace(Regex("^ *image: *"), "")
                        .replace(Regex(" *$"), "")
                }
                Regex("^ *duration: *.+ *$") in line -> {
                    duration = line.replace(Regex("^ *duration: *"), "")
                        .replace(Regex(" *$"), "").toInt()
                }
                Regex("^ *## *repeat *[0-9]+ *\\{") in line -> {
                    addElement()
                    val repetitions = line.replace(Regex("^ *## *repeat *"), "")
                        .replace(Regex(" *\\{"), "").toInt()
                    val endIndex = lines.withIndex()
                        .indexOfFirst { (index, it) -> index > lineIndex && Regex("^ *} *$") in it }
                    elements.add(ExerciseSet(repetitions, parseWorkout(lines.subList(lineIndex, endIndex)).toTypedArray()))
                    lineIndex = endIndex + 1
                }
            }
        }
        addElement()
        return elements
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