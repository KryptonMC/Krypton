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

import ca.spottedleaf.dataconverter.types.MapType
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.MCTypeRegistry
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converter.walkers.ItemsDataWalker

object V2511 {

    private const val VERSION = MCVersions.V20W09A + 1

    fun register() {
        val throwableConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val owner = data.getMap<String>("owner")
                data.remove("owner")
                if (owner == null) return null
                data.setUUID(owner.getLong("M"), owner.getLong("L"))
                return null
            }
        }
        val potionConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val potion = data.getMap<String>("Potion")
                data.remove("Potion")
                data.setMap("Item", potion ?: NBTTypeUtil.createEmptyMap<String>())
                return null
            }
        }
        val llamaSpitConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                val owner = data.getMap<String>("Owner")
                data.remove("Owner")
                if (owner == null) return null
                data.setUUID(owner.getLong("OwnerUUIDMost"), owner.getLong("OwnerUUIDLeast"))
                return null
            }
        }
        val arrowConverter = object : StringDataConverter(VERSION) {
            override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
                data.setUUID(data.getLong("OwnerUUIDMost"), data.getLong("OwnerUUIDLeast"))
                data.remove("OwnerUUIDMost")
                data.remove("OwnerUUIDLeast")
                return null
            }
        }

        MCTypeRegistry.ENTITY.addConverterForId("minecraft:egg", throwableConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:ender_pearl", throwableConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:experience_bottle", throwableConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:snowball", throwableConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:potion", throwableConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:potion", potionConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:llama_spit", llamaSpitConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:arrow", arrowConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:spectral_arrow", arrowConverter)
        MCTypeRegistry.ENTITY.addConverterForId("minecraft:trident", arrowConverter)

        // Vanilla migrates the potion item but does not change the schema.
        MCTypeRegistry.ENTITY.addWalker(VERSION, "minecraft:potion", ItemsDataWalker("Item"))
    }

    private fun createUUIDArray(most: Long, least: Long) = intArrayOf(
        (most ushr 32).toInt(),
        most.toInt(),
        (least ushr 32).toInt(),
        least.toInt()
    )

    private fun MapType<String>.setUUID(most: Long, least: Long) {
        if (most != 0L && least != 0L) setInts("OwnerUUID", createUUIDArray(most, least))
    }
}
