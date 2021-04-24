package org.kryptonmc.krypton.server.gui.stats

import org.kryptonmc.krypton.KryptonServer
import java.awt.Dimension
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Vector
import javax.swing.DefaultListCellRenderer
import javax.swing.DefaultListSelectionModel
import javax.swing.JList
import javax.swing.border.EmptyBorder

class RAMDetails(private val server: KryptonServer) : JList<String>() {

    init {
        border = EmptyBorder(0, 10, 0, 0)
        fixedCellHeight = 20
        isOpaque = false
        cellRenderer = DefaultListCellRenderer().apply { isOpaque = false }
        selectionModel = NoOpListSelectionModel
    }

    fun update() {
        val data = RAMGraph.DATA.peekLast()
        setListData(Vector(listOf(
            "Memory use: ${data.usedMemory / MEGABYTES_IN_BYTES} MB (${data.free * 100L / data.max}% free)",
            "Heap: ${data.total / MEGABYTES_IN_BYTES} / ${data.max / MEGABYTES_IN_BYTES} MB",
            "Average tick: ${DECIMAL_FORMAT.format(server.tickTimes.average() * 1.0E-6)} ms"
        )))
    }

    override fun getPreferredSize() = Dimension(350, 100)

    companion object {

        private const val MEGABYTES_IN_BYTES = 1024L * 1024L

        val DECIMAL_FORMAT = DecimalFormat("########0.000").apply {
            decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ROOT)
        }
    }
}

private object NoOpListSelectionModel : DefaultListSelectionModel() {

    override fun setAnchorSelectionIndex(anchorIndex: Int) = Unit
    override fun setLeadAnchorNotificationEnabled(flag: Boolean) = Unit
    override fun setLeadSelectionIndex(leadIndex: Int) = Unit
    override fun setSelectionInterval(index0: Int, index1: Int) = Unit
}
