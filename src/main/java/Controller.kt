class Controller {

    fun nextMedia() {
        sendCode(87)
    }
    fun prevMedia() {
        sendCode(88)
    }
    fun pauseMedia() {
        sendCode(85)
    }

    fun addVolume() {
        sendCode(24)
    }
    fun delVolume() {
        sendCode(25)
    }

    private fun sendCode(keyCode:Int) {
        Runtime.getRuntime().exec("adb shell input keyevent $keyCode")
    }
}