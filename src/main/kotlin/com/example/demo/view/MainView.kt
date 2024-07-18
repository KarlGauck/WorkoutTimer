package com.example.demo.view

import com.example.demo.app.MyApp
import com.example.demo.app.Styles
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    private val timeMinutes = SimpleIntegerProperty(0)
    private val timeSeconds = SimpleIntegerProperty(0)
    private val exerciseDisplay = SimpleStringProperty("something goes here")
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
        label("YOU CAN DO IT!!!") {
            addClass(Styles.exerciseLabel)
        }
        label(exerciseDisplay) {
            addClass(Styles.heading)
            vgrow = Priority.ALWAYS
            useMaxHeight = true
        }
        label {
            fun changeText () {
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
                    myApp.paused = myApp.paused.not()
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