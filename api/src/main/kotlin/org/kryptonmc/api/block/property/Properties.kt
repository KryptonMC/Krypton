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
@Suppress("StringLiteralDuplication", "MaxLineLength", "UndocumentedPublicProperty")
public object Properties {

    /**
     * Boolean properties.
     */
    @JvmField public val ATTACHED: Property<Boolean> = Property.forBoolean("attached")
    @JvmField public val BERRIES: Property<Boolean> = Property.forBoolean("berries")
    @JvmField public val BOTTOM: Property<Boolean> = Property.forBoolean("bottom")
    @JvmField public val CONDITIONAL: Property<Boolean> = Property.forBoolean("conditional")
    @JvmField public val DISARMED: Property<Boolean> = Property.forBoolean("disarmed")
    @JvmField public val DOWN: Property<Boolean> = Property.forBoolean("down")
    @JvmField public val DRAG: Property<Boolean> = Property.forBoolean("drag")
    @JvmField public val EAST: Property<Boolean> = Property.forBoolean("east")
    @JvmField public val ENABLED: Property<Boolean> = Property.forBoolean("enabled")
    @JvmField public val EXTENDED: Property<Boolean> = Property.forBoolean("extended")
    @JvmField public val EYE: Property<Boolean> = Property.forBoolean("eye")
    @JvmField public val FALLING: Property<Boolean> = Property.forBoolean("falling")
    @JvmField public val HANGING: Property<Boolean> = Property.forBoolean("hanging")
    @JvmField public val HAS_BOOK: Property<Boolean> = Property.forBoolean("has_book")
    @JvmField public val HAS_FIRST_BOTTLE: Property<Boolean> = Property.forBoolean("has_bottle_0")
    @JvmField public val HAS_SECOND_BOTTLE: Property<Boolean> = Property.forBoolean("has_bottle_1")
    @JvmField public val HAS_THIRD_BOTTLE: Property<Boolean> = Property.forBoolean("has_bottle_2")
    @JvmField public val HAS_RECORD: Property<Boolean> = Property.forBoolean("has_record")
    @JvmField public val IN_WALL: Property<Boolean> = Property.forBoolean("in_wall")
    @JvmField public val INVERTED: Property<Boolean> = Property.forBoolean("inverted")
    @JvmField public val LIT: Property<Boolean> = Property.forBoolean("lit")
    @JvmField public val LOCKED: Property<Boolean> = Property.forBoolean("locked")
    @JvmField public val NORTH: Property<Boolean> = Property.forBoolean("north")
    @JvmField public val OCCUPIED: Property<Boolean> = Property.forBoolean("occupied")
    @JvmField public val OPEN: Property<Boolean> = Property.forBoolean("open")
    @JvmField public val PERSISTENT: Property<Boolean> = Property.forBoolean("persistent")
    @JvmField public val POWERED: Property<Boolean> = Property.forBoolean("powered")
    @JvmField public val SHORT: Property<Boolean> = Property.forBoolean("short")
    @JvmField public val SIGNAL_FIRE: Property<Boolean> = Property.forBoolean("signal_fire")
    @JvmField public val SNOWY: Property<Boolean> = Property.forBoolean("snowy")
    @JvmField public val SOUTH: Property<Boolean> = Property.forBoolean("south")
    @JvmField public val TRIGGERED: Property<Boolean> = Property.forBoolean("triggered")
    @JvmField public val UNSTABLE: Property<Boolean> = Property.forBoolean("unstable")
    @JvmField public val UP: Property<Boolean> = Property.forBoolean("up")
    @JvmField public val VINE_END: Property<Boolean> = Property.forBoolean("vine_end")
    @JvmField public val WATERLOGGED: Property<Boolean> = Property.forBoolean("waterlogged")
    @JvmField public val WEST: Property<Boolean> = Property.forBoolean("west")

    /**
     * Integer properties.
     */
    @JvmField public val AGE_1: Property<Int> = Property.forInt("age", 0..1)
    @JvmField public val AGE_2: Property<Int> = Property.forInt("age", 0..2)
    @JvmField public val AGE_3: Property<Int> = Property.forInt("age", 0..3)
    @JvmField public val AGE_5: Property<Int> = Property.forInt("age", 0..5)
    @JvmField public val AGE_7: Property<Int> = Property.forInt("age", 0..7)
    @JvmField public val AGE_15: Property<Int> = Property.forInt("age", 0..15)
    @JvmField public val AGE_25: Property<Int> = Property.forInt("age", 0..25)
    @JvmField public val BITES: Property<Int> = Property.forInt("bites", 0..6)
    @JvmField public val CANDLES: Property<Int> = Property.forInt("candles", 1..4)
    @JvmField public val CHARGES: Property<Int> = Property.forInt("charges", 0..4)
    @JvmField public val DELAY: Property<Int> = Property.forInt("delay", 1..4)
    @JvmField public val DISTANCE: Property<Int> = Property.forInt("distance", 1..7)
    @JvmField public val SCAFFOLD_DISTANCE: Property<Int> = Property.forInt("distance", 0..7)
    @JvmField public val EGGS: Property<Int> = Property.forInt("eggs", 1..4)
    @JvmField public val HATCH: Property<Int> = Property.forInt("hatch", 0..2)
    @JvmField public val LAYERS: Property<Int> = Property.forInt("layers", 1..8)
    @JvmField public val CAULDRON_LEVEL: Property<Int> = Property.forInt("level", 0..3)
    @JvmField public val COMPOSTER_LEVEL: Property<Int> = Property.forInt("level", 0..8)
    @JvmField public val HONEY_LEVEL: Property<Int> = Property.forInt("level", 0..5)
    @JvmField public val LIQUID_LEVEL: Property<Int> = Property.forInt("level", 0..8)
    @JvmField public val LEVEL: Property<Int> = Property.forInt("level", 0..15)
    @JvmField public val MOISTURE: Property<Int> = Property.forInt("moisture", 0..7)
    @JvmField public val NOTE: Property<Int> = Property.forInt("note", 0..24)
    @JvmField public val PICKLES: Property<Int> = Property.forInt("pickles", 1..4)
    @JvmField public val POWER: Property<Int> = Property.forInt("power", 0..15)
    @JvmField public val ROTATION: Property<Int> = Property.forInt("rotation", 0..15)
    @JvmField public val STAGE: Property<Int> = Property.forInt("stage", 0..1)

    /**
     * Enum properties.
     */
    @JvmField public val BELL_ATTACHMENT: Property<BellAttachment> = Property.forEnum("attachment", BellAttachment.values())
    @JvmField public val AXIS: Property<Direction.Axis> = Property.forEnum("axis", Direction.Axis.values())
    @JvmField public val HORIZONTAL_AXIS: Property<Direction.Axis> = Property.forEnum(
        "axis",
        setOf(Direction.Axis.X, Direction.Axis.Z)
    )
    @JvmField public val EAST_REDSTONE_SIDE: Property<RedstoneSide> = Property.forEnum("east", RedstoneSide.values())
    @JvmField public val EAST_WALL_SIDE: Property<WallSide> = Property.forEnum("east", WallSide.values())
    @JvmField public val ATTACH_FACE: Property<AttachFace> = Property.forEnum("face", AttachFace.values())
    @JvmField public val FACING: Property<Direction> = Property.forEnum("facing", Direction.values())
    @JvmField public val HORIZONTAL_FACING: Property<Direction> = Property.forEnum(
        "facing",
        setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
    )
    @JvmField public val HOPPER_FACING: Property<Direction> = Property.forEnum(
        "facing",
        setOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN)
    )
    @JvmField public val DOUBLE_BLOCK_HALF: Property<DoubleBlockHalf> = Property.forEnum("half", DoubleBlockHalf.values())
    @JvmField public val HALF: Property<Half> = Property.forEnum("half", Half.values())
    @JvmField public val DOOR_HINGE: Property<DoorHingeSide> = Property.forEnum("hinge", DoorHingeSide.values())
    @JvmField public val INSTRUMENT: Property<NoteBlockInstrument> = Property.forEnum("instrument", NoteBlockInstrument.values())
    @JvmField public val BAMBOO_LEAVES: Property<BambooLeaves> = Property.forEnum("leaves", BambooLeaves.values())
    @JvmField public val COMPARATOR_MODE: Property<ComparatorMode> = Property.forEnum("mode", ComparatorMode.values())
    @JvmField public val STRUCTURE_MODE: Property<StructureMode> = Property.forEnum("mode", StructureMode.values())
    @JvmField public val NORTH_REDSTONE_SIDE: Property<RedstoneSide> = Property.forEnum("north", RedstoneSide.values())
    @JvmField public val NORTH_WALL_SIDE: Property<WallSide> = Property.forEnum("north", WallSide.values())
    @JvmField public val ORIENTATION: Property<Orientation> = Property.forEnum("orientation", Orientation.values())
    @JvmField public val BED_PART: Property<BedPart> = Property.forEnum("part", BedPart.values())
    @JvmField public val SCULK_SENSOR_PHASE: Property<SculkSensorPhase> = Property.forEnum(
        "sculk_sensor_phase",
        SculkSensorPhase.values()
    )
    @JvmField public val RAIL_SHAPE: Property<RailShape> = Property.forEnum("shape", RailShape.values())
    @JvmField public val STRAIGHT_RAIL_SHAPE: Property<RailShape> = Property.forEnum(
        "shape",
        setOf(
            RailShape.ASCENDING_NORTH,
            RailShape.ASCENDING_SOUTH,
            RailShape.ASCENDING_EAST,
            RailShape.ASCENDING_WEST,
            RailShape.EAST_WEST,
            RailShape.NORTH_SOUTH
        )
    )
    @JvmField public val STAIR_SHAPE: Property<StairShape> = Property.forEnum("shape", StairShape.values())
    @JvmField public val SOUTH_REDSTONE_SIDE: Property<RedstoneSide> = Property.forEnum("south", RedstoneSide.values())
    @JvmField public val SOUTH_WALL_SIDE: Property<WallSide> = Property.forEnum("south", WallSide.values())
    @JvmField public val DRIPSTONE_THICKNESS: Property<DripstoneThickness> = Property.forEnum(
        "thickness",
        DripstoneThickness.values()
    )
    @JvmField public val TILT: Property<Tilt> = Property.forEnum("tilt", Tilt.values())
    @JvmField public val PISTON_TYPE: Property<PistonType> = Property.forEnum("type", PistonType.values())
    @JvmField public val CHEST_TYPE: Property<ChestType> = Property.forEnum("type", ChestType.values())
    @JvmField public val SLAB_TYPE: Property<SlabType> = Property.forEnum("type", SlabType.values())
    @JvmField public val VERTICAL_DIRECTION: Property<Direction> = Property.forEnum(
        "vertical_direction",
        setOf(Direction.UP, Direction.DOWN)
    )
    @JvmField public val WEST_REDSTONE_SIDE: Property<RedstoneSide> = Property.forEnum("west", RedstoneSide.values())
    @JvmField public val WEST_WALL_SIDE: Property<WallSide> = Property.forEnum("west", WallSide.values())
}
