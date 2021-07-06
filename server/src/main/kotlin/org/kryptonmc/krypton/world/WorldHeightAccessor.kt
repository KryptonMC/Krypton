package org.kryptonmc.krypton.world

interface WorldHeightAccessor {

    val height: Int
    val minimumBuildHeight: Int

    val maximumBuildHeight: Int
        get() = minimumBuildHeight + height

    val minimumSection: Int
        get() = minimumBuildHeight shr 4

    val maximumSection: Int
        get() = ((maximumBuildHeight - 1) shr 4) + 1

    val sectionCount: Int
        get() = maximumSection - minimumSection

    fun sectionIndexFromY(y: Int) = y - minimumSection

    fun sectionYFromIndex(index: Int) = index + minimumSection

    fun sectionIndex(y: Int) = sectionIndexFromY(y shr 4)
}
