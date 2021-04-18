package org.kryptonmc.krypton.util.gui.stats

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import javax.swing.Timer
import kotlin.math.roundToInt

class RAMGraph : JComponent(), AutoCloseable {

    private val timer = Timer(50) { repaint() }.apply { start() }

    private var currentTick = 0

    init {
        ToolTipManager.sharedInstance().initialDelay = 0
        addMouseListener(GraphMouseListener())
    }

    fun update() {
        val jvm = Runtime.getRuntime()
        DATA += GraphData(jvm.totalMemory(), jvm.freeMemory(), jvm.maxMemory())

        val pointerInfo = MouseInfo.getPointerInfo()
        if (pointerInfo != null) {
            val point = pointerInfo.location
            if (point != null) {
                val location = Point(point)
                SwingUtilities.convertPointFromScreen(location, this)
                if (contains(location)) {
                    ToolTipManager.sharedInstance().mouseMoved(MouseEvent(this, -1, System.currentTimeMillis(), 0, location.x, location.y, point.x, point.y, 0, false, 0))
                }
            }
        }

        currentTick++
    }

    override fun paint(graphics: Graphics) = graphics.run {
        color = Color.WHITE
        fillRect(0, 0, 350, 100)

        color = Color(0x888888)
        drawLine(1, 25, 348, 25)
        drawLine(1, 50, 348, 50)
        drawLine(1, 75, 348, 75)

        DATA.forEachIndexed { index, data ->
            if ((index + currentTick) % 120 == 0) {
                color = Color(0x888888)
                drawLine(index, 1, index, 99)
            }
            val used = data.usedPercentage
            if (used < 0) return@forEachIndexed

            val lineColor = data.lineColor
            color = data.fillColor
            fillRect(index, 100 - used, 1, used)
            color = lineColor
            fillRect(index, 100 - used, 1, 1)
        }

        color = Color.BLACK
        drawRect(0, 0, 348, 100)

        val mouse = mousePosition ?: return@run
        if (mouse.x in 1..347 && mouse.y in 1..99) {
            val data = DATA[mouse.x]
            val used = data.usedPercentage
            color = Color.BLACK
            drawLine(mouse.x, 1, mouse.x, 99)
            drawOval(mouse.x - 2, 100 - used - 2, 5, 5)
            color = data.lineColor
            fillOval(mouse.x - 2, 100 - used - 2, 5, 5)
            toolTipText = "<html><body>Used: ${(data.usedMemory / 1024F / 1024F).roundToInt()} MB ($used%)<br/>${mouse.x.time()}</body></html>"
        }
    }

    private fun Int.time(): String {
        val millis = (348 - this) / 2 * 1000
        return TIME_FORMAT.format(Date(System.currentTimeMillis() - millis))
    }

    override fun getPreferredSize() = Dimension(350, 110)

    override fun close() = timer.stop()

    companion object {

        private val TIME_FORMAT = SimpleDateFormat("HH:mm:ss")

        val DATA = object : LinkedList<GraphData>() {
            override fun add(element: GraphData): Boolean {
                if (size >= 348) remove()
                return super.add(element)
            }
        }

        init {
            val empty = GraphData(0, 0, 0)
            repeat(350) { DATA += empty }
        }
    }
}

private class GraphMouseListener : MouseAdapter() {

    val defaultDismissTimeout = ToolTipManager.sharedInstance().dismissDelay

    override fun mouseEntered(event: MouseEvent) {
        ToolTipManager.sharedInstance().dismissDelay = DISMISS_DELAY_MINUTES
    }

    override fun mouseExited(event: MouseEvent) {
        ToolTipManager.sharedInstance().dismissDelay = defaultDismissTimeout
    }

    companion object {

        private const val DISMISS_DELAY_MINUTES = 600000 // 10 minutes in milliseconds
    }
}