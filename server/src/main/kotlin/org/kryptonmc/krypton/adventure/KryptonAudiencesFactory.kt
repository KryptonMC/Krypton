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

import net.kyori.adventure.audience.Audience
import org.kryptonmc.api.adventure.Audiences
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.KryptonServer
import java.util.function.Predicate

class KryptonAudiencesFactory(private val server: KryptonServer) : Audiences.Factory {

    override fun server(): Audience = server

    override fun players(): Audience = PacketGroupingAudience.of(server.sessionManager, server.players)

    override fun players(predicate: Predicate<Player>): Audience = PacketGroupingAudience.of(
        server.sessionManager,
        server.players.filter { predicate.test(it) }
    )

    override fun console(): Audience = server.console
}
