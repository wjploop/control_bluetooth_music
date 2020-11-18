import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.awt.event.*
import java.io.*
import java.lang.Exception
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JFrame
import kotlin.system.exitProcess

fun main() {


    if (!keymapsFile.exists()) {
        keymapsFile.createNewFile()
    } else {
        readConfig()
    }


    val controller = Controller()


    JFrame().apply {
        contentPane = Panel(controller).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        }
        isFocusable = true
        pack()
        setLocationRelativeTo(null)
        isVisible = true

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                saveConfig()
                exitProcess(0)
            }
        })
    }

    // refer https://stackoverflow.com/questions/901224/listening-for-input-without-focus-in-java
    GlobalScreen.registerNativeHook()

    Logger.getLogger(GlobalScreen::class.java.`package`.name).apply {
        level = Level.OFF
    }

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

val keymapsFile = File("keymaps")

fun readConfig() {
    ObjectInputStream(FileInputStream(keymapsFile)).use {
        try {
            it.readObject()?.let {
                @Suppress("UNCHECKED_CAST")
                keyMaps = it as MutableMap<Action, CustomKey>
            }
        } catch (e: Exception) {

        }
    }
}

fun saveConfig() {
    ObjectOutputStream(FileOutputStream(keymapsFile)).use {
        it.writeObject(keyMaps)
    }
}
