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
package org.kryptonmc.krypton.world.biome.layer

import org.kryptonmc.krypton.world.biome.context.Context
import org.kryptonmc.krypton.world.biome.layer.traits.AreaTransformer0

object IslandLayer : AreaTransformer0 {

    override fun invoke(context: Context, x: Int, z: Int): Int {
        if (x == 0 && z == 0) return 1
        return if (context.nextRandom(10) == 0) 1 else 0
    }
}
