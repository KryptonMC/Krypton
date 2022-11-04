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

import com.github.benmanes.caffeine.cache.Caffeine
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.item.context.BlockPlaceContext
import org.kryptonmc.krypton.shapes.BooleanOperator
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.StateHolderDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.BlockAccessor
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.WorldAccessor
import org.kryptonmc.krypton.world.WorldEvent
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.BlockBehaviour
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.spongepowered.math.vector.Vector3i
import java.util.function.Function

@Suppress("LeakingThis")
open class KryptonBlock(properties: Properties) : BlockBehaviour(properties), StateHolderDelegate<BlockState, KryptonBlockState> {

    final override val stateDefinition: StateDefinition<KryptonBlock, KryptonBlockState>
    private var defaultBlockState: KryptonBlockState
    private var descriptionId: String? = null
    private var item: KryptonItemType? = null
    private val defaultItemStack: KryptonItemStack by lazy { KryptonItemStack(this) }

    final override val defaultState: KryptonBlockState
        get() = defaultBlockState
    override val canRespawnIn: Boolean
        get() = !material.solid && !material.liquid

    init {
        val builder = StateDefinition.Builder<KryptonBlock, KryptonBlockState>(this)
        createStateDefinition(builder)
        stateDefinition = builder.build(KryptonBlock::defaultBlockState, ::KryptonBlockState)
        defaultBlockState = stateDefinition.any()
    }

    open fun randomlyTicks(state: KryptonBlockState): Boolean = randomlyTicks

    open fun propagatesSkylightDown(state: KryptonBlockState, world: BlockAccessor, x: Int, y: Int, z: Int): Boolean =
        !isShapeFullBlock(state.getShape(world, x, y, z)) && state.asFluid().isEmpty

    open fun destroy(world: WorldAccessor, position: Vector3i, state: KryptonBlockState) {
        // Do nothing by default
    }

    open fun stepOn(world: KryptonWorld, x: Int, y: Int, z: Int, state: KryptonBlockState, entity: KryptonEntity) {
        // Do nothing by default
    }

    open fun getPlacementState(context: BlockPlaceContext): KryptonBlockState? = defaultBlockState

    open fun playerDestroy(
        world: KryptonWorld,
        player: KryptonPlayer,
        position: Vector3i,
        state: KryptonBlockState,
        entity: KryptonBlockEntity?,
        tool: KryptonItemStack
    ) {
        player.statistics.increment(StatisticTypes.BLOCK_MINED.get(this))
        // TODO: Cause exhaustion and drop items
    }

    open fun setPlacedBy(
        world: KryptonWorld,
        x: Int,
        y: Int,
        z: Int,
        state: KryptonBlockState,
        entity: KryptonLivingEntity?,
        stack: KryptonItemStack
    ) {
        // Do nothing by default
    }

    open fun fallOn(world: KryptonWorld, state: KryptonBlockState, x: Int, y: Int, z: Int, entity: KryptonEntity, fallDistance: Float) {
        // TODO: Cause fall damage to entity
    }

    open fun updateEntityAfterFallOn(world: BlockAccessor, entity: KryptonEntity) {
        entity.velocity = entity.velocity.mul(1.0, 0.0, 1.0)
    }

    open fun getItemStack(world: BlockAccessor, x: Int, y: Int, z: Int, state: KryptonBlockState): KryptonItemStack = defaultItemStack

    open fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, position: Vector3i, state: KryptonBlockState) {
        world.worldEvent(WorldEvent.DESTROY_BLOCK, position.x(), position.y(), position.z(), idOf(state), player)
    }

    open fun playerWillDestroy(world: KryptonWorld, position: Vector3i, state: KryptonBlockState, player: KryptonPlayer) {
        spawnDestroyParticles(world, player, position, state)
        // TODO: Anger nearby piglins if state is guarded by piglins and trigger game event for sculk sensors
    }

    open fun handlePrecipitation(state: KryptonBlockState, world: KryptonWorld, x: Int, y: Int, z: Int, precipitation: Precipitation) {
        // Do nothing by default
    }

    protected open fun createStateDefinition(builder: StateDefinition.Builder<KryptonBlock, KryptonBlockState>) {
        // Do nothing by default
    }

    protected fun registerDefaultState(state: KryptonBlockState) {
        defaultBlockState = state
    }

    fun withPropertiesOf(state: KryptonBlockState): KryptonBlockState {
        var result = defaultBlockState
        state.block.stateDefinition.properties.forEach { if (result.contains(it)) result = copyProperty(state, result, it) }
        return result
    }

    protected fun getShapeForEachState(mapper: Function<KryptonBlockState, VoxelShape>): Map<KryptonBlockState, VoxelShape> =
        stateDefinition.states.associateWith(mapper::apply)

    override fun asItem(): KryptonItemType {
        if (item == null) item = KryptonItemType.fromBlock(this)
        return item!!
    }

    override fun asBlock(): KryptonBlock = this

    override fun key(): Key = Registries.BLOCK.get(this)

    override fun translationKey(): String {
        if (descriptionId == null) descriptionId = Keys.translation("block", key())
        return descriptionId!!
    }

    override fun toString(): String = "Block(${key()})"

    companion object {

        private const val BOX_FACTOR = 16.0
        @JvmField
        val STATES: IntHashBiMap<KryptonBlockState> = IntHashBiMap()
        private val SHAPE_FULL_BLOCK_CACHE = Caffeine.newBuilder()
            .maximumSize(512)
            .weakKeys()
            .build<VoxelShape, Boolean> { !Shapes.joinIsNotEmpty(Shapes.block(), it, BooleanOperator.NOT_SAME) }

        @JvmStatic
        fun stateFromId(id: Int): KryptonBlockState = STATES.get(id) ?: Blocks.AIR.defaultState.downcast()

        @JvmStatic
        fun idOf(state: KryptonBlockState?): Int {
            if (state == null) return 0
            val id = STATES.idOf(state)
            return if (id == -1) 0 else id
        }

        @JvmStatic
        fun box(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): VoxelShape =
            Shapes.box(minX / BOX_FACTOR, minY / BOX_FACTOR, minZ / BOX_FACTOR, maxX / BOX_FACTOR, maxY / BOX_FACTOR, maxZ / BOX_FACTOR)

        @JvmStatic
        fun isFaceFull(shape: VoxelShape, face: Direction): Boolean = isShapeFullBlock(shape.getFaceShape(face))

        @JvmStatic
        fun isShapeFullBlock(shape: VoxelShape): Boolean = SHAPE_FULL_BLOCK_CACHE.get(shape)

        @JvmStatic
        private fun <T : Comparable<T>> copyProperty(
            result: KryptonBlockState,
            state: KryptonBlockState,
            property: KryptonProperty<T>
        ): KryptonBlockState = state.set(property, result.require(property))
    }
}
