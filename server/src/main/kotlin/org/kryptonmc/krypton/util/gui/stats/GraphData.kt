package org.kryptonmc.krypton.util.gui.stats

data class GraphData(var total: Long, var free: Long, var max: Long) {

    var usedMemory = total - free
    var usedPercentage = if (usedMemory == 0L) 0 else (usedMemory * 100L / max).toInt()

    val fillColor = GraphColor.fillColors[usedPercentage]
    val lineColor = GraphColor.lineColors[usedPercentage]
}