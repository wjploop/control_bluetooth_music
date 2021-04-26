package com.wjp

import java.awt.GridLayout
import javax.swing.*

class Panel(val controller: Controller) : JPanel() {
    init {
        layout = GridLayout(0, 2, 10, 20)

        add(JButton("play / pause").apply {
            addActionListener {
                controller.pauseMedia()
            }
        })
        add(CustomJTextFiled(MediaAction.PAUSE))


        add(JButton("prev").apply {
            addActionListener {
                controller.prevMedia()
            }
        })
        add(CustomJTextFiled(MediaAction.PREV))


        add(JButton("next").apply {
            addActionListener {
                controller.nextMedia()
            }
        })
        add(CustomJTextFiled(MediaAction.NEXT))

        add(JButton("add volume").apply {
            addActionListener {
                controller.addVolume()
            }
        })
        add(CustomJTextFiled(MediaAction.ADD_VOL))

        add(JButton("del volume").apply {
            addActionListener {
                controller.delVolume()
            }
        })
        add(CustomJTextFiled(MediaAction.DEL_VOL))

        requestFocus()
    }


}

