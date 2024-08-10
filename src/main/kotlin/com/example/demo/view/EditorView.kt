package com.example.demo.view

import com.example.demo.app.exercisemodel.Exercise
import com.example.demo.app.exercisemodel.ExerciseSet
import com.example.demo.app.exercisemodel.ScheduleElement
import javafx.scene.Parent
import javafx.scene.control.TreeItem
import tornadofx.*

class EditorView: View("Workouttimer") {

    val top = Exercise.exerciseFromList(arrayOf(
        Exercise("top", 3),
        Exercise("lel", 3),
        ExerciseSet(
            3,
            arrayOf(
                Exercise("l2l,", 3),
                Exercise("999292", 4)
            )
        )
    ))

    override val root: Parent = stackpane {
        treeview<ScheduleElement> {
            root = TreeItem(top)

            cellFormat {
                vbox {
                    if (it is Exercise)
                    {
                        label(it.name)
                        hbox {
                            label("Duration: ")
                            textfield (it.duration.toString())
                        }
                    } else if (it is ExerciseSet)
                    {
                        hbox {
                            label("Repeat for")
                            textfield(it.repetitions.toString())
                        }
                    }
                }
            }

            populate {
                val item = it.value
                if (item is Exercise)
                    return@populate if (item.next != null) listOf(item.next!!) else null
                else
                    return@populate (item as ExerciseSet).elements.toList()
            }
        }
    }

}