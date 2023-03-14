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
package org.kryptonmc.krypton.coordinate

import org.kryptonmc.api.util.Position

/**
 * Holds a pair of chunk coordinates (x and z).
 */
@JvmRecord
data class ChunkPos(val x: Int, val z: Int) {

    fun pack(): Long = pack(x, z)

    override fun toString(): String = "($x, $z)"

    companion object {

        @JvmField
        val ZERO: ChunkPos = ChunkPos(0, 0)

        @JvmStatic
        fun pack(x: Int, z: Int): Long = x.toLong() and 0xFFFFFFFFL or (z.toLong() and 0xFFFFFFFFL shl 32)

        @JvmStatic
        fun unpackX(encoded: Long): Int = (encoded and 0xFFFFFFFFL).toInt()

        @JvmStatic
        fun unpackZ(encoded: Long): Int = (encoded ushr 32 and 0xFFFFFFFFL).toInt()

        @JvmStatic
        fun forEntityPosition(position: Position): ChunkPos {
            return ChunkPos(SectionPos.blockToSection(position.x), SectionPos.blockToSection(position.z))
        }
    }
}
