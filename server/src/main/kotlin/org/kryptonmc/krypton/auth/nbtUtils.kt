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
package org.kryptonmc.krypton.auth

import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.auth.requests.ApiService
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound

fun CompoundTag.getGameProfile(key: String): GameProfile? {
    val tag = get(key) ?: return null
    if (tag is StringTag) return ApiService.profile(tag.value).get()
    if (tag !is CompoundTag) return null
    val name = tag.getString("Name")
    val uuid = checkNotNull(tag.getUUID("Id")) { "UUID for skull owner cannot be null!" }
    if (!tag.contains("Properties", CompoundTag.ID)) return KryptonGameProfile(name, uuid, persistentListOf())
    val properties = persistentListOf<ProfileProperty>().builder()
    tag.getCompound("Properties").forEachList { profileKey, list ->
        list.forEachCompound {
            val value = it.getString("Value")
            val signature = if (it.contains("Signature", StringTag.ID)) it.getString("Signature") else null
            properties.add(KryptonProfileProperty(profileKey, value, signature))
        }
    }
    return KryptonGameProfile(name, uuid, properties.build())
}

fun CompoundTag.Builder.gameProfile(key: String, profile: GameProfile?): CompoundTag.Builder = apply {
    if (profile == null) return@apply
    compound(key) {
        string("Name", profile.name)
        uuid("Id", profile.uuid)
        if (profile.properties.isNotEmpty()) {
            put("Properties", compound {
                profile.properties.forEach {
                    val tag = compound {
                        string("Value", it.value)
                        if (it.signature != null) string("Signature", it.signature!!)
                    }
                    list(it.name, CompoundTag.ID, listOf(tag))
                }
            })
        }
    }
}
