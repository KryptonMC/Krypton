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
import org.kryptonmc.krypton.world.biome.layer.traits.BishopTransformer

object AddIslandLayer : BishopTransformer {

    override fun invoke(context: Context, sw: Int, se: Int, ne: Int, nw: Int, center: Int): Int {
        if (!center.isShallowOcean() || nw.isShallowOcean() && ne.isShallowOcean() && sw.isShallowOcean() && se.isShallowOcean()) {
            if (!center.isShallowOcean() && (nw.isShallowOcean() || sw.isShallowOcean() || ne.isShallowOcean() || se.isShallowOcean()) && context.nextRandom(5) == 0) {
                if (nw.isShallowOcean()) return if (center == 4) 4 else nw
                if (sw.isShallowOcean()) return if (center == 4) 4 else sw
                if (ne.isShallowOcean()) return if (center == 4) 4 else ne
                if (se.isShallowOcean()) return if (center == 4) 4 else se
            }
            return center
        }
        var i = 1
        var selected = 1
        if (!nw.isShallowOcean() && context.nextRandom(i++) == 0) selected = nw
        if (!ne.isShallowOcean() && context.nextRandom(i++) == 0) selected = ne
        if (!sw.isShallowOcean() && context.nextRandom(i++) == 0) selected = sw
        if (!se.isShallowOcean() && context.nextRandom(i++) == 0) selected = se
        return when {
            context.nextRandom(3) == 0 -> selected
            selected == 4 -> 4
            else -> selected
        }
    }
}
