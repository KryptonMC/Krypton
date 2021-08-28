/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.property

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
import org.kryptonmc.api.space.Direction

/**
 * All built-in block state properties.
 *
 * See [here](https://minecraft.fandom.com/wiki/Java_Edition_data_values#Block_states)
 * for information on what types of blocks each property is applicable to.
 */
@Suppress("StringLiteralDuplication", "MaxLineLength")
object Properties {

    /**
     * Boolean properties.
     */
    @JvmField val ATTACHED = Property.forBoolean("attached")
    @JvmField val BERRIES = Property.forBoolean("berries")
    @JvmField val BOTTOM = Property.forBoolean("bottom")
    @JvmField val CONDITIONAL = Property.forBoolean("conditional")
    @JvmField val DISARMED = Property.forBoolean("disarmed")
    @JvmField val DOWN = Property.forBoolean("down")
    @JvmField val DRAG = Property.forBoolean("drag")
    @JvmField val EAST = Property.forBoolean("east")
    @JvmField val ENABLED = Property.forBoolean("enabled")
    @JvmField val EXTENDED = Property.forBoolean("extended")
    @JvmField val EYE = Property.forBoolean("eye")
    @JvmField val FALLING = Property.forBoolean("falling")
    @JvmField val HANGING = Property.forBoolean("hanging")
    @JvmField val HAS_BOOK = Property.forBoolean("has_book")
    @JvmField val HAS_FIRST_BOTTLE = Property.forBoolean("has_bottle_0")
    @JvmField val HAS_SECOND_BOTTLE = Property.forBoolean("has_bottle_1")
    @JvmField val HAS_THIRD_BOTTLE = Property.forBoolean("has_bottle_2")
    @JvmField val HAS_RECORD = Property.forBoolean("has_record")
    @JvmField val IN_WALL = Property.forBoolean("in_wall")
    @JvmField val INVERTED = Property.forBoolean("inverted")
    @JvmField val LIT = Property.forBoolean("lit")
    @JvmField val LOCKED = Property.forBoolean("locked")
    @JvmField val NORTH = Property.forBoolean("north")
    @JvmField val OCCUPIED = Property.forBoolean("occupied")
    @JvmField val OPEN = Property.forBoolean("open")
    @JvmField val PERSISTENT = Property.forBoolean("persistent")
    @JvmField val POWERED = Property.forBoolean("powered")
    @JvmField val SHORT = Property.forBoolean("short")
    @JvmField val SIGNAL_FIRE = Property.forBoolean("signal_fire")
    @JvmField val SNOWY = Property.forBoolean("snowy")
    @JvmField val SOUTH = Property.forBoolean("south")
    @JvmField val TRIGGERED = Property.forBoolean("triggered")
    @JvmField val UNSTABLE = Property.forBoolean("unstable")
    @JvmField val UP = Property.forBoolean("up")
    @JvmField val VINE_END = Property.forBoolean("vine_end")
    @JvmField val WATERLOGGED = Property.forBoolean("waterlogged")
    @JvmField val WEST = Property.forBoolean("west")

    /**
     * Integer properties.
     */
    @JvmField val AGE_1 = Property.forInt("age", 0..1)
    @JvmField val AGE_2 = Property.forInt("age", 0..2)
    @JvmField val AGE_3 = Property.forInt("age", 0..3)
    @JvmField val AGE_5 = Property.forInt("age", 0..5)
    @JvmField val AGE_7 = Property.forInt("age", 0..7)
    @JvmField val AGE_15 = Property.forInt("age", 0..15)
    @JvmField val AGE_25 = Property.forInt("age", 0..25)
    @JvmField val BITES = Property.forInt("bites", 0..6)
    @JvmField val CANDLES = Property.forInt("candles", 1..4)
    @JvmField val CHARGES = Property.forInt("charges", 0..4)
    @JvmField val DELAY = Property.forInt("delay", 1..4)
    @JvmField val DISTANCE = Property.forInt("distance", 1..7)
    @JvmField val SCAFFOLD_DISTANCE = Property.forInt("distance", 0..7)
    @JvmField val EGGS = Property.forInt("eggs", 1..4)
    @JvmField val HATCH = Property.forInt("hatch", 0..2)
    @JvmField val LAYERS = Property.forInt("layers", 1..8)
    @JvmField val CAULDRON_LEVEL = Property.forInt("level", 0..3)
    @JvmField val COMPOSTER_LEVEL = Property.forInt("level", 0..8)
    @JvmField val HONEY_LEVEL = Property.forInt("level", 0..5)
    @JvmField val LIQUID_LEVEL = Property.forInt("level", 0..8)
    @JvmField val LEVEL = Property.forInt("level", 0..15)
    @JvmField val MOISTURE = Property.forInt("moisture", 0..7)
    @JvmField val NOTE = Property.forInt("note", 0..24)
    @JvmField val PICKLES = Property.forInt("pickles", 1..4)
    @JvmField val POWER = Property.forInt("power", 0..15)
    @JvmField val ROTATION = Property.forInt("rotation", 0..15)
    @JvmField val STAGE = Property.forInt("stage", 0..1)

    /**
     * Enum properties.
     */
    @JvmField val BELL_ATTACHMENT = Property.forEnum("attachment", BellAttachment.values())
    @JvmField val AXIS = Property.forEnum("axis", Direction.Axis.values())
    @JvmField val HORIZONTAL_AXIS = Property.forEnum("axis", setOf(Direction.Axis.X, Direction.Axis.Z))
    @JvmField val REDSTONE_EAST = Property.forEnum("east", RedstoneSide.values())
    @JvmField val WALL_EAST = Property.forEnum("east", WallSide.values())
    @JvmField val ATTACH_FACE = Property.forEnum("face", AttachFace.values())
    @JvmField val FACING = Property.forEnum("facing", Direction.values())
    @JvmField val HORIZONTAL_FACING = Property.forEnum("facing", setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
    @JvmField val HOPPER_FACING = Property.forEnum("facing", setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN))
    @JvmField val DOUBLE_BLOCK_HALF = Property.forEnum("half", DoubleBlockHalf.values())
    @JvmField val HALF = Property.forEnum("half", Half.values())
    @JvmField val DOOR_HINGE = Property.forEnum("hinge", DoorHingeSide.values())
    @JvmField val INSTRUMENT = Property.forEnum("instrument", NoteBlockInstrument.values())
    @JvmField val BAMBOO_LEAVES = Property.forEnum("leaves", BambooLeaves.values())
    @JvmField val COMPARATOR_MODE = Property.forEnum("mode", ComparatorMode.values())
    @JvmField val STRUCTURE_MODE = Property.forEnum("mode", StructureMode.values())
    @JvmField val REDSTONE_NORTH = Property.forEnum("north", RedstoneSide.values())
    @JvmField val WALL_NORTH = Property.forEnum("north", WallSide.values())
    @JvmField val ORIENTATION = Property.forEnum("orientation", Orientation.values())
    @JvmField val BED_PART = Property.forEnum("part", BedPart.values())
    @JvmField val SCULK_SENSOR_PHASE = Property.forEnum("sculk_sensor_phase", SculkSensorPhase.values())
    @JvmField val RAIL_SHAPE = Property.forEnum("shape", RailShape.values())
    @JvmField val STRAIGHT_RAIL_SHAPE = Property.forEnum("shape", setOf(RailShape.ASCENDING_NORTH, RailShape.ASCENDING_SOUTH, RailShape.ASCENDING_EAST, RailShape.ASCENDING_WEST, RailShape.EAST_WEST, RailShape.NORTH_SOUTH))
    @JvmField val STAIR_SHAPE = Property.forEnum("shape", StairShape.values())
    @JvmField val REDSTONE_SOUTH = Property.forEnum("south", RedstoneSide.values())
    @JvmField val WALL_SOUTH = Property.forEnum("south", WallSide.values())
    @JvmField val DRIPSTONE_THICKNESS = Property.forEnum("thickness", DripstoneThickness.values())
    @JvmField val TILT = Property.forEnum("tilt", Tilt.values())
    @JvmField val PISTON_TYPE = Property.forEnum("type", PistonType.values())
    @JvmField val CHEST_TYPE = Property.forEnum("type", ChestType.values())
    @JvmField val SLAB_TYPE = Property.forEnum("type", SlabType.values())
    @JvmField val VERTICAL_DIRECTION = Property.forEnum("vertical_direction", setOf(Direction.UP, Direction.DOWN))
    @JvmField val REDSTONE_WEST = Property.forEnum("west", RedstoneSide.values())
    @JvmField val WALL_WEST = Property.forEnum("west", WallSide.values())
}
