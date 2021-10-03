/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util.converter.versions

import ca.spottedleaf.dataconverter.types.ObjectType
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.isPowerOfTwo
import kotlin.math.max

object V2527 {

    private const val VERSION = MCVersions.V20W16A + 1

    fun register() = MCTypeRegistry.CHUNK.addStructureConverter(VERSION) { data, _, _ ->
        val level = data.getMap<String>("Level") ?: return@addStructureConverter null
        level.getList("Sections", ObjectType.MAP)?.let {
            for (i in 0 until it.size()) {
                val section = it.getMap<String>(i)
                val palette = section.getList("Palette", ObjectType.MAP) ?: continue
                val bits = max(4, palette.size().ceillog2())
                if (bits.isPowerOfTwo()) continue // fits perfectly
                val states = section.getLongs("BlockStates") ?: continue // wat
                section.setLongs("BlockStates", addPadding(4096, bits, states))
            }
        }

        level.getMap<String>("Heightmaps")?.let { heightmaps ->
            heightmaps.keys().forEach {
                val old = heightmaps.getLongs(it)
                heightmaps.setLongs(it, addPadding(256, 9, old))
            }
        }
        null
    }

    private fun addPadding(indices: Int, bits: Int, old: LongArray): LongArray {
        val k = old.size
        if (k == 0) return old
        val l = (1L shl bits) - 1L
        val m = 64 / bits
        val n = (indices + m - 1) / m
        val padded = LongArray(n)
        var o = 0
        var p = 0
        var q = 0L
        var r = 0
        var s = old[0]
        var t = if (k > 1) old[1] else 0L

        for (u in 0 until indices) {
            val v = u * bits
            val w = v shr 6
            val x = (u + 1) * bits - 1 shl 6
            val y = v xor w shl 6
            if (w != r) {
                s = t
                t = if (w + 1 < k) old[w + 1] else 0L
                r = w
            }

            val ab: Long
            var ac: Int
            if (w == x) {
                ab = s ushr y and l
            } else {
                ac = 64 - y
                ab = (s ushr y or (t shl ac)) and l
            }

            ac = p + bits
            if (ac >= 64) {
                padded[o++] = q
                q = ab
                p = bits
            } else {
                q = q or (ab shl p)
                p = ac
            }
        }

        if (q != 0L) padded[o] = q
        return padded
    }
}
