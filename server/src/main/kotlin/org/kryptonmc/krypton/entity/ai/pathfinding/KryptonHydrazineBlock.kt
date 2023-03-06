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
