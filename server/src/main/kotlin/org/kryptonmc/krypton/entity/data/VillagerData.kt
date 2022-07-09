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
package org.kryptonmc.krypton.entity.data

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Various data for villagers. Used for entity metadata.
 */
@JvmRecord
data class VillagerData(val type: VillagerType, val profession: VillagerProfession, val level: Int) : Writable {

    constructor(buf: ByteBuf) : this(buf.readEnum(), buf.readEnum(), buf.readVarInt())

    override fun write(buf: ByteBuf) {
        buf.writeEnum(type)
        buf.writeEnum(profession)
        buf.writeVarInt(level)
    }
}
