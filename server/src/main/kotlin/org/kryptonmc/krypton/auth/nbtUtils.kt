/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.auth

import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound
import java.util.UUID

private val NIL_UUID = UUID(0L, 0L)

fun CompoundTag.getGameProfile(key: String): GameProfile? {
    if (contains(key, CompoundTag.ID)) return getCompound(key).toGameProfile()
    if (contains(key, StringTag.ID) && getString(key).isNotBlank()) return KryptonGameProfile(getString(key), NIL_UUID, persistentListOf())
    return null
}

fun CompoundTag.toGameProfile(): GameProfile? {
    val name = if (contains("Name", StringTag.ID)) getString("Name") else return null
    val uuid = getUUID("Id") ?: return null
    if (!contains("Properties", CompoundTag.ID)) return KryptonGameProfile(name, uuid, persistentListOf())

    val properties = persistentListOf<ProfileProperty>().builder()
    getCompound("Properties").forEachList { profileKey, list ->
        list.forEachCompound {
            val value = it.getString("Value")
            val signature = if (it.contains("Signature", StringTag.ID)) it.getString("Signature") else null
            properties.add(KryptonProfileProperty(profileKey, value, signature))
        }
    }
    return KryptonGameProfile(name, uuid, properties.build())
}

fun CompoundTag.putGameProfile(key: String, profile: GameProfile?): CompoundTag {
    if (profile == null) return CompoundTag.empty()
    return put(key, profile.toCompound())
}

fun CompoundTag.Builder.gameProfile(key: String, profile: GameProfile?): CompoundTag.Builder = apply {
    if (profile != null) put(key, profile.toCompound())
}

private fun GameProfile.toCompound(): CompoundTag = compound {
    string("Name", name)
    uuid("Id", uuid)
    if (properties.isEmpty()) return@compound
    put("Properties", compound {
        properties.forEach {
            val data = compound {
                string("Value", it.value)
                if (it.signature != null) string("Signature", it.signature!!)
            }
            list(it.name, CompoundTag.ID, listOf(data))
        }
    })
}
