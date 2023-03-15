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
package org.kryptonmc.krypton.network.buffer

import java.io.InputStream
import java.nio.ByteBuffer
import kotlin.math.min

/**
 * A simple input stream backed by a byte buffer.
 *
 * Source: https://stackoverflow.com/questions/4332264/wrapping-a-bytebuffer-with-an-inputstream/6603018#6603018
 */
class ByteBufferInputStream(private val buffer: ByteBuffer) : InputStream() {

    override fun read(): Int {
        if (!buffer.hasRemaining()) return -1
        return buffer.get().toInt() and 0xFF
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (!buffer.hasRemaining()) return -1
        val length = min(len, buffer.remaining())
        buffer.get(b, off, length)
        return length
    }
}
