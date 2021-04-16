package org.kryptonmc.krypton.space

// TODO: Look into removing this and replacing it with the API one
data class BoundingBox(
    val minimumX: Int,
    val minimumY: Int,
    val minimumZ: Int,
    val maximumX: Int,
    val maximumY: Int,
    val maximumZ: Int
)