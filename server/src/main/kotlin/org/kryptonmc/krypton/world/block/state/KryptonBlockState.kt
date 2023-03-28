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
package org.kryptonmc.krypton.world.block.state

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.context.BlockPlaceContext
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.shapes.collision.CollisionContext
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.StateDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.data.BlockOffsetType
import org.kryptonmc.krypton.world.block.data.SupportType
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.components.WorldAccessor
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.material.Material
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import java.util.function.Predicate

@Suppress("UnusedPrivateMember")
class KryptonBlockState(
    owner: KryptonBlock,
    values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    propertiesCodec: MapCodec<KryptonBlockState>
) : KryptonState<KryptonBlock, KryptonBlockState>(owner, values, propertiesCodec), BlockState, StateDelegate<BlockState, KryptonBlockState> {

    val useShapeForLightOcclusion: Boolean = owner.propertiesProvider.useShapeForLightOcclusion(asState())
    val material: Material = owner.properties.material
    val requiresCorrectTool: Boolean = owner.properties.requiresCorrectTool
    private val canOcclude = owner.properties.canOcclude
    private var cache: Cache? = null

    override val hardness: Double
        get() = owner.properties.destroyTime.toDouble()
    override val pushReaction: PushReaction
        get() = owner.propertiesProvider.getPushReaction(asState())

    override val block: KryptonBlock
        get() = owner

    fun offsetType(): BlockOffsetType = owner.propertiesProvider.getOffsetType(asState())

    fun lightEmission(): Int = owner.propertiesProvider.getLightEmission(asState())

    override fun isAir(): Boolean = block.properties.isAir

    override fun isSolid(): Boolean = material.solid

    override fun isLiquid(): Boolean = material.liquid

    override fun isFlammable(): Boolean = material.flammable

    override fun isReplaceable(): Boolean = material.replaceable

    override fun isOpaque(): Boolean = canOcclude

    override fun blocksMotion(): Boolean = material.blocksMotion

    // FIXME: When we create all the KryptonBlock implementations, check if is instance of EntityBlock
    @Suppress("FunctionOnlyReturningConstant")
    fun hasBlockEntity(): Boolean = false

    fun hasAnalogOutputSignal(): Boolean = block.redstoneDataProvider.hasAnalogOutputSignal(asState())

    fun initCache() {
        if (!block.properties.hasDynamicShape) cache = Cache(asState())
    }

    fun propagatesSkylightDown(world: BlockGetter, pos: Vec3i): Boolean {
        return cache?.propagatesSkylightDown ?: block.propertiesProvider.propagatesSkylightDown(asState(), world, pos)
    }

    fun getLightBlock(world: BlockGetter, pos: Vec3i): Int {
        return cache?.lightBlock ?: block.propertiesProvider.getLightBlock(asState(), world, pos)
    }

    fun isRedstoneConductor(world: BlockGetter, pos: Vec3i): Boolean = block.propertiesProvider.isRedstoneConductor(asState(), world, pos)

    fun getDestroyProgress(player: KryptonPlayer, world: BlockGetter, pos: Vec3i): Float {
        return block.handler.getDestroyProgress(asState(), player, world, pos)
    }

    fun isSolidRender(world: BlockGetter, pos: Vec3i): Boolean {
        if (cache != null) return cache!!.solidRender
        val state = asState()
        return if (state.canOcclude) KryptonBlock.isShapeFullBlock(state.getOcclusionShape(world, pos)) else false
    }

    fun getShape(world: BlockGetter, pos: Vec3i): VoxelShape = getShape(world, pos, CollisionContext.empty())

    private fun getShape(world: BlockGetter, pos: Vec3i, context: CollisionContext): VoxelShape {
        return block.shapesProvider.getShape(asState(), world, pos, context)
    }

    fun getOcclusionShape(world: BlockGetter, pos: Vec3i): VoxelShape {
        return block.shapesProvider.getOcclusionShape(asState(), world, pos)
    }

    fun getCollisionShape(world: BlockGetter, pos: Vec3i): VoxelShape {
        return cache?.collisionShape ?: getCollisionShape(world, pos, CollisionContext.empty())
    }

    private fun getCollisionShape(world: BlockGetter, pos: Vec3i, context: CollisionContext): VoxelShape {
        return block.shapesProvider.getCollisionShape(asState(), world, pos, context)
    }

    fun getBlockSupportShape(world: BlockGetter, pos: Vec3i): VoxelShape {
        return block.shapesProvider.getBlockSupportShape(asState(), world, pos)
    }

    fun neighbourChanged(world: KryptonWorld, pos: Vec3i, block: KryptonBlock, neighbourPos: Vec3i, moving: Boolean) {
        block.handler.neighbourChanged(asState(), world, pos, block, neighbourPos, moving)
    }

    fun updateNeighbourShapes(world: WorldAccessor, pos: Vec3i, flags: Int, recursionLeft: Int) {
        UPDATE_SHAPE_ORDER.forEach {
            val currentPos = Vec3i(pos.x + it.normalX, pos.y + it.normalY, pos.z + it.normalZ)
            world.neighbourShapeChanged(it.opposite, asState(), currentPos, pos, flags, recursionLeft)
        }
    }

    fun updateIndirectNeighbourShapes(world: WorldAccessor, pos: Vec3i, flags: Int, recursionLeft: Int) {
        block.handler.updateIndirectNeighbourShapes(asState(), world, pos, flags, recursionLeft)
    }

    fun use(world: KryptonWorld, player: KryptonPlayer, hand: Hand, hit: BlockHitResult): InteractionResult =
        block.handler.use(asState(), world, hit.position, player, hand, hit)

    fun attack(world: KryptonWorld, pos: Vec3i, player: KryptonPlayer) {
        block.handler.attack(asState(), world, pos, player)
    }

    fun updateShape(direction: Direction, neighbour: KryptonBlockState, world: BlockGetter,
                    currentPos: Vec3i, neighbourPos: Vec3i): KryptonBlockState {
        return block.handler.updateShape(world, asState(), currentPos, neighbour, neighbourPos, direction)
    }

    fun canBeReplaced(context: BlockPlaceContext): Boolean = block.propertiesProvider.canBeReplaced(asState(), context)

    fun isFaceSturdy(world: BlockGetter, pos: Vec3i, face: Direction): Boolean = isFaceSturdy(world, pos, face, SupportType.FULL)

    private fun isFaceSturdy(world: BlockGetter, pos: Vec3i, face: Direction, type: SupportType): Boolean {
        return cache?.isFaceSturdy(face, type) ?: type.isSupporting(asState(), world, pos, face)
    }

    fun isCollisionShapeFullBlock(world: BlockGetter, pos: Vec3i): Boolean {
        return cache?.isCollisionShapeFullBlock ?: block.shapesProvider.isCollisionShapeFullBlock(asState(), world, pos)
    }

    @Suppress("UNCHECKED_CAST")
    fun eq(tag: TagKey<Block>): Boolean = block.builtInRegistryHolder.eq(tag as TagKey<KryptonBlock>)

    fun eq(tag: TagKey<Block>, predicate: Predicate<KryptonBlockState>): Boolean = eq(tag) && predicate.test(this)

    fun eq(block: Block): Boolean = this.block === block

    override fun asFluid(): KryptonFluidState = block.propertiesProvider.getFluidState(asState())

    override fun asState(): KryptonBlockState = this

    private class Cache(state: KryptonBlockState) {

        val solidRender: Boolean = state.isSolidRender(BlockGetter.Empty, Vec3i.ZERO)
        val propagatesSkylightDown: Boolean = state.block.propertiesProvider.propagatesSkylightDown(state, BlockGetter.Empty, Vec3i.ZERO)
        val lightBlock: Int = state.block.propertiesProvider.getLightBlock(state, BlockGetter.Empty, Vec3i.ZERO)
        val collisionShape: VoxelShape =
            state.block.shapesProvider.getCollisionShape(state, BlockGetter.Empty, Vec3i.ZERO, CollisionContext.empty())
        val largeCollisionShape: Boolean
        private val faceSturdy = BooleanArray(DIRECTIONS.size * SUPPORT_TYPES.size)
        val isCollisionShapeFullBlock: Boolean

        init {
            if (!collisionShape.isEmpty() && state.offsetType() != BlockOffsetType.NONE) {
                val key = KryptonRegistries.BLOCK.getKey(state.block)
                error("$key has a collision shape and an offset type, but is not marked as dynamic in its properties!")
            }
            largeCollisionShape = Direction.Axis.values().any { collisionShape.min(it) < 0.0 || collisionShape.max(it) > 1.0 }
            DIRECTIONS.forEach { direction ->
                SUPPORT_TYPES.forEach {
                    faceSturdy[getFaceSupportIndex(direction, it)] = it.isSupporting(state, BlockGetter.Empty, Vec3i.ZERO, direction)
                }
            }
            isCollisionShapeFullBlock = KryptonBlock.isShapeFullBlock(state.getCollisionShape(BlockGetter.Empty, Vec3i.ZERO))
        }

        fun isFaceSturdy(direction: Direction, type: SupportType): Boolean = faceSturdy[getFaceSupportIndex(direction, type)]

        companion object {

            private val DIRECTIONS = Direction.values()
            private val SUPPORT_TYPES = SupportType.values()

            @JvmStatic
            private fun getFaceSupportIndex(direction: Direction, type: SupportType): Int = direction.ordinal * SUPPORT_TYPES.size + type.ordinal
        }
    }

    companion object {

        private val UPDATE_SHAPE_ORDER = arrayOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP)
        @JvmField
        val CODEC: Codec<KryptonBlockState> = codec(KryptonRegistries.BLOCK.byNameCodec()) { it.defaultState }.stable()
    }
}
