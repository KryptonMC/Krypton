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
package org.kryptonmc.krypton

import org.kryptonmc.api.world.GameVersion
import java.util.concurrent.atomic.AtomicInteger

object ServerStorage {

    val PLAYER_COUNT = AtomicInteger(0)
    val NEXT_ENTITY_ID = AtomicInteger(0)
}

object ServerInfo {

    const val PROTOCOL = 756
    const val WORLD_VERSION = 2730
    const val PACK_VERSION = 7
    val GAME_VERSION = GameVersion(WORLD_VERSION, KryptonServer.KryptonServerInfo.minecraftVersion, false)
}
