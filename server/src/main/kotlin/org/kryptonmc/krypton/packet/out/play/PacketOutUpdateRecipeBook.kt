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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.entity.player.RecipeBookSettings
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readKey
import org.kryptonmc.krypton.util.readList
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeKey

@JvmRecord
data class PacketOutUpdateRecipeBook(
    val action: Action,
    val recipes: List<Key>,
    val toHighlight: List<Key>,
    val settings: RecipeBookSettings
) : Packet {

    constructor(buf: ByteBuf) : this(buf, buf.readEnum(), RecipeBookSettings.read(buf))

    private constructor(
        buf: ByteBuf,
        action: Action,
        settings: RecipeBookSettings
    ) : this(action, buf.readList(ByteBuf::readKey), if (action == Action.INIT) buf.readList(ByteBuf::readKey) else emptyList(), settings)

    override fun write(buf: ByteBuf) {
        buf.writeEnum(action)
        settings.write(buf)
        buf.writeCollection(recipes, buf::writeKey)
        if (action == Action.INIT) buf.writeCollection(toHighlight, buf::writeKey)
    }

    enum class Action {

        INIT,
        ADD,
        REMOVE;

        companion object {

            private val BY_ID = values()

            @JvmStatic
            fun fromId(id: Int): Action? = BY_ID.getOrNull(id)
        }
    }

    companion object {

        // TODO: Remove this when we actually bother with the recipe book
        @JvmField
        val CACHED_INIT: CachedPacket = CachedPacket { PacketOutUpdateRecipeBook(Action.INIT, emptyList(), emptyList(), RecipeBookSettings()) }
    }
}
