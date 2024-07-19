package com.example.demo.view

import com.example.demo.app.MyApp
import com.example.demo.app.Styles
import com.example.demo.app.io.DataLoader
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Priority
import javafx.scene.text.TextAlignment
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    private val timeMinutes = SimpleIntegerProperty(0)
    private val timeSeconds = SimpleIntegerProperty(0)
    val exerciseDisplay = SimpleStringProperty("something goes here")

    var time = 0
        set(value)
        {
            timeMinutes.value = value / 60
            timeSeconds.value = value % 60
            field = value
        }

    override val root = vbox {
        val myApp: MyApp = app as MyApp

        alignment = Pos.CENTER
        paddingAll = 5.0

        addEventFilter(KeyEvent.KEY_PRESSED) {
            println("test")
            println(it.character)
            println(it.text)
            println(it.code)
            if (it.text != " ")
                return@addEventFilter
            myApp.paused = !myApp.paused
        }

        label("YOU CAN DO IT!!!") {
            addClass(Styles.exerciseLabel)
            hgrow = Priority.ALWAYS
        }
        label(exerciseDisplay) {
            addClass(Styles.heading)
            vgrow = Priority.ALWAYS
            useMaxHeight = true
        }
        label {
            fun changeText() {
                text = "${"%02d".format(timeMinutes.value)} : ${"%02d".format(timeSeconds.value)}"
            }
            changeText()
            timeMinutes.onChange { changeText() }
            timeSeconds.onChange { changeText() }

            addClass(Styles.timeDisplay)
            alignment = Pos.BOTTOM_CENTER
        }
        hbox {
            prefWidth = Double.MAX_VALUE
            alignment = Pos.CENTER
            spacing = 15.0
            button("<<") {
                addClass(Styles.controlButton)
            }
            button("<") {
                addClass(Styles.controlButton)
            }
            button("=") {
                addClass(Styles.controlButton)
                setOnAction {
                    myApp.paused = !myApp.paused
                }
            }
            button(">") {
                addClass(Styles.controlButton)
            }
            button(">>") {
                addClass(Styles.controlButton)
            }
        }
    }

    override fun onDock() {
        super.onDock()
        super.currentStage?.isMaximized = true
    }
}

class DataView() : View("")
{
    override val root = vbox {
        for (name in DataLoader.workoutList())
        {
            button(name)
            {
                addClass(Styles.selectionButton)
            }
        }
    }
}