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
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.Direction
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.item.context.BlockPlaceContext
import org.kryptonmc.krypton.registry.Holder
import org.kryptonmc.krypton.registry.IntrusiveRegistryObject
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.shapes.BooleanOperator
import org.kryptonmc.krypton.shapes.Shapes
import org.kryptonmc.krypton.shapes.VoxelShape
import org.kryptonmc.krypton.state.StateDefinition
import org.kryptonmc.krypton.state.StateHolderDelegate
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.util.Keys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.components.WorldAccessor
import org.kryptonmc.krypton.world.WorldEvent
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.krypton.world.block.state.BlockBehaviour
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.SetBlockFlag
import org.kryptonmc.krypton.world.components.BlockGetter
import java.util.function.Function

@Suppress("LeakingThis")
@CataloguedBy(KryptonBlocks::class)
open class KryptonBlock(properties: Properties) : BlockBehaviour(properties), StateHolderDelegate<BlockState, KryptonBlockState>, IRO {

    final override val stateDefinition: StateDefinition<KryptonBlock, KryptonBlockState>
    private var defaultBlockState: KryptonBlockState
    private var descriptionId: String? = null
    private var item: KryptonItemType? = null
    private val defaultItemStack: KryptonItemStack by lazy { KryptonItemStack(this) }
    override val builtInRegistryHolder: Holder.Reference<KryptonBlock> = KryptonRegistries.BLOCK.createIntrusiveHolder(this)

    final override val defaultState: KryptonBlockState
        get() = defaultBlockState
    override val canRespawnIn: Boolean
        get() = !material.solid && !material.liquid

    init {
        val builder = StateDefinition.Builder<KryptonBlock, KryptonBlockState>(this)
        createStateDefinition(builder)
        stateDefinition = builder.build({ it.defaultBlockState }, { owner, values, codec -> KryptonBlockState(owner, values, codec) })
        defaultBlockState = stateDefinition.any()
    }

    open fun randomlyTicks(state: KryptonBlockState): Boolean = randomlyTicks

    open fun propagatesSkylightDown(state: KryptonBlockState, world: BlockGetter, pos: BlockPos): Boolean =
        !isShapeFullBlock(state.getShape(world, pos)) && state.asFluid().isEmpty

    open fun destroy(world: WorldAccessor, pos: BlockPos, state: KryptonBlockState) {
        // Do nothing by default
    }

    open fun stepOn(world: KryptonWorld, pos: BlockPos, state: KryptonBlockState, entity: KryptonEntity) {
        // Do nothing by default
    }

    open fun getPlacementState(context: BlockPlaceContext): KryptonBlockState? = defaultBlockState

    open fun playerDestroy(world: KryptonWorld, player: KryptonPlayer, pos: BlockPos, state: KryptonBlockState, entity: KryptonBlockEntity?,
                           tool: KryptonItemStack) {
        player.statisticsTracker.incrementStatistic(StatisticTypes.BLOCK_MINED.getStatistic(this))
        // TODO: Cause exhaustion and drop items
    }

    open fun setPlacedBy(world: KryptonWorld, pos: BlockPos, state: KryptonBlockState, entity: KryptonLivingEntity?, stack: KryptonItemStack) {
        // Do nothing by default
    }

    open fun fallOn(world: KryptonWorld, state: KryptonBlockState, pos: BlockPos, entity: KryptonEntity, fallDistance: Float) {
        // TODO: Cause fall damage to entity
    }

    open fun updateEntityAfterFallOn(world: BlockGetter, entity: KryptonEntity) {
        entity.velocity = entity.velocity.multiply(1.0, 0.0, 1.0)
    }

    open fun getItemStack(world: BlockGetter, pos: BlockPos, state: KryptonBlockState): KryptonItemStack = defaultItemStack

    open fun spawnDestroyParticles(world: KryptonWorld, player: KryptonPlayer, pos: BlockPos, state: KryptonBlockState) {
        world.worldEvent(pos, WorldEvent.DESTROY_BLOCK, idOf(state), player)
    }

    open fun playerWillDestroy(world: KryptonWorld, pos: BlockPos, state: KryptonBlockState, player: KryptonPlayer) {
        spawnDestroyParticles(world, player, pos, state)
        // TODO: Anger nearby piglins if state is guarded by piglins and trigger game event for sculk sensors
    }

    open fun handlePrecipitation(state: KryptonBlockState, world: KryptonWorld, pos: BlockPos, precipitation: Precipitation) {
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
        state.block.stateDefinition.properties.forEach { if (result.hasProperty(it)) result = copyProperty(state, result, it) }
        return result
    }

    protected fun getShapeForEachState(mapper: Function<KryptonBlockState, VoxelShape>): Map<KryptonBlockState, VoxelShape> =
        stateDefinition.states.associateWith(mapper::apply)

    override fun asItem(): KryptonItemType {
        if (item == null) item = KryptonItemType.fromBlock(this)
        return item!!
    }

    override fun asBlock(): KryptonBlock = this

    override fun key(): Key = KryptonRegistries.BLOCK.getKey(this)

    override fun translationKey(): String {
        if (descriptionId == null) descriptionId = Keys.translation("block", key())
        return descriptionId!!
    }

    override fun toString(): String = "KryptonBlock(${key()})"

    companion object {

        private const val BOX_FACTOR = 16.0
        @JvmField
        val STATES: IntHashBiMap<KryptonBlockState> = IntHashBiMap()
        private val SHAPE_FULL_BLOCK_CACHE = Caffeine.newBuilder()
            .maximumSize(512)
            .weakKeys()
            .build<VoxelShape, Boolean> { !Shapes.joinIsNotEmpty(Shapes.block(), it, BooleanOperator.NOT_SAME) }

        @JvmStatic
        fun stateFromId(id: Int): KryptonBlockState = STATES.get(id) ?: KryptonBlocks.AIR.defaultState

        @JvmStatic
        fun idOf(state: KryptonBlockState?): Int {
            if (state == null) return 0
            val id = STATES.getId(state)
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
        fun updateOrDestroy(oldState: KryptonBlockState, newState: KryptonBlockState, world: WorldAccessor, pos: BlockPos, flags: Int,
                            recursionLeft: Int) {
            if (oldState === newState) return
            if (oldState.isAir) {
                world.destroyBlock(pos, flags and SetBlockFlag.NEIGHBOUR_DROPS == 0, null, recursionLeft)
            } else {
                world.setBlock(pos, newState, flags and SetBlockFlag.NO_NEIGHBOUR_DROPS, recursionLeft)
            }
        }

        @JvmStatic
        private fun <T : Comparable<T>> copyProperty(result: KryptonBlockState, state: KryptonBlockState,
                                                     property: KryptonProperty<T>): KryptonBlockState {
            return state.setProperty(property, result.requireProperty(property))
        }
    }
}

private typealias IRO = IntrusiveRegistryObject<KryptonBlock>
