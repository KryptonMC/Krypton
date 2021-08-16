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
package org.kryptonmc.krypton.adventure

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer
import net.kyori.adventure.util.Codec
import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.nbt.buildCompound
import java.io.IOException
import java.lang.RuntimeException
import java.util.UUID

object NBTLegacyHoverEventSerializer : LegacyHoverEventSerializer {

    private const val ITEM_TYPE = "id"
    private const val ITEM_COUNT = "Count"
    private const val ITEM_TAG = "tag"
    private const val ENTITY_TYPE = "type"
    private const val ENTITY_NAME = "name"
    private const val ENTITY_ID = "id"

    override fun deserializeShowItem(input: Component) = try {
        val raw = input.toPlainText()
        val nbt = ADVENTURE_SNBT_CODEC.decode(raw)
        val tag = nbt.getCompound(ITEM_TAG)
        HoverEvent.ShowItem.of(
            Key.key(nbt.getString(ITEM_TYPE)),
            nbt.getByte(ITEM_COUNT).toInt(),
            if (tag.isEmpty()) null else BinaryTagHolder.encode(tag, ADVENTURE_SNBT_CODEC)
        )
    } catch (exception: CommandSyntaxException) {
        throw IOException(exception)
    }

    override fun deserializeShowEntity(input: Component, decoder: Codec.Decoder<Component, String, out RuntimeException>) = try {
        val raw = input.toPlainText()
        val nbt = ADVENTURE_SNBT_CODEC.decode(raw)
        HoverEvent.ShowEntity.of(
            Key.key(nbt.getString(ENTITY_TYPE)),
            UUID.fromString(nbt.getString(ENTITY_ID)),
            decoder.decode(nbt.getString(ENTITY_NAME))
        )
    } catch (exception: CommandSyntaxException) {
        throw IOException(exception)
    }

    override fun serializeShowItem(input: HoverEvent.ShowItem): Component {
        val tag = buildCompound {
            string(ITEM_TYPE, input.item().asString())
            byte(ITEM_COUNT, input.count().toByte())
        }
        input.nbt()?.let {
            try {
                tag.put(ITEM_TAG, it[ADVENTURE_SNBT_CODEC])
            } catch (exception: CommandSyntaxException) {
                throw IOException(exception)
            }
        }
        return Component.text(ADVENTURE_SNBT_CODEC.encode(tag.build()))
    }

    override fun serializeShowEntity(input: HoverEvent.ShowEntity, encoder: Codec.Encoder<Component, String, out RuntimeException>): Component {
        val tag = buildCompound {
            string(ENTITY_ID, input.id().toString())
            string(ENTITY_TYPE, input.type().asString())
        }
        input.name()?.let { tag.string(ENTITY_NAME, encoder.encode(it)) }
        return Component.text(ADVENTURE_SNBT_CODEC.encode(tag.build()))
    }
}
