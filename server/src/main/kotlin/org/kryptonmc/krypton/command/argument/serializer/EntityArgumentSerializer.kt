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
package org.kryptonmc.krypton.command.argument.serializer

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.arguments.entities.EntityArgumentType

/**
 * Entity argument types are serialized with flags indicating if the selector
 * has a single target, and if it only targets players.
 *
 * See [here](https://wiki.vg/Command_Data#minecraft:entity)
 */
object EntityArgumentSerializer : FlaggedArgumentSerializer<EntityArgumentType> {

    override fun read(buf: ByteBuf, flags: Int): EntityArgumentType = EntityArgumentType.from(flags and 1 != 0, flags and 2 != 0)

    override fun write(buf: ByteBuf, value: EntityArgumentType) {
        buf.writeByte(createFlags(value.singleTarget, value.onlyPlayers))
    }
}
