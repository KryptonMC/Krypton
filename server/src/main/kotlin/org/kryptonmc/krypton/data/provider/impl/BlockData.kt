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
package org.kryptonmc.krypton.data.provider.impl

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.data.Key
import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import java.util.function.Function

object BlockData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.register(Keys.HARDNESS) { it.hardness }
        registrar.register(Keys.EXPLOSION_RESISTANCE) { it.explosionResistance }
        registrar.register(Keys.FRICTION) { it.friction }
        registrar.register(Keys.IS_SOLID) { it.isSolid }
        registrar.register(Keys.IS_LIQUID) { it.isLiquid }
        registrar.register(Keys.IS_FLAMMABLE) { it.isFlammable }
        registrar.register(Keys.IS_REPLACEABLE) { it.isReplaceable }
        registrar.register(Keys.IS_OPAQUE) { it.isOpaque }
        registrar.register(Keys.BLOCKS_MOTION) { it.blocksMotion }
        registrar.register(Keys.HAS_GRAVITY) { it.hasGravity }
        registrar.register(Keys.CAN_RESPAWN_IN) { it.canRespawnIn }
        registrar.register(Keys.PUSH_REACTION) { it.pushReaction }
        registrar.register(Keys.HAS_BLOCK_ENTITY) { it.hasBlockEntity }
    }
}

private fun <E> DataProviderRegistrar.register(key: Key<E>, getter: Function<Block, E?>) {
    registerImmutable<Block, E>(key) { get(getter) }
}
