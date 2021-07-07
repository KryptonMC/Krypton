package org.kryptonmc.krypton.util

import com.mojang.brigadier.StringReader

fun StringReader.readString(length: Int): String {
    if (!canRead(4)) error("Expected string of length $length!")
    var string = ""
    for (i in 1..length) string += read()
    return string
}

fun StringReader.peekString(length: Int): String {
    var string = ""
    for (i in 1..length) string += peek()
    return string
}

fun StringReader.expect(string: String) {
    if (!canRead() || readString(string.length) != string) error("Expected $string!")
}
