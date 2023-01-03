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
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import org.kryptonmc.nbt.StringTag

object GameProfileUtil {

    private const val NAME_TAG = "Name"
    private const val ID_TAG = "Id"
    private const val PROPERTIES_TAG = "Properties"
    private const val VALUE_TAG = "Value"
    private const val SIGNATURE_TAG = "Signature"

    @JvmStatic
    fun getProfile(data: CompoundTag, key: String): GameProfile? {
        if (data.contains(key, CompoundTag.ID)) return deserialize(data.getCompound(key))
        if (data.contains(key, StringTag.ID)) {
            val name = data.getString(key)
            if (name.isNotBlank()) return KryptonGameProfile.partial(name)
        }
        return null
    }

    @JvmStatic
    fun deserialize(data: CompoundTag): GameProfile? {
        val uuid = if (data.hasUUID(ID_TAG)) data.getUUID(ID_TAG) else return null
        val name = if (data.contains(NAME_TAG, StringTag.ID)) data.getString(NAME_TAG) else return null
        if (!data.contains(PROPERTIES_TAG, CompoundTag.ID)) return KryptonGameProfile.basic(uuid, name)

        val properties = persistentListOf<ProfileProperty>().builder()
        data.getCompound(PROPERTIES_TAG).forEachList { profileKey, list ->
            list.forEachCompound {
                val value = it.getString(VALUE_TAG)
                val signature = if (it.contains(SIGNATURE_TAG, StringTag.ID)) it.getString(SIGNATURE_TAG) else null
                properties.add(KryptonProfileProperty(profileKey, value, signature))
            }
        }
        return KryptonGameProfile.full(uuid, name, properties.build())
    }

    @JvmStatic
    fun putProfile(data: CompoundTag, key: String, profile: GameProfile?): CompoundTag =
        if (profile == null) data else data.put(key, serialize(profile))

    @JvmStatic
    fun putProfile(data: CompoundTag.Builder, key: String, profile: GameProfile?): CompoundTag.Builder =
        if (profile == null) data else data.put(key, serialize(profile))

    @JvmStatic
    fun serialize(profile: GameProfile): CompoundTag {
        val builder = ImmutableCompoundTag.builder().putString(NAME_TAG, profile.name).putUUID(ID_TAG, profile.uuid)
        if (profile.properties.isEmpty()) return builder.build()
        return builder.putCompound(PROPERTIES_TAG) { properties ->
            profile.properties.forEach { property ->
                val data = ImmutableCompoundTag.builder().putString(VALUE_TAG, property.value)
                if (property.signature != null) data.putString(SIGNATURE_TAG, property.signature!!)
                properties.putList(property.name) { it.add(data.build()) }
            }
        }.build()
    }
}
