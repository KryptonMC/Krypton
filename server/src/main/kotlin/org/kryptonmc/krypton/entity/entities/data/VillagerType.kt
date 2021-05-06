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
package org.kryptonmc.krypton.entity.entities.data

import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * Types of villagers
 */
enum class VillagerType(val id: Int) {

    DESERT(0),
    JUNGLE(1),
    PLAINS(2),
    SAVANNA(3),
    SNOW(4),
    SWAMP(5),
    TAIGA(6);

    val key by lazy { NamespacedKey(value = name.lowercase()) }
}
