package com.example.demo.view

import com.example.demo.app.MyApp
import com.example.demo.app.Styles
import com.example.demo.app.io.DataLoader
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.effect.BoxBlur
import javafx.scene.effect.Shadow
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import javax.xml.crypto.Data

class MainView : View("Workouttimer") {

    private val timeMinutes = SimpleIntegerProperty(0)
    private val timeSeconds = SimpleIntegerProperty(0)
    val exerciseDisplay = SimpleStringProperty("something goes here")
    var inDataview = true
    private lateinit var mainElement: VBox
    private lateinit var dataElement: Node

    var time = 0
        set(value)
        {
            timeMinutes.value = value / 60
            timeSeconds.value = value % 60
            field = value
        }

    override val root = stackpane {
        val myApp: MyApp = app as MyApp
        mainElement = vbox {

            alignment = Pos.CENTER
            paddingAll = 5.0

            addEventFilter(KeyEvent.KEY_PRESSED) {
                if (it.text != " ")
                    return@addEventFilter
                myApp.paused = !myApp.paused
            }

            stackpane {
                hgrow = Priority.ALWAYS
                label("YOU CAN DO IT!!!") {
                    addClass(Styles.exerciseLabel)
                }
                vbox {
                    alignment = Pos.CENTER_LEFT
                    button("☰") {
                        addClass(Styles.switchButton)
                        action {
                            showLoadingScreen()
                        }
                        isFocusTraversable = false
                    }
                }
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
            effect = Shadow()
        }
        dataElement = vbox {
            addClass(Styles.dataView)
            alignment = Pos.TOP_CENTER

            label ("Chose your Workout") {
                addClass(Styles.dataHeading)
            }
            vbox {
                paddingAll = 20
                vgrow = Priority.ALWAYS
                alignment = Pos.CENTER

                val workouts = DataLoader.workoutList()
                if (workouts.isEmpty())
                {
                    label("No saved workouts")
                }
                button("Load default") {
                    addClass(Styles.selectionButton)
                    action {
                        myApp.loadDefaultWorkout()
                        hideLoadingScreen()
                    }
                }

                if (workouts.isNotEmpty())
                {
                    vgrow = Priority.ALWAYS
                    alignment = Pos.CENTER
                    separator {
                        paddingAll = 10
                    }
                    for (workout in workouts)
                    {
                        button (workout) {
                            addClass(Styles.selectionButton)
                            hgrow = Priority.ALWAYS
                            paddingAll = 50
                            spacing = 5.0

                            action {
                                myApp.loadWorkout(workout)
                                hideLoadingScreen()
                            }
                        }
                    }
                }
                spacer()
            }
        }
    }

    private fun showLoadingScreen()
    {
        mainElement.effect = Shadow()
        dataElement.show()
        inDataview = true
    }

    private fun hideLoadingScreen()
    {
        mainElement.effect = null
        dataElement.hide()
        inDataview = false
    }

    override fun onDock() {
        super.onDock()
        super.currentStage?.isMaximized = true
    }
}

