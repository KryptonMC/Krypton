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
package org.kryptonmc.krypton.world.block.state

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockSoundGroup
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.krypton.coordinate.BlockPos
import org.kryptonmc.krypton.coordinate.KryptonVec3d
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.projectile.KryptonProjectile
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.context.BlockPlaceContext
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.shapes.collision.CollisionContext
import org.kryptonmc.krypton.state.KryptonState
import org.kryptonmc.krypton.state.StateDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.util.hit.BlockHitResult
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.util.math.Mirror
import org.kryptonmc.krypton.util.math.Rotation
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.data.BlockOffsetType
import org.kryptonmc.krypton.world.block.data.RenderShape
import org.kryptonmc.krypton.world.block.data.SupportType
import org.kryptonmc.krypton.world.components.BlockGetter
import org.kryptonmc.krypton.world.components.WorldAccessor
import org.kryptonmc.krypton.world.fluid.KryptonFluid
import org.kryptonmc.krypton.world.fluid.KryptonFluidState
import org.kryptonmc.krypton.world.material.Material
import org.kryptonmc.krypton.world.material.MaterialColor
import org.kryptonmc.serialization.Codec
import org.kryptonmc.serialization.MapCodec
import java.util.function.Predicate

@Suppress("UnusedPrivateMember")
class KryptonBlockState(
    owner: KryptonBlock,
    values: ImmutableMap<KryptonProperty<*>, Comparable<*>>,
    propertiesCodec: MapCodec<KryptonBlockState>
) : KryptonState<KryptonBlock, KryptonBlockState>(owner, values, propertiesCodec), BlockState, StateDelegate<BlockState, KryptonBlockState> {

    val lightEmission: Int
        get() = owner.propertiesProvider.getLightEmission(asState())
    val useShapeForLightOcclusion: Boolean = owner.propertiesProvider.useShapeForLightOcclusion(asState())
    val material: Material = owner.properties.material
    private val materialColor: MaterialColor
        get() = owner.propertiesProvider.getMaterialColor(asState())
    private val destroySpeed = owner.properties.destroyTime
    val requiresCorrectTool: Boolean = owner.properties.requiresCorrectTool
    val canOcclude: Boolean = owner.properties.canOcclude
    val offsetType: BlockOffsetType
        get() = owner.propertiesProvider.getOffsetType(asState())
    private var cache: Cache? = null

    override val isAir: Boolean
        get() = block.properties.isAir
    override val hardness: Double
        get() = owner.properties.destroyTime.toDouble()
    override val isOpaque: Boolean
        get() = canOcclude
    override val isSolid: Boolean
        get() = material.solid
    override val blocksMotion: Boolean
        get() = material.blocksMotion
    override val isFlammable: Boolean
        get() = material.flammable
    override val isLiquid: Boolean
        get() = material.liquid
    override val isReplaceable: Boolean
        get() = material.replaceable
    override val pushReaction: PushReaction
        get() = owner.propertiesProvider.getPushReaction(asState())

    override val block: KryptonBlock
        get() = owner

    // FIXME: When we create all the KryptonBlock implementations, check if is instance of EntityBlock
    @Suppress("FunctionOnlyReturningConstant")
    fun hasBlockEntity(): Boolean = false

    fun hasLargeCollisionShape(): Boolean = cache == null || cache!!.largeCollisionShape

    fun renderShape(): RenderShape = block.propertiesProvider.getRenderShape(asState())

    fun isSignalSource(): Boolean = block.redstoneDataProvider.isSignalSource(asState())

    fun hasAnalogOutputSignal(): Boolean = block.redstoneDataProvider.hasAnalogOutputSignal(asState())

    fun soundGroup(): BlockSoundGroup = block.properties.soundGroup

    fun randomlyTicks(): Boolean = block.propertiesProvider.randomlyTicks(asState())

    fun initCache() {
        if (!block.properties.hasDynamicShape) cache = Cache(asState())
    }

    fun isValidSpawn(world: BlockGetter, pos: BlockPos, type: KryptonEntityType<*>): Boolean =
        block.propertiesProvider.isValidSpawn(asState(), world, pos, type)

    fun propagatesSkylightDown(world: BlockGetter, pos: BlockPos): Boolean {
        return cache?.propagatesSkylightDown ?: block.propertiesProvider.propagatesSkylightDown(asState(), world, pos)
    }

    fun getLightBlock(world: BlockGetter, pos: BlockPos): Int {
        return cache?.lightBlock ?: block.propertiesProvider.getLightBlock(asState(), world, pos)
    }

    fun getMapColor(world: BlockGetter, pos: BlockPos): MaterialColor = materialColor

    fun rotate(rotation: Rotation): KryptonBlockState = block.handler.rotate(asState(), rotation)

    fun mirror(mirror: Mirror): KryptonBlockState = block.handler.mirror(asState(), mirror)

    fun isRedstoneConductor(world: BlockGetter, pos: BlockPos): Boolean = block.propertiesProvider.isRedstoneConductor(asState(), world, pos)

    fun getSignal(world: BlockGetter, pos: BlockPos, direction: Direction): Int {
        return block.redstoneDataProvider.getSignal(asState(), world, pos, direction)
    }

    fun getDirectSignal(world: BlockGetter, pos: BlockPos, direction: Direction): Int {
        return block.redstoneDataProvider.getDirectSignal(asState(), world, pos, direction)
    }

    fun getAnalogOutputSignal(world: KryptonWorld, pos: BlockPos): Int {
        return block.redstoneDataProvider.getAnalogOutputSignal(asState(), world, pos)
    }

    fun getDestroySpeed(world: BlockGetter, pos: BlockPos): Float = destroySpeed

    fun getDestroyProgress(player: KryptonPlayer, world: BlockGetter, pos: BlockPos): Float {
        return block.handler.getDestroyProgress(asState(), player, world, pos)
    }

    fun isSolidRender(world: BlockGetter, pos: BlockPos): Boolean {
        if (cache != null) return cache!!.solidRender
        val state = asState()
        return if (state.canOcclude) KryptonBlock.isShapeFullBlock(state.getOcclusionShape(world, pos)) else false
    }

    fun shouldSkipRendering(state: KryptonBlockState, face: Direction): Boolean {
        return block.propertiesProvider.shouldSkipRendering(asState(), state, face)
    }

    fun getShape(world: BlockGetter, pos: BlockPos): VoxelShape = getShape(world, pos, CollisionContext.empty())

    fun getShape(world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return block.shapesProvider.getShape(asState(), world, pos, context)
    }

    fun getFaceOcclusionShape(world: BlockGetter, pos: BlockPos, face: Direction): VoxelShape =
        cache?.occlusionShapes?.get(face.ordinal) ?: Shapes.faceShape(getOcclusionShape(world, pos), face)

    fun getOcclusionShape(world: BlockGetter, pos: BlockPos): VoxelShape {
        return block.shapesProvider.getOcclusionShape(asState(), world, pos)
    }

    fun getCollisionShape(world: BlockGetter, pos: BlockPos): VoxelShape {
        return cache?.collisionShape ?: getCollisionShape(world, pos, CollisionContext.empty())
    }

    fun getCollisionShape(world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return block.shapesProvider.getCollisionShape(asState(), world, pos, context)
    }

    fun getBlockSupportShape(world: BlockGetter, pos: BlockPos): VoxelShape {
        return block.shapesProvider.getBlockSupportShape(asState(), world, pos)
    }

    fun getVisualShape(world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return block.shapesProvider.getVisualShape(asState(), world, pos, context)
    }

    fun getInteractionShape(world: BlockGetter, pos: BlockPos): VoxelShape {
        return block.shapesProvider.getInteractionShape(asState(), world, pos)
    }

    fun entityCanStandOn(world: BlockGetter, pos: BlockPos, entity: KryptonEntity): Boolean {
        return entityCanStandOnFace(world, pos, entity, Direction.UP)
    }

    fun entityCanStandOnFace(world: BlockGetter, pos: BlockPos, entity: KryptonEntity, face: Direction): Boolean {
        return KryptonBlock.isFaceFull(getCollisionShape(world, pos, CollisionContext.of(entity)), face)
    }

    fun getOffset(world: BlockGetter, pos: BlockPos): Vec3d {
        if (offsetType == BlockOffsetType.NONE) return KryptonVec3d.ZERO

        val seed = Maths.getSeed(pos)
        val maxHorizontalOffset = block.propertiesProvider.maximumHorizontalOffset().toDouble()

        val offsetX = Maths.clamp(((seed and 15L) / 15F - 0.5) * 0.5, -maxHorizontalOffset, maxHorizontalOffset)
        val offsetY = if (offsetType == BlockOffsetType.XYZ) {
            val seededPart = seed shr 4 and 15L
            (seededPart / 15F - 1.0) * block.propertiesProvider.maximumVerticalOffset()
        } else {
            0.0
        }
        val offsetZ = Maths.clamp(((seed shr 8 and 15) / 15F - 0.5) * 0.5, -maxHorizontalOffset, maxHorizontalOffset)
        return KryptonVec3d(offsetX, offsetY, offsetZ)
    }

    fun triggerEvent(world: KryptonWorld, pos: BlockPos, id: Int, parameter: Int): Boolean {
        return block.handler.triggerEvent(asState(), world, pos, id, parameter)
    }

    fun neighbourChanged(world: KryptonWorld, pos: BlockPos, block: KryptonBlock, neighbourPos: BlockPos, moving: Boolean) {
        block.handler.neighbourChanged(asState(), world, pos, block, neighbourPos, moving)
    }

    fun updateNeighbourShapes(world: WorldAccessor, pos: BlockPos, flags: Int) {
        updateNeighbourShapes(world, pos, flags, 512)
    }

    fun updateNeighbourShapes(world: WorldAccessor, pos: BlockPos, flags: Int, recursionLeft: Int) {
        val currentPos = BlockPos.Mutable()
        UPDATE_SHAPE_ORDER.forEach {
            currentPos.setWithOffset(pos, it)
            world.neighbourShapeChanged(it.opposite, asState(), currentPos, pos, flags, recursionLeft)
        }
    }

    fun updateIndirectNeighbourShapes(world: WorldAccessor, pos: BlockPos, flags: Int) {
        updateIndirectNeighbourShapes(world, pos, flags, 512)
    }

    fun updateIndirectNeighbourShapes(world: WorldAccessor, pos: BlockPos, flags: Int, recursionLeft: Int) {
        block.handler.updateIndirectNeighbourShapes(asState(), world, pos, flags, recursionLeft)
    }

    fun onPlace(world: KryptonWorld, pos: BlockPos, old: KryptonBlockState, isMoving: Boolean) {
        block.handler.onPlace(asState(), world, pos, old, isMoving)
    }

    fun onRemove(world: KryptonWorld, pos: BlockPos, new: KryptonBlockState, isMoving: Boolean) {
        block.handler.onRemove(asState(), world, pos, new, isMoving)
    }

    fun entityInside(world: KryptonWorld, pos: BlockPos, entity: KryptonEntity) {
        block.handler.entityInside(asState(), world, pos, entity)
    }

    fun spawnAfterBreak(world: KryptonWorld, pos: BlockPos, item: KryptonItemStack, dropItems: Boolean) {
        block.handler.spawnAfterBreak(asState(), world, pos, item, dropItems)
    }

    fun use(world: KryptonWorld, player: KryptonPlayer, hand: Hand, hit: BlockHitResult): InteractionResult =
        block.handler.use(asState(), world, hit.position, player, hand, hit)

    fun attack(world: KryptonWorld, pos: BlockPos, player: KryptonPlayer) {
        block.handler.attack(asState(), world, pos, player)
    }

    fun isSuffocating(world: BlockGetter, pos: BlockPos): Boolean = block.propertiesProvider.isSuffocating(asState(), world, pos)

    fun updateShape(direction: Direction, neighbour: KryptonBlockState, world: BlockGetter,
                    currentPos: BlockPos, neighbourPos: BlockPos): KryptonBlockState {
        return block.handler.updateShape(world, asState(), currentPos, neighbour, neighbourPos, direction)
    }

    fun canBeReplaced(context: BlockPlaceContext): Boolean = block.propertiesProvider.canBeReplaced(asState(), context)

    fun canBeReplaced(fluid: KryptonFluid): Boolean = block.propertiesProvider.canBeReplaced(asState(), fluid)

    fun onProjectileHit(world: KryptonWorld, state: KryptonBlockState, hit: BlockHitResult, projectile: KryptonProjectile) {
        block.handler.onProjectileHit(world, state, hit, projectile)
    }

    fun isFaceSturdy(world: BlockGetter, pos: BlockPos, face: Direction): Boolean = isFaceSturdy(world, pos, face, SupportType.FULL)

    fun isFaceSturdy(world: BlockGetter, pos: BlockPos, face: Direction, type: SupportType): Boolean {
        return cache?.isFaceSturdy(face, type) ?: type.isSupporting(asState(), world, pos, face)
    }

    fun isCollisionShapeFullBlock(world: BlockGetter, pos: BlockPos): Boolean {
        return cache?.isCollisionShapeFullBlock ?: block.shapesProvider.isCollisionShapeFullBlock(asState(), world, pos)
    }

    @Suppress("UNCHECKED_CAST")
    fun eq(tag: TagKey<Block>): Boolean = block.builtInRegistryHolder.eq(tag as TagKey<KryptonBlock>)

    fun eq(tag: TagKey<Block>, predicate: Predicate<KryptonBlockState>): Boolean = eq(tag) && predicate.test(this)

    fun eq(block: Block): Boolean = this.block === block

    override fun asFluid(): KryptonFluidState = block.propertiesProvider.getFluidState(asState())

    override fun asState(): KryptonBlockState = this

    private class Cache(state: KryptonBlockState) {

        val solidRender: Boolean = state.isSolidRender(BlockGetter.Empty, BlockPos.ZERO)
        val propagatesSkylightDown: Boolean = state.block.propertiesProvider.propagatesSkylightDown(state, BlockGetter.Empty, BlockPos.ZERO)
        val lightBlock: Int = state.block.propertiesProvider.getLightBlock(state, BlockGetter.Empty, BlockPos.ZERO)
        val occlusionShapes: Array<VoxelShape>? = createOcclusionShapes(state)
        val collisionShape: VoxelShape =
            state.block.shapesProvider.getCollisionShape(state, BlockGetter.Empty, BlockPos.ZERO, CollisionContext.empty())
        val largeCollisionShape: Boolean
        private val faceSturdy = BooleanArray(DIRECTIONS.size * SUPPORT_TYPES.size)
        val isCollisionShapeFullBlock: Boolean

        init {
            if (!collisionShape.isEmpty() && state.offsetType != BlockOffsetType.NONE) {
                val key = KryptonRegistries.BLOCK.getKey(state.block)
                error("$key has a collision shape and an offset type, but is not marked as dynamic in its properties!")
            }
            largeCollisionShape = Direction.Axis.values().any { collisionShape.min(it) < 0.0 || collisionShape.max(it) > 1.0 }
            DIRECTIONS.forEach { direction ->
                SUPPORT_TYPES.forEach {
                    faceSturdy[getFaceSupportIndex(direction, it)] = it.isSupporting(state, BlockGetter.Empty, BlockPos.ZERO, direction)
                }
            }
            isCollisionShapeFullBlock = KryptonBlock.isShapeFullBlock(state.getCollisionShape(BlockGetter.Empty, BlockPos.ZERO))
        }

        fun isFaceSturdy(direction: Direction, type: SupportType): Boolean = faceSturdy[getFaceSupportIndex(direction, type)]

        companion object {

            private val DIRECTIONS = Direction.values()
            private val SUPPORT_TYPES = SupportType.values()

            @JvmStatic
            @Suppress("UNCHECKED_CAST")
            private fun createOcclusionShapes(state: KryptonBlockState): Array<VoxelShape>? {
                if (!state.canOcclude) return null

                val result = arrayOfNulls<VoxelShape>(DIRECTIONS.size)
                val shape = state.block.shapesProvider.getOcclusionShape(state, BlockGetter.Empty, BlockPos.ZERO)

                DIRECTIONS.forEach { result[it.ordinal] = Shapes.faceShape(shape, it) }
                return result as Array<VoxelShape>
            }

            @JvmStatic
            private fun getFaceSupportIndex(direction: Direction, type: SupportType): Int =
                direction.ordinal * SUPPORT_TYPES.size + type.ordinal
        }
    }

    companion object {

        private val UPDATE_SHAPE_ORDER = arrayOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP)
        @JvmField
        val CODEC: Codec<KryptonBlockState> = codec(KryptonRegistries.BLOCK.byNameCodec()) { it.defaultState }.stable()
    }
}
