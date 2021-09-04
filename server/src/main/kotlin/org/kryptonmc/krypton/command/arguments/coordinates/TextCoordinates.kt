package org.kryptonmc.krypton.command.arguments.coordinates

data class TextCoordinates(val x: String, val y: String, val z: String) {

    companion object {

        val CENTER_LOCAL = TextCoordinates("^", "^", "^")
        val CENTER_GLOBAL = TextCoordinates("~", "~", "~")
    }
}
