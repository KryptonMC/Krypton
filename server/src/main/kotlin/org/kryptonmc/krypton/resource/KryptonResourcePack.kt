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
package org.kryptonmc.krypton.resource

import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.resource.ResourcePack
import java.net.URI

@JvmRecord
data class KryptonResourcePack(
    override val uri: URI,
    override val hash: String,
    override val isForced: Boolean,
    override val promptMessage: Component
) : ResourcePack {

    override fun send(player: Player) = player.sendResourcePack(this)

    object Factory : ResourcePack.Factory {

        override fun of(
            uri: URI,
            hash: String,
            isForced: Boolean,
            promptMessage: Component
        ): ResourcePack = KryptonResourcePack(uri, hash, isForced, promptMessage)
    }
}
