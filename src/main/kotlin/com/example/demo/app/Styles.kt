package com.example.demo.app

import com.sun.java.swing.plaf.windows.WindowsBorders.DashedBorder
import javafx.scene.effect.Shadow
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
        val selectionButton by cssclass()
        val switchButton by cssclass()
        val dataView by cssclass()
        val dataHeading by cssclass()
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
        selectionButton {
            and(hover) {
                backgroundColor = multi(Color.DARKGREY)
            }
            borderStyle = multi(BorderStrokeStyle.DASHED)
            borderRadius = multi(box(4.px))
            borderColor = multi(box(Color.BLACK))
            backgroundColor = multi(Color.color(1.0, 1.0, 1.0, 0.8))
            padding = box(5.0.px)
            fontSize = 40.px
            fontWeight = FontWeight.BOLD
            prefWidth = 20.pc
        }
        dataView {
            fill = Color.RED
        }
        dataHeading {
            fontSize = 50.px
            fontWeight = FontWeight.BOLD
            textFill = Color.WHITE
        }
        switchButton {
            and (hover) {
                textFill = Color.DARKGREY
            }
            backgroundColor = multi(Color.TRANSPARENT)
            fontSize = 20.px
        }
    }
}