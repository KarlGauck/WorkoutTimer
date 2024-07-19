package com.example.demo.app.io

import com.example.demo.app.exercisemodel.Exercise
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

object DataLoader {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun workoutList(): List<String> = File("./")
            .listFiles()
            ?.filter { ".wod" in it.name }
            ?.map { it.name.replace(".wod", "") }
            ?.toList()
        ?: emptyList()

    fun getWorkout(name: String): Exercise
    {
        return gson.fromJson(File("data/$name.json").readText(), Exercise::class.java)
    }

}