package org.kryptonmc.krypton.world.data

class DataVersion(val version: Int, val series: String) {

    constructor(version: Int) : this(version, MAIN_SERIES)

    fun isSideSeries(): Boolean = series != MAIN_SERIES

    fun isCompatible(other: DataVersion): Boolean = series == other.series

    companion object {

        const val MAIN_SERIES: String = "main"
    }
}
