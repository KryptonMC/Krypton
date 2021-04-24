package org.kryptonmc.krypton.server.gui.stats

import org.kryptonmc.krypton.util.square
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

object GraphColor {

    val lineColors = Array(101) {
        val colour = create(it)
        Color(colour.red / 2, colour.green / 2, colour.blue / 2, 255)
    }
    val fillColors = Array(101) { Color(lineColors[it].red, lineColors[it].green, lineColors[it].blue, 125) }

    private fun create(percent: Int): Color {
        if (percent <= 50) return Color(0x00FF00)

        val value = 510 - (min(max(0, ((percent - 50) / 50)), 1) * 510)

        val (red, green) = if (value < 255) {
            255 to (sqrt(value.toDouble()) * 16).toInt()
        } else {
            val newValue = value - 255
            (255 - (newValue.square() / 255)) to 255
        }
        return Color(red, green, 0)
    }
}
