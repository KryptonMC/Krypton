package org.kryptonmc.krypton.util

import com.mojang.brigadier.StringReader

fun StringReader.readString(length: Int): String {
    if (!canRead(length)) error("Expected string of length $length!")
    var string = ""
    for (i in 1..length) string += read()
    return string
}

fun StringReader.expect(string: String) {
    if (!canRead(string.length) || readString(string.length) != string) error("Expected $string!")
}
