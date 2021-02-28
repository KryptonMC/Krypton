package org.kryptonmc.krypton.extension

import kotlin.math.max

fun Int.calculateBits(): Int {
    var usefulBits = 0
    var value = this
    while (value != 0) {
        ++usefulBits
        value = value ushr 1
    }
    return max(usefulBits, 4)
}