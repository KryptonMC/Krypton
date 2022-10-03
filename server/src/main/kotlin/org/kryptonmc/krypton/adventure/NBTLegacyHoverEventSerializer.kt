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
package org.kryptonmc.krypton.adventure

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer
import net.kyori.adventure.util.Codec
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.krypton.util.nbt.SNBTParser
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag
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

    private val SNBT_CODEC: Codec<CompoundTag, String, CommandSyntaxException, RuntimeException> = Codec.codec(SNBTParser::parse, Tag::asString)

    override fun deserializeShowItem(input: Component): HoverEvent.ShowItem {
        return try {
            val nbt = SNBT_CODEC.decode(input.toPlainText())
            val tag = nbt.getCompound(ITEM_TAG)
            val holder = if (!tag.isEmpty()) BinaryTagHolder.encode(tag, SNBT_CODEC) else null
            HoverEvent.ShowItem.of(Key.key(nbt.getString(ITEM_TYPE)), nbt.getByte(ITEM_COUNT).toInt(), holder)
        } catch (exception: CommandSyntaxException) {
            throw IOException(exception)
        }
    }

    override fun deserializeShowEntity(input: Component, decoder: Codec.Decoder<Component, String, out RuntimeException>): HoverEvent.ShowEntity {
        return try {
            val nbt = SNBT_CODEC.decode(input.toPlainText())
            val name = decoder.decode(nbt.getString(ENTITY_NAME))
            HoverEvent.ShowEntity.of(Key.key(nbt.getString(ENTITY_TYPE)), UUID.fromString(nbt.getString(ENTITY_ID)), name)
        } catch (exception: CommandSyntaxException) {
            throw IOException(exception)
        }
    }

    override fun serializeShowItem(input: HoverEvent.ShowItem): Component {
        val tag = CompoundTag.immutableBuilder().string(ITEM_TYPE, input.item().asString()).byte(ITEM_COUNT, input.count().toByte())
        try {
            input.nbt()?.let { tag.put(ITEM_TAG, it.get(SNBT_CODEC)) }
        } catch (exception: CommandSyntaxException) {
            throw IOException(exception)
        }
        return Component.text(SNBT_CODEC.encode(tag.build()))
    }

    override fun serializeShowEntity(input: HoverEvent.ShowEntity, encoder: Codec.Encoder<Component, String, out RuntimeException>): Component {
        val tag = CompoundTag.immutableBuilder().string(ENTITY_ID, input.id().toString()).string(ENTITY_TYPE, input.type().asString())
        input.name()?.let { tag.string(ENTITY_NAME, encoder.encode(it)) }
        return Component.text(SNBT_CODEC.encode(tag.build()))
    }
}
