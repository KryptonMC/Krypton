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
package org.kryptonmc.krypton.entity.ai.pathfinding

import com.extollit.gaming.ai.path.model.IBlockObject
import com.extollit.linalg.immutable.AxisAlignedBBox
import it.unimi.dsi.fastutil.shorts.Short2ObjectFunction
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.components.BlockGetter
import space.vectrix.flare.fastutil.Short2ObjectSyncMap

class KryptonHydrazineBlock(private val block: KryptonBlockState) : IBlockObject {

    override fun bounds(): AxisAlignedBBox {
        val box = block.getCollisionShape(BlockGetter.Empty, Vec3i.ZERO).bounds()
        return AxisAlignedBBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    override fun isFenceLike(): Boolean = block.eq(BlockTags.FENCES) || block.eq(BlockTags.FENCE_GATES) || block.eq(BlockTags.WALLS)

    override fun isClimbable(): Boolean = block.eq(BlockTags.CLIMBABLE)

    override fun isDoor(): Boolean = block.eq(BlockTags.DOORS)

    override fun isIntractable(): Boolean = false

    override fun isImpeding(): Boolean = block.isSolid()

    override fun isFullyBounded(): Boolean = block.isCollisionShapeFullBlock(BlockGetter.Empty, Vec3i.ZERO)

    override fun isLiquid(): Boolean = block.isLiquid()

    override fun isIncinerating(): Boolean = block.eq(KryptonBlocks.LAVA) || block.eq(BlockTags.FIRE)

    companion object {

        private val BY_ID = Short2ObjectSyncMap.hashmap<KryptonHydrazineBlock>()

        @JvmStatic
        fun get(block: KryptonBlockState): KryptonHydrazineBlock {
            return BY_ID.computeIfAbsent(KryptonBlock.idOf(block).toShort(), Short2ObjectFunction { KryptonHydrazineBlock(block) })
        }
    }
}
