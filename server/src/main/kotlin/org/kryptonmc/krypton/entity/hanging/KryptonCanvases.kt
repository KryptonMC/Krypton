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
package org.kryptonmc.krypton.entity.hanging

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries

object KryptonCanvases {

    val KEBAB = register("kebab", 16, 16)
    val AZTEC = register("aztec", 16, 16)
    val ALBAN = register("alban", 16, 16)
    val AZTEC2 = register("aztec2", 16, 16)
    val BOMB = register("bomb", 16, 16)
    val PLANT = register("plant", 16, 16)
    val WASTELAND = register("wasteland", 16, 16)
    val POOL = register("pool", 32, 16)
    val COURBET = register("courbet", 32, 16)
    val SEA = register("sea", 32, 16)
    val SUNSET = register("sunset", 32, 16)
    val CREEBET = register("creebet", 32, 16)
    val WANDERER = register("wanderer", 16, 32)
    val GRAHAM = register("graham", 16, 32)
    val MATCH = register("match", 32, 32)
    val BUST = register("bust", 32, 32)
    val STAGE = register("stage", 32, 32)
    val VOID = register("void", 32, 32)
    val SKULL_AND_ROSES = register("skull_and_roses", 32, 32)
    val WITHER = register("wither", 32, 32)
    val FIGHTERS = register("fighters", 64, 32)
    val POINTER = register("pointer", 64, 64)
    val PIGSCENE = register("pigscene", 64, 64)
    val BURNING_SKULL = register("burning_skull", 64, 64)
    val SKELETON = register("skeleton", 64, 48)
    val DONKEY_KONG = register("donkey_kong", 64, 48)

    private fun register(name: String, width: Int, height: Int): KryptonPicture {
        val key = Key.key(name)
        return Registries.register(InternalRegistries.CANVAS, key, KryptonPicture(key, width, height)) as KryptonPicture
    }
}
