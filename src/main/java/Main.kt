import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.BorderFactory
import javax.swing.JFrame

fun main() {


    val controller = Controller()


    val jFrame = JFrame()

    jFrame.contentPane = Panel(controller).apply {
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    }


    jFrame.isVisible = true
    jFrame.isFocusable = true
    jFrame.pack()
    jFrame.setLocationRelativeTo(null)
    jFrame.isVisible = true

    // refer https://stackoverflow.com/questions/901224/listening-for-input-without-focus-in-java
    GlobalScreen.registerNativeHook()

    GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
        override fun nativeKeyTyped(p0: NativeKeyEvent?) {

        }

        override fun nativeKeyPressed(e: NativeKeyEvent) {
            val customKey = CustomKey(e.keyCode, e.modifiers)
            println("find key $customKey")
            if (keyMaps.containsValue(customKey)) {
                val action = keyMaps.entries.first { it.value == customKey }.key
                println("do action $customKey and do $action")
                when (action) {
                    Action.PAUSE -> controller.pauseMedia()
                    Action.PREV -> controller.prevMedia()
                    Action.NEXT -> controller.nextMedia()
                    Action.ADD_VOL -> controller.addVolume()
                    Action.DEL_VOL -> controller.delVolume()
                }
            }
        }

        override fun nativeKeyReleased(p0: NativeKeyEvent?) {
        }
    })

}
