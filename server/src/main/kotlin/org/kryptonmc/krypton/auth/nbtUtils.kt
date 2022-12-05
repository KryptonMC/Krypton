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
import org.kryptonmc.krypton.util.UUIDUtil
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound

fun CompoundTag.getGameProfile(key: String): GameProfile? {
    if (contains(key, CompoundTag.ID)) return getCompound(key).toGameProfile()
    if (contains(key, StringTag.ID) && getString(key).isNotBlank()) return KryptonGameProfile(getString(key), UUIDUtil.NIL_UUID, persistentListOf())
    return null
}

private const val NAME_TAG = "Name"
private const val ID_TAG = "Id"
private const val PROPERTIES_TAG = "Properties"
private const val VALUE_TAG = "Value"
private const val SIGNATURE_TAG = "Signature"

fun CompoundTag.toGameProfile(): GameProfile? {
    val name = if (contains(NAME_TAG, StringTag.ID)) getString(NAME_TAG) else return null
    val uuid = getUUID(ID_TAG) ?: return null
    if (!contains(PROPERTIES_TAG, CompoundTag.ID)) return KryptonGameProfile(name, uuid, persistentListOf())

    val properties = persistentListOf<ProfileProperty>().builder()
    getCompound(PROPERTIES_TAG).forEachList { profileKey, list ->
        list.forEachCompound {
            val value = it.getString(VALUE_TAG)
            val signature = if (it.contains(SIGNATURE_TAG, StringTag.ID)) it.getString(SIGNATURE_TAG) else null
            properties.add(KryptonProfileProperty(profileKey, value, signature))
        }
    }
    return KryptonGameProfile(name, uuid, properties.build())
}

fun CompoundTag.putGameProfile(key: String, profile: GameProfile?): CompoundTag {
    if (profile == null) return this
    return put(key, profile.toCompound())
}

fun CompoundTag.Builder.putGameProfile(key: String, profile: GameProfile?): CompoundTag.Builder = apply {
    if (profile != null) put(key, profile.toCompound())
}

private fun GameProfile.toCompound(): CompoundTag = compound {
    putString(NAME_TAG, name)
    putUUID(ID_TAG, uuid)
    if (properties.isEmpty()) return@compound
    put(PROPERTIES_TAG, compound {
        properties.forEach {
            val data = compound {
                putString(VALUE_TAG, it.value)
                if (it.signature != null) putString(SIGNATURE_TAG, it.signature!!)
            }
            putList(it.name, CompoundTag.ID, listOf(data))
        }
    })
}
