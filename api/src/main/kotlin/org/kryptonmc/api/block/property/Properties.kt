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
import org.kryptonmc.api.block.meta.Half
import org.kryptonmc.api.block.meta.NoteBlockInstrument
import org.kryptonmc.api.block.meta.Orientation
import org.kryptonmc.api.block.meta.PistonType
import org.kryptonmc.api.block.meta.RailShape
import org.kryptonmc.api.block.meta.RedstoneSide
import org.kryptonmc.api.block.meta.SlabType
import org.kryptonmc.api.block.meta.StairShape
import org.kryptonmc.api.block.meta.StructureMode
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
     * Boolean values
     */
    val ATTACHED = Property.forBoolean("attached")
    val BOTTOM = Property.forBoolean("bottom")
    val CONDITIONAL = Property.forBoolean("conditional")
    val DISARMED = Property.forBoolean("disarmed")
    val DOWN = Property.forBoolean("down")
    val DRAG = Property.forBoolean("drag")
    val EAST = Property.forBoolean("east")
    val ENABLED = Property.forBoolean("enabled")
    val EXTENDED = Property.forBoolean("extended")
    val EYE = Property.forBoolean("eye")
    val HANGING = Property.forBoolean("hanging")
    val HAS_BOOK = Property.forBoolean("has_book")
    val HAS_FIRST_BOTTLE = Property.forBoolean("has_bottle_0")
    val HAS_SECOND_BOTTLE = Property.forBoolean("has_bottle_1")
    val HAS_THIRD_BOTTLE = Property.forBoolean("has_bottle_2")
    val HAS_RECORD = Property.forBoolean("has_record")
    val IN_WALL = Property.forBoolean("in_wall")
    val INVERTED = Property.forBoolean("inverted")
    val LIT = Property.forBoolean("lit")
    val LOCKED = Property.forBoolean("locked")
    val NORTH = Property.forBoolean("north")
    val OCCUPIED = Property.forBoolean("occupied")
    val OPEN = Property.forBoolean("open")
    val PERSISTENT = Property.forBoolean("persistent")
    val POWERED = Property.forBoolean("powered")
    val SHORT = Property.forBoolean("short")
    val SIGNAL_FIRE = Property.forBoolean("signal_fire")
    val SNOWY = Property.forBoolean("snowy")
    val SOUTH = Property.forBoolean("south")
    val TRIGGERED = Property.forBoolean("triggered")
    val UNSTABLE = Property.forBoolean("unstable")
    val UP = Property.forBoolean("up")
    val WATERLOGGED = Property.forBoolean("waterlogged")
    val WEST = Property.forBoolean("west")

    /**
     * Integer values.
     */
    val AGE_1 = Property.forInt("age", 0..1)
    val AGE_2 = Property.forInt("age", 0..2)
    val AGE_3 = Property.forInt("age", 0..3)
    val AGE_5 = Property.forInt("age", 0..5)
    val AGE_7 = Property.forInt("age", 0..7)
    val AGE_15 = Property.forInt("age", 0..15)
    val AGE_25 = Property.forInt("age", 0..25)
    val BITES = Property.forInt("bites", 0..6)
    val CHARGES = Property.forInt("charges", 0..4)
    val DELAY = Property.forInt("delay", 1..4)
    val DISTANCE = Property.forInt("distance", 1..7)
    val SCAFFOLD_DISTANCE = Property.forInt("distance", 0..7)
    val EGGS = Property.forInt("eggs", 1..4)
    val HATCH = Property.forInt("hatch", 0..2)
    val LAYERS = Property.forInt("layers", 1..8)
    val CAULDRON_LEVEL = Property.forInt("level", 0..3)
    val COMPOSTER_LEVEL = Property.forInt("level", 0..8)
    val LIQUID_LEVEL = Property.forInt("level", 0..15)
    val MOISTURE = Property.forInt("moisture", 0..7)
    val NOTE = Property.forInt("note", 0..24)
    val PICKLES = Property.forInt("pickles", 1..4)
    val POWER = Property.forInt("power", 0..15)
    val ROTATION = Property.forInt("rotation", 0..15)
    val STAGE = Property.forInt("stage", 0..1)

    /**
     * Enum properties
     */
    val BELL_ATTACHMENT = Property.forEnum("attachment", BellAttachment.values())
    val AXIS = Property.forEnum("axis", Direction.Axis.values())
    val HORIZONTAL_AXIS = Property.forEnum("axis", setOf(Direction.Axis.X, Direction.Axis.Z))
    val REDSTONE_EAST = Property.forEnum("east", RedstoneSide.values())
    val FACE = Property.forEnum("face", AttachFace.values())
    val FACING = Property.forEnum("facing", Direction.values())
    val HORIZONTAL_FACING = Property.forEnum("facing", setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
    val HOPPER_FACING = Property.forEnum("facing", setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN))
    val DOUBLE_BLOCK_HALF = Property.forEnum("half", DoubleBlockHalf.values())
    val HALF = Property.forEnum("half", Half.values())
    val HINGE = Property.forEnum("hinge", DoorHingeSide.values())
    val INSTRUMENT = Property.forEnum("instrument", NoteBlockInstrument.values())
    val LEAVES = Property.forEnum("leaves", BambooLeaves.values())
    val COMPARATOR_MODE = Property.forEnum("mode", ComparatorMode.values())
    val STRUCTURE_MODE = Property.forEnum("mode", StructureMode.values())
    val REDSTONE_NORTH = Property.forEnum("north", RedstoneSide.values())
    val ORIENTATION = Property.forEnum("orientation", Orientation.values())
    val PART = Property.forEnum("part", BedPart.values())
    val RAIL_SHAPE = Property.forEnum("shape", RailShape.values())
    val STRAIGHT_RAIL_SHAPE = Property.forEnum("shape", setOf(RailShape.ASCENDING_NORTH, RailShape.ASCENDING_SOUTH, RailShape.ASCENDING_EAST, RailShape.ASCENDING_WEST, RailShape.EAST_WEST, RailShape.NORTH_SOUTH))
    val STAIR_SHAPE = Property.forEnum("shape", StairShape.values())
    val REDSTONE_SOUTH = Property.forEnum("south", RedstoneSide.values())
    val PISTON_TYPE = Property.forEnum("type", PistonType.values())
    val CHEST_TYPE = Property.forEnum("type", ChestType.values())
    val SLAB_TYPE = Property.forEnum("type", SlabType.values())
    val REDSTONE_WEST = Property.forEnum("west", RedstoneSide.values())
}
