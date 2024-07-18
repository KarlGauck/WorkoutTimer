package com.example.demo.app

import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import javax.swing.BorderFactory

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val timeDisplay by cssclass()
        val exerciseLabel by cssclass()
        val controlButton by cssclass()
    }

    init {
        heading {
            padding = box(10.px)
            fontSize = 80.px
            fontWeight = FontWeight.BOLD
        }
        timeDisplay {
            padding = box(10.px)
            fontSize = 35.px
            fontWeight = FontWeight.BOLD
        }
        exerciseLabel {
            padding = box(5.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            fill = Color.DARKBLUE
        }
        button and controlButton {
            and(hover) {
                backgroundColor = multi(Color.DARKGREY)
            }
            borderRadius = multi(box(15.px))
            backgroundColor = multi(Color.TRANSPARENT)
            padding = box(2.0.px, 25.0.px)
            fontSize = 30.px
            fontWeight = FontWeight.BOLD
            fontFamily = "Comic Sans MS"
        }
    }
}