/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.item.handler

import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.material.Materials

object SwordHandler : ItemHandler {

    private const val COBWEB_DESTROY_SPEED = 15F
    private const val PLANT_LEAVES_VEGETABLE_DESTROY_SPEED = 1.5F
    private const val DEFAULT_DESTROY_SPEED = 1F

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlockState, pos: Vec3i): Boolean =
        player.gameMode != GameMode.CREATIVE

    override fun destroySpeed(item: KryptonItemStack, block: KryptonBlockState): Float {
        if (block.eq(KryptonBlocks.COBWEB)) return COBWEB_DESTROY_SPEED
        val material = block.material
        val isNotLeaves = !block.eq(BlockTags.LEAVES)
        if (material != Materials.PLANT && material != Materials.REPLACEABLE_PLANT && isNotLeaves && material != Materials.VEGETABLE) {
            return DEFAULT_DESTROY_SPEED
        }
        return PLANT_LEAVES_VEGETABLE_DESTROY_SPEED
    }

    override fun isCorrectTool(block: KryptonBlockState): Boolean = block.eq(KryptonBlocks.COBWEB)

    override fun mineBlock(player: KryptonPlayer, item: KryptonItemStack, world: KryptonWorld, block: KryptonBlockState, pos: Vec3i): Boolean {
        return true
    }
}
