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

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry

object V1460 {

    private const val VERSION = MCVersions.V18W01A + 1
    private val MOTIVE_REMAP = mapOf(
        "donkeykong" to "donkey_kong",
        "burningskull" to "burning_skull",
        "skullandroses" to "skull_and_roses"
    )

    fun register() {
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:painting", VERSION) { data, _, _ ->
            var motive = data.getString("Motive")
            if (motive != null) {
                motive = motive.lowercase()
                data.setString("Motive", Key.key(MOTIVE_REMAP.getOrDefault(motive, motive)).asString())
            }
            null
        }

        // No idea why so many type redefines exist here in Vanilla. nothing about the data structure changed, it's literally a copy of
        // the existing types.
    }
}
