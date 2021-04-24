package org.kryptonmc.krypton.server.gui.stats

import org.kryptonmc.krypton.KryptonServer
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.Timer

class GuiStatsComponent(server: KryptonServer) : JPanel(BorderLayout()), AutoCloseable {

    private val ramGraph = RAMGraph()
    private val timer: Timer

    init {
        isOpaque = false

        val ramDetails = RAMDetails(server)
        add(ramGraph, "North")
        add(ramDetails, "Center")

        timer = Timer(500) {
            ramGraph.update()
            ramDetails.update()
        }.apply { start() }
    }

    override fun getPreferredSize() = Dimension(350, 200)

    override fun close() {
        timer.stop()
        ramGraph.close()
    }
}
