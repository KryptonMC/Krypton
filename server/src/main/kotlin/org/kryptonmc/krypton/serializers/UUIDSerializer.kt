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
package org.kryptonmc.krypton.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.nbt.IntArrayBinaryTag
import java.util.UUID

/**
 * Standard UUID serialiser, as kotlinx.serialization still doesn't seem to have a built-in
 */
object UUIDSerializer : KSerializer<UUID> {

    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}

/**
 * This isn't related to the above, this turns a UUID into 4 integers sorted by significance descending
 * (most significant first, least significant last)
 */
fun UUID.serialize() = IntArrayBinaryTag.of(
    (mostSignificantBits shr 32).toInt(),
    (mostSignificantBits and Int.MAX_VALUE.toLong()).toInt(),
    (leastSignificantBits shr 32).toInt(),
    (leastSignificantBits and Int.MAX_VALUE.toLong()).toInt()
)
