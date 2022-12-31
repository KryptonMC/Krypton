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
package org.kryptonmc.krypton.state.property

import org.kryptonmc.api.block.meta.AttachFace
import org.kryptonmc.api.block.meta.BambooLeaves
import org.kryptonmc.api.block.meta.BedPart
import org.kryptonmc.api.block.meta.BellAttachment
import org.kryptonmc.api.block.meta.ChestType
import org.kryptonmc.api.block.meta.ComparatorMode
import org.kryptonmc.api.block.meta.DoorHingeSide
import org.kryptonmc.api.block.meta.DoubleBlockHalf
import org.kryptonmc.api.block.meta.DripstoneThickness
import org.kryptonmc.api.block.meta.Half
import org.kryptonmc.api.block.meta.NoteBlockInstrument
import org.kryptonmc.api.block.meta.Orientation
import org.kryptonmc.api.block.meta.PistonType
import org.kryptonmc.api.block.meta.RailShape
import org.kryptonmc.api.block.meta.RedstoneSide
import org.kryptonmc.api.block.meta.SculkSensorPhase
import org.kryptonmc.api.block.meta.SlabType
import org.kryptonmc.api.block.meta.StairShape
import org.kryptonmc.api.block.meta.StructureMode
import org.kryptonmc.api.block.meta.Tilt
import org.kryptonmc.api.block.meta.WallSide
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.enumhelper.Directions

@Suppress("StringLiteralDuplication")
object KryptonProperties {

    // ==============================
    // Boolean properties
    // ==============================

    @JvmField
    val ATTACHED: BooleanProperty = BooleanProperty("attached")
    @JvmField
    val BERRIES: BooleanProperty = BooleanProperty("berries")
    @JvmField
    val BLOOM: BooleanProperty = BooleanProperty("bloom")
    @JvmField
    val BOTTOM: BooleanProperty = BooleanProperty("bottom")
    @JvmField
    val CAN_SUMMON: BooleanProperty = BooleanProperty("can_summon")
    @JvmField
    val CONDITIONAL: BooleanProperty = BooleanProperty("conditional")
    @JvmField
    val DISARMED: BooleanProperty = BooleanProperty("disarmed")
    @JvmField
    val DOWN: BooleanProperty = BooleanProperty("down")
    @JvmField
    val DRAG: BooleanProperty = BooleanProperty("drag")
    @JvmField
    val EAST: BooleanProperty = BooleanProperty("east")
    @JvmField
    val ENABLED: BooleanProperty = BooleanProperty("enabled")
    @JvmField
    val EXTENDED: BooleanProperty = BooleanProperty("extended")
    @JvmField
    val EYE: BooleanProperty = BooleanProperty("eye")
    @JvmField
    val FALLING: BooleanProperty = BooleanProperty("falling")
    @JvmField
    val HANGING: BooleanProperty = BooleanProperty("hanging")
    @JvmField
    val HAS_BOOK: BooleanProperty = BooleanProperty("has_book")
    @JvmField
    val HAS_FIRST_BOTTLE: BooleanProperty = BooleanProperty("has_bottle_0")
    @JvmField
    val HAS_SECOND_BOTTLE: BooleanProperty = BooleanProperty("has_bottle_1")
    @JvmField
    val HAS_THIRD_BOTTLE: BooleanProperty = BooleanProperty("has_bottle_2")
    @JvmField
    val HAS_RECORD: BooleanProperty = BooleanProperty("has_record")
    @JvmField
    val INVERTED: BooleanProperty = BooleanProperty("inverted")
    @JvmField
    val IN_WALL: BooleanProperty = BooleanProperty("in_wall")
    @JvmField
    val LIT: BooleanProperty = BooleanProperty("lit")
    @JvmField
    val LOCKED: BooleanProperty = BooleanProperty("locked")
    @JvmField
    val NORTH: BooleanProperty = BooleanProperty("north")
    @JvmField
    val OCCUPIED: BooleanProperty = BooleanProperty("occupied")
    @JvmField
    val OPEN: BooleanProperty = BooleanProperty("open")
    @JvmField
    val PERSISTENT: BooleanProperty = BooleanProperty("persistent")
    @JvmField
    val POWERED: BooleanProperty = BooleanProperty("powered")
    @JvmField
    val SHORT: BooleanProperty = BooleanProperty("short")
    @JvmField
    val SHRIEKING: BooleanProperty = BooleanProperty("shrieking")
    @JvmField
    val SIGNAL_FIRE: BooleanProperty = BooleanProperty("signal_fire")
    @JvmField
    val SNOWY: BooleanProperty = BooleanProperty("snowy")
    @JvmField
    val SOUTH: BooleanProperty = BooleanProperty("south")
    @JvmField
    val TRIGGERED: BooleanProperty = BooleanProperty("triggered")
    @JvmField
    val UNSTABLE: BooleanProperty = BooleanProperty("unstable")
    @JvmField
    val UP: BooleanProperty = BooleanProperty("up")
    @JvmField
    val VINE_END: BooleanProperty = BooleanProperty("vine_end")
    @JvmField
    val WATERLOGGED: BooleanProperty = BooleanProperty("waterlogged")
    @JvmField
    val WEST: BooleanProperty = BooleanProperty("west")

    // ==============================
    // Integer properties
    // ==============================

    @JvmField
    val AGE_1: IntProperty = IntProperty("age", 0, 1)
    @JvmField
    val AGE_2: IntProperty = IntProperty("age", 0, 2)
    @JvmField
    val AGE_3: IntProperty = IntProperty("age", 0, 3)
    @JvmField
    val AGE_4: IntProperty = IntProperty("age", 0, 4)
    @JvmField
    val AGE_5: IntProperty = IntProperty("age", 0, 5)
    @JvmField
    val AGE_7: IntProperty = IntProperty("age", 0, 7)
    @JvmField
    val AGE_15: IntProperty = IntProperty("age", 0, 15)
    @JvmField
    val AGE_25: IntProperty = IntProperty("age", 0, 25)
    @JvmField
    val BITES: IntProperty = IntProperty("bites", 0, 6)
    @JvmField
    val CANDLES: IntProperty = IntProperty("candles", 1, 4)
    @JvmField
    val CAULDRON_LEVEL: IntProperty = IntProperty("level", 0, 3)
    @JvmField
    val COMPOSTER_LEVEL: IntProperty = IntProperty("level", 0, 8)
    @JvmField
    val DELAY: IntProperty = IntProperty("delay", 1, 4)
    @JvmField
    val DISTANCE: IntProperty = IntProperty("distance", 1, 7)
    @JvmField
    val EGGS: IntProperty = IntProperty("eggs", 1, 4)
    @JvmField
    val HATCH: IntProperty = IntProperty("hatch", 0, 2)
    @JvmField
    val HONEY_LEVEL: IntProperty = IntProperty("honey_level", 0, 5)
    @JvmField
    val LAYERS: IntProperty = IntProperty("layers", 1, 8)
    @JvmField
    val LEVEL: IntProperty = IntProperty("level", 0, 15)
    @JvmField
    val LIQUID_LEVEL: IntProperty = IntProperty("level", 1, 8)
    @JvmField
    val MOISTURE: IntProperty = IntProperty("moisture", 0, 7)
    @JvmField
    val NOTE: IntProperty = IntProperty("note", 0, 24)
    @JvmField
    val PICKLES: IntProperty = IntProperty("pickles", 1, 4)
    @JvmField
    val POWER: IntProperty = IntProperty("power", 0, 15)
    @JvmField
    val RESPAWN_ANCHOR_CHARGES: IntProperty = IntProperty("charges", 0, 4)
    @JvmField
    val ROTATION: IntProperty = IntProperty("rotation", 0, 15)
    @JvmField
    val SCAFFOLD_DISTANCE: IntProperty = IntProperty("distance", 0, 7)
    @JvmField
    val STAGE: IntProperty = IntProperty("stage", 0, 1)

    // ==============================
    // Enum properties
    // ==============================

    @JvmField
    val ATTACH_FACE: EnumProperty<AttachFace> = EnumProperty.create("face")
    @JvmField
    val AXIS: EnumProperty<Direction.Axis> = EnumProperty.create("axis")
    @JvmField
    val BAMBOO_LEAVES: EnumProperty<BambooLeaves> = EnumProperty.create("leaves")
    @JvmField
    val BED_PART: EnumProperty<BedPart> = EnumProperty.create("part")
    @JvmField
    val BELL_ATTACHMENT: EnumProperty<BellAttachment> = EnumProperty.create("attachment")
    @JvmField
    val CHEST_TYPE: EnumProperty<ChestType> = EnumProperty.create("type")
    @JvmField
    val COMPARATOR_MODE: EnumProperty<ComparatorMode> = EnumProperty.create("mode")
    @JvmField
    val DOOR_HINGE: EnumProperty<DoorHingeSide> = EnumProperty.create("hinge")
    @JvmField
    val DOUBLE_BLOCK_HALF: EnumProperty<DoubleBlockHalf> = EnumProperty.create("half")
    @JvmField
    val DRIPSTONE_THICKNESS: EnumProperty<DripstoneThickness> = EnumProperty.create("thickness")
    @JvmField
    val EAST_REDSTONE_SIDE: EnumProperty<RedstoneSide> = EnumProperty.create("east")
    @JvmField
    val EAST_WALL_SIDE: EnumProperty<WallSide> = EnumProperty.create("east")
    @JvmField
    val HALF: EnumProperty<Half> = EnumProperty.create("half")
    @JvmField
    val HORIZONTAL_AXIS: EnumProperty<Direction.Axis> = EnumProperty.create("axis", Direction.Axis.X, Direction.Axis.Z)
    @JvmField
    val INSTRUMENT: EnumProperty<NoteBlockInstrument> = EnumProperty.create("instrument")
    @JvmField
    val NORTH_REDSTONE_SIDE: EnumProperty<RedstoneSide> = EnumProperty.create("north")
    @JvmField
    val NORTH_WALL_SIDE: EnumProperty<WallSide> = EnumProperty.create("north")
    @JvmField
    val ORIENTATION: EnumProperty<Orientation> = EnumProperty.create("orientation")
    @JvmField
    val PISTON_TYPE: EnumProperty<PistonType> = EnumProperty.create("type")
    @JvmField
    val RAIL_SHAPE: EnumProperty<RailShape> = EnumProperty.create("shape")
    @JvmField
    val SCULK_SENSOR_PHASE: EnumProperty<SculkSensorPhase> = EnumProperty.create("sculk_sensor_phase")
    @JvmField
    val SLAB_TYPE: EnumProperty<SlabType> = EnumProperty.create("type")
    @JvmField
    val SOUTH_REDSTONE_SIDE: EnumProperty<RedstoneSide> = EnumProperty.create("south")
    @JvmField
    val SOUTH_WALL_SIDE: EnumProperty<WallSide> = EnumProperty.create("south")
    @JvmField
    val STAIR_SHAPE: EnumProperty<StairShape> = EnumProperty.create("shape")
    @JvmField
    val STRUCTURE_MODE: EnumProperty<StructureMode> = EnumProperty.create("mode")
    @JvmField
    val STRAIGHT_RAIL_SHAPE: EnumProperty<RailShape> = EnumProperty.create("shape") {
        it != RailShape.NORTH_EAST && it != RailShape.NORTH_WEST && it != RailShape.SOUTH_EAST && it != RailShape.SOUTH_WEST
    }
    @JvmField
    val TILT: EnumProperty<Tilt> = EnumProperty.create("tilt")
    @JvmField
    val WEST_REDSTONE_SIDE: EnumProperty<RedstoneSide> = EnumProperty.create("west")
    @JvmField
    val WEST_WALL_SIDE: EnumProperty<WallSide> = EnumProperty.create("west")

    // ==============================
    // Direction properties
    // ==============================

    @JvmField
    val FACING: DirectionProperty = DirectionProperty.create("facing")
    @JvmField
    val HOPPER_FACING: DirectionProperty = DirectionProperty.create("facing") { it != Direction.UP }
    @JvmField
    val HORIZONTAL_FACING: DirectionProperty = DirectionProperty.create("facing", Directions.Plane.HORIZONTAL)
    @JvmField
    val VERTICAL_DIRECTION: DirectionProperty = DirectionProperty.create("vertical_direction", Direction.UP, Direction.DOWN)
}
