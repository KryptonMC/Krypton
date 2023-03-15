/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util

import java.nio.ByteBuffer

/*
 * The var int encoding is a variable-length encoding, which consists of a sequence of bytes, with 7 bits representing the value, and the 8th bit
 * indicating whether there is a next byte to read or not.
 *
 * For the reading, we do a simple loop up to the maximum bytes we can read in a var int/long, which is 5 and 10 respectively.
 * We then read each byte individually, shift the value in to the correct position, ignoring the marker bit (value & 0x7F gets rid of the marker
 * bit and << j * 7 shifts left by 7 bits * the byte index for each byte we read).
 * Then, we check if the marker bit is set, and if it isn't, we are done reading, otherwise, we continue reading.
 *
 * For the writing, instead of implementing it using a looping mechanism, we check each value individually for better performance.
 * We check the value against each individual case, by taking the unsigned maximum integer value (all 32 bits set to 1) and shifting it left by
 * the amount of bits we want to check, which leaves us with a value that has only the number of bits we want to check set to 0. By doing an AND
 * against this value and the provided value, we can check if the value has no bits above that number set to 1, as any number & 1 = any.
 *
 * With var longs, these methods are simply extended to 10 bytes.
 */

// https://github.com/jvm-profiling-tools/async-profiler/blob/a38a375dc62b31a8109f3af97366a307abb0fe6f/src/converter/one/jfr/JfrReader.java#L393
@Suppress("MagicNumber")
fun ByteBuffer.readVarInt(): Int {
    var result = 0
    var shift = 0
    while (true) {
        val next = get()
        result = result or ((next.toInt() and 0x7F) shl shift)
        if (next >= 0) return result
        shift += 7
    }
}

// This way of writing var ints came from an article by Andrew Steinborn of Velocity.
// The article: https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
@Suppress("MagicNumber") // Explained in a comment on readVarInt
fun ByteBuffer.writeVarInt(value: Int) {
    when {
        value.toLong() and (0xFFFFFFFF shl 7) == 0L -> put(value.toByte())
        value.toLong() and (0xFFFFFFFF shl 14) == 0L -> putShort((value and 0x7F or 0x80 shl 8 or (value ushr 7)).toShort())
        value.toLong() and (0xFFFFFFFF shl 21) == 0L -> {
            put((value and 0x7F or 0x80).toByte())
            put(((value shr 7) and 0x7F or 0x80).toByte())
            put((value shr 14).toByte())
        }
        value.toLong() and (0xFFFFFFFF shl 28) == 0L -> putInt(value and 0x7F or 0x80 shl 24 or
                (value ushr 7 and 0x7F or 0x80 shl 16) or (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21))
        else -> {
            putInt(value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16) or
                    (value shr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80))
            put((value ushr 28).toByte())
        }
    }
}

fun ByteBuffer.writeEmptyVarIntHeader(): Int {
    val index = position()
    position(index + 3)
    return index
}

fun ByteBuffer.writeVarIntHeader(index: Int, value: Int) {
    put(index, (value and 0x7F or 0x80).toByte())
    put(index + 1, ((value ushr 7) and 0x7F or 0x80).toByte())
    put(index + 2, (value ushr 14).toByte())
}
