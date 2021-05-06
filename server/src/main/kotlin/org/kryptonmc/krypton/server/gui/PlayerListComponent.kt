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
package org.kryptonmc.krypton.server.gui

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.entities.KryptonPlayer
import java.util.Vector
import javax.swing.JList

class PlayerListComponent(private val server: KryptonServer) : JList<String>() {

    private var tickCount = 0

    init {
        server.tickables.add(::tick)
    }

    private fun tick() {
        if (tickCount++ % 20 != 0) return
        setListData(server.players.map(KryptonPlayer::name).toVector())
    }
}

private fun <T> List<T>.toVector(): Vector<T> = Vector(this)
