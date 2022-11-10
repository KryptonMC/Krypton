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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.world.block.state.BlockBehaviour.Properties
import org.kryptonmc.krypton.world.material.Materials

// TODO: Add the rest of these when we add the block implementations
object KryptonBlocks {

    @JvmField
    val AIR: KryptonBlock = register("air", AirBlock(Properties.of(Materials.AIR).noCollision().noLootTable().air()))

    @JvmStatic
    private fun register(key: String, block: KryptonBlock): KryptonBlock = KryptonRegistries.register(KryptonRegistries.BLOCK, Key.key(key), block)
}
