package com.wjp

import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.io.Serializable
import javax.swing.JTextField

class CustomJTextFiled(id: MediaAction) : JTextField() {

    init {
        val key = keyMaps[id]!!
        setText(key)


        // NativeEvent 和 Java 的Event 互转很麻烦
        // refer https://github.com/kwhat/jnativehook/issues/63
        val listener = object : NativeKeyListener {
            override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {

            }

            override fun nativeKeyPressed(e: NativeKeyEvent) {
                keyMaps[id] = key.copy(keyCode = e.keyCode, keyMod = e.modifiers)
                setText(keyMaps[id]!!)
            }

            override fun nativeKeyReleased(nativeEvent: NativeKeyEvent?) {
            }

        }
        addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent?) {
                GlobalScreen.addNativeKeyListener(listener)
            }

            override fun focusLost(e: FocusEvent?) {
                GlobalScreen.removeNativeKeyListener(listener)
            }
        })

    }

    private fun setText(key: CustomKey) {
        text = NativeKeyEvent.getModifiersText(key.keyMod) + " + " + NativeKeyEvent.getKeyText(key.keyCode)
    }
}

data class CustomKey(
        val keyCode: Int,
        val keyMod: Int,
):Serializable

var keyMaps = mutableMapOf<MediaAction, CustomKey>(
        MediaAction.PAUSE to CustomKey(NativeKeyEvent.VC_SPACE, NativeKeyEvent.CTRL_L_MASK or NativeKeyEvent.ALT_L_MASK),
        MediaAction.PREV to CustomKey(NativeKeyEvent.VC_LEFT, NativeKeyEvent.CTRL_L_MASK or NativeKeyEvent.ALT_L_MASK),
        MediaAction.NEXT to CustomKey(NativeKeyEvent.VC_RIGHT, NativeKeyEvent.CTRL_L_MASK or NativeKeyEvent.ALT_L_MASK),

        MediaAction.ADD_VOL to CustomKey(NativeKeyEvent.VC_UP, NativeKeyEvent.CTRL_L_MASK or NativeKeyEvent.ALT_L_MASK),
        MediaAction.DEL_VOL to CustomKey(NativeKeyEvent.VC_DOWN, NativeKeyEvent.CTRL_L_MASK or NativeKeyEvent.ALT_L_MASK),
)