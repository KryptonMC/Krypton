package org.kryptonmc.krypton.space

data class BoundingBox(
    val minimumX: Int,
    val minimumY: Int,
    val minimumZ: Int,
    val maximumX: Int,
    val maximumY: Int,
    val maximumZ: Int
)