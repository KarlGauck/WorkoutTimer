package com.example.demo.view

import com.example.demo.app.MyApp
import com.example.demo.app.Styles
import com.example.demo.app.io.DataLoader
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.effect.BoxBlur
import javafx.scene.effect.ColorAdjust
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.effect.GaussianBlur
import javafx.scene.effect.Shadow
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.*
import javax.xml.crypto.Data

class MainView : View("Workouttimer") {

    private val timeMinutes = SimpleIntegerProperty(0)
    private val timeSeconds = SimpleIntegerProperty(0)
    val exerciseDisplay = SimpleStringProperty("something goes here")
    val imageSource = SimpleStringProperty(null)
    private var inDataview = true
    private lateinit var mainElement: VBox
    private lateinit var dataElement: Node
    private lateinit var blurRect: Node
    private lateinit var imageView: Node

    private val backgroundEffect: Effect
        get() {
            val blur = GaussianBlur()
            blur.input = DropShadow()
            return blur
        }

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

                if (it.code == KeyCode.ESCAPE)
                {
                    hideLoadingScreen()
                    it.consume()
                }
                if (it.code == KeyCode.S)
                {
                    showLoadingScreen()
                    it.consume()
                }
                if (it.code == KeyCode.SPACE)
                {
                    myApp.paused = !myApp.paused
                    it.consume()
                }
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
            imageView = imageview(imageSource) {
                hide()
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
                    action {
                        myApp.backToStart()
                    }
                }
                button("<") {
                    addClass(Styles.controlButton)
                    action {
                        myApp.back()
                    }
                }
                button("=") {
                    addClass(Styles.controlButton)
                    setOnAction {
                        myApp.paused = !myApp.paused
                    }
                }
                button(">") {
                    addClass(Styles.controlButton)
                    action {
                        myApp.next()
                    }
                }
                button(">>") {
                    addClass(Styles.controlButton)
                    action {
                        if (text == ">>")
                            text = "Seriously, this button?"
                        else if (text == "Seriously, this button?")
                            text = ">>"
                    }
                }
            }
            effect = backgroundEffect
        }
        blurRect = rectangle {
            width = 10000.0
            height = 10000.0
            fill = Color.color(0.0, 0.0, 0.0, 0.8)
        }
        dataElement = vbox {
            addClass(Styles.dataView)
            alignment = Pos.CENTER

            spacer()

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
                    {
                        textFill = Color.WHITE
                    }
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
                    for (workout in workouts)
                    {
                        val data = DataLoader.getWorkout(workout)
                        val dur = data.getTotalDuration()
                        button ("$workout (${dur/60}:${dur%60})") {
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

    fun showImageView()
    {
        imageView.show()
    }

    fun hideImageView()
    {
        imageView.hide()
    }

    private fun showLoadingScreen()
    {
        mainElement.effect = backgroundEffect
        dataElement.show()
        blurRect.show()
        inDataview = true
    }

    private fun hideLoadingScreen()
    {
        mainElement.effect = null
        dataElement.hide()
        blurRect.hide()
        inDataview = false
    }

    override fun onDock() {
        super.onDock()
        super.currentStage?.isMaximized = true
    }
}

