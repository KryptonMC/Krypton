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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.api.service.provide

interface Vanishable : Player {

    override val isVanished: Boolean
        get() = vanishService().isVanished(this)

    override fun vanish() {
        vanishService().vanish(this)
    }

    override fun unvanish() {
        vanishService().unvanish(this)
    }

    override fun show(player: Player) {
        vanishService().show(this, player)
    }

    override fun hide(player: Player) {
        vanishService().hide(this, player)
    }

    override fun canSee(player: Player): Boolean = vanishService().canSee(this, player)

    private fun vanishService(): VanishService = server.servicesManager.provide()!!
}
