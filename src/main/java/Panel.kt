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
        add(CustomJTextFiled(Action.PAUSE))


        add(JButton("prev").apply {
            addActionListener {
                controller.prevMedia()
            }
        })
        add(CustomJTextFiled(Action.PREV))


        add(JButton("next").apply {
            addActionListener {
                controller.nextMedia()
            }
        })
        add(CustomJTextFiled(Action.NEXT))

        add(JButton("add volume").apply {
            addActionListener {
                controller.addVolume()
            }
        })
        add(CustomJTextFiled(Action.ADD_VOL))

        add(JButton("del volume").apply {
            addActionListener {
                controller.delVolume()
            }
        })
        add(CustomJTextFiled(Action.DEL_VOL))

        requestFocus()
    }


}

