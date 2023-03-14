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
package org.kryptonmc.krypton.adventure.provider

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.util.Codec
import org.kryptonmc.krypton.util.nbt.SNBTParser
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import java.io.IOException
import java.lang.RuntimeException
import java.util.UUID

/**
 * Serializes and deserializes hover events that used to be stored as
 * components and are now dedicated objects. This uses Krypton's SNBT codec
 * and Krypton NBT to parse NBT.
 */
object NBTLegacyHoverEventSerializer : LegacyHoverEventSerializer {

    private const val ITEM_TYPE = "id"
    private const val ITEM_COUNT = "Count"
    private const val ITEM_TAG = "tag"
    private const val ENTITY_TYPE = "type"
    private const val ENTITY_NAME = "name"
    private const val ENTITY_ID = "id"

    private val SNBT_CODEC: Codec<CompoundTag, String, CommandSyntaxException, RuntimeException> =
        Codec.codec({ SNBTParser.parse(it) }, { it.asString() })

    override fun deserializeShowItem(input: Component): HoverEvent.ShowItem {
        return try {
            val nbt = SNBT_CODEC.decode(PlainTextComponentSerializer.plainText().serialize(input))
            val tag = nbt.getCompound(ITEM_TAG)
            val holder = if (!tag.isEmpty) BinaryTagHolder.encode(tag, SNBT_CODEC) else null
            HoverEvent.ShowItem.of(Key.key(nbt.getString(ITEM_TYPE)), nbt.getByte(ITEM_COUNT).toInt(), holder)
        } catch (exception: CommandSyntaxException) {
            throw IOException(exception)
        }
    }

    override fun deserializeShowEntity(input: Component, decoder: Codec.Decoder<Component, String, out RuntimeException>): HoverEvent.ShowEntity {
        return try {
            val nbt = SNBT_CODEC.decode(PlainTextComponentSerializer.plainText().serialize(input))
            val name = decoder.decode(nbt.getString(ENTITY_NAME))
            HoverEvent.ShowEntity.of(Key.key(nbt.getString(ENTITY_TYPE)), UUID.fromString(nbt.getString(ENTITY_ID)), name)
        } catch (exception: CommandSyntaxException) {
            throw IOException(exception)
        }
    }

    override fun serializeShowItem(input: HoverEvent.ShowItem): Component {
        val tag = ImmutableCompoundTag.builder().putString(ITEM_TYPE, input.item().asString()).putByte(ITEM_COUNT, input.count().toByte())
        try {
            input.nbt()?.let { tag.put(ITEM_TAG, it.get(SNBT_CODEC)) }
        } catch (exception: CommandSyntaxException) {
            throw IOException(exception)
        }
        return Component.text(SNBT_CODEC.encode(tag.build()))
    }

    override fun serializeShowEntity(input: HoverEvent.ShowEntity, encoder: Codec.Encoder<Component, String, out RuntimeException>): Component {
        val tag = ImmutableCompoundTag.builder().putString(ENTITY_ID, input.id().toString()).putString(ENTITY_TYPE, input.type().asString())
        input.name()?.let { tag.putString(ENTITY_NAME, encoder.encode(it)) }
        return Component.text(SNBT_CODEC.encode(tag.build()))
    }
}
