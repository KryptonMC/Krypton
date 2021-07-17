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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutUnlockRecipes(
    private val action: UnlockRecipesAction,
    private val isCraftingBookOpen: Boolean = false,
    private val isCraftingBookFiltered: Boolean = false,
    private val isSmeltingBookOpen: Boolean = false,
    private val isSmeltingBookFiltered: Boolean = false,
    private val isBlastFurnaceBookOpen: Boolean = false,
    private val isBlastFurnaceBookFiltered: Boolean = false,
    private val isSmokerBookOpen: Boolean = false,
    private val isSmokerBookFiltered: Boolean = false,
    private val recipes: List<Key> = emptyList(),
    private val newRecipes: List<Key> = emptyList()
) : PlayPacket(0x39) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.ordinal)

        buf.writeBoolean(isCraftingBookOpen)
        buf.writeBoolean(isCraftingBookFiltered)
        buf.writeBoolean(isSmeltingBookOpen)
        buf.writeBoolean(isSmeltingBookFiltered)
        buf.writeBoolean(isBlastFurnaceBookOpen)
        buf.writeBoolean(isBlastFurnaceBookFiltered)
        buf.writeBoolean(isSmokerBookOpen)
        buf.writeBoolean(isSmokerBookFiltered)

        buf.writeVarInt(recipes.size)
        for (recipe in recipes) buf.writeKey(recipe)

        if (action == UnlockRecipesAction.INIT) {
            buf.writeVarInt(newRecipes.size)
            for (recipe in newRecipes) buf.writeKey(recipe)
        }
    }
}

enum class UnlockRecipesAction {

    INIT,
    ADD,
    REMOVE
}
