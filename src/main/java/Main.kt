import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.awt.*
import java.awt.event.*
import java.io.*
import java.lang.Exception
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.*
import kotlin.system.exitProcess

fun main() {


    if (!keymapsFile.exists()) {
        keymapsFile.createNewFile()
    } else {
        readConfig()
    }


    val controller = Controller()



    JFrame().apply {
        val jframe = this
        contentPane = Panel(controller).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        }
        isFocusable = true
        pack()
        setLocationRelativeTo(null)
        isVisible = true



        // 这里注意到使用了controller的javaClass
        // 若是在直接使用了javaClass,则会使用javax.JFrame的class，而这个class的resource会找不到图片
        // 原因是这两个类的加载器是不一样的哈
        println(javaClass.classLoader)
        println(controller.javaClass.classLoader)
        val myImg = ImageIcon(controller.javaClass.getResource("avatar_32x32.jpg")).image
        val smallImg = ImageIcon(controller.javaClass.getResource("avatar_16x16.jpg")).image
        iconImage= myImg


        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                saveConfig()

                val optionPanel = JOptionPane("minimize to tray or exit ?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null
                )
                val options = arrayOf(
                        JButton(object : AbstractAction("minimize") {
                            override fun actionPerformed(e: ActionEvent?) {

                                // 对话框是另一个window了哈
                                SwingUtilities.getWindowAncestor(optionPanel).dispose()

                                val popupMenu = PopupMenu()
                                val trayIcon = TrayIcon(smallImg, "control", popupMenu)
                                popupMenu.apply {
                                    add(MenuItem("open").apply {
                                        addActionListener {
                                            jframe.isVisible = true
                                            SystemTray.getSystemTray().remove(trayIcon)
                                        }
                                    })
                                    add(MenuItem("exit")).apply {
                                        addActionListener {
                                            exitProcess(0)
                                        }
                                    }
                                }
                                SystemTray.getSystemTray().add(trayIcon)
                            }

                        }),
                        JButton(object : AbstractAction("exit") {

                            override fun actionPerformed(e: ActionEvent?) {
                                exitProcess(0)
                            }

                        }),
                )
                optionPanel.options = options

                if (SystemTray.isSupported()) {
                    optionPanel.createDialog(jframe, null).run {
                        isVisible=true
                    }
                }else{
                    exitProcess(0)
                }
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
                    MediaAction.PAUSE -> controller.pauseMedia()
                    MediaAction.PREV -> controller.prevMedia()
                    MediaAction.NEXT -> controller.nextMedia()
                    MediaAction.ADD_VOL -> controller.addVolume()
                    MediaAction.DEL_VOL -> controller.delVolume()
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
                keyMaps = it as MutableMap<MediaAction, CustomKey>
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
