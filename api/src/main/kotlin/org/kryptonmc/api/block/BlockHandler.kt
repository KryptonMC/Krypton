/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.util.InteractionResult
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

interface BlockHandler {

    fun getDestroyProgress(player: Player, world: World, block: Block, position: Vector3i): Float

    fun onPlace(player: Player, block: Block, position: Vector3i, face: BlockFace)

    fun onDestroy(player: Player, block: Block, position: Vector3i, item: ItemStack)

    fun interact(player: Player, world: World, block: Block, position: Vector3i, hand: Hand): InteractionResult

    fun attack(player: Player, world: World, block: Block, position: Vector3i)
}
