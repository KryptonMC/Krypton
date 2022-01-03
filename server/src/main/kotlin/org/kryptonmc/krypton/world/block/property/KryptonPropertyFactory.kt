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
package org.kryptonmc.krypton.world.block.property

import kotlinx.collections.immutable.toImmutableSet
import org.kryptonmc.api.block.property.Properties
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.util.StringSerializable
import java.util.concurrent.ConcurrentHashMap

object KryptonPropertyFactory : Property.Factory {

    @JvmField
    val PROPERTIES: MutableMap<String, Property<*>> = ConcurrentHashMap()
    // Rewrites from names in the Properties object to names from Mojang
    // The way this entire thing works is pretty hacky. The reason why we do this in the first place is because ArticData uses Mojang names,
    // and so we have to map those Mojang names to our names, since Krypton uses some more sensible names in places where they make more sense.
    // TODO: Check on update
    private val NAME_REWRITES = mapOf(
        "HAS_FIRST_BOTTLE" to "HAS_BOTTLE_0",
        "HAS_SECOND_BOTTLE" to "HAS_BOTTLE_1",
        "HAS_THIRD_BOTTLE" to "HAS_BOTTLE_2",
        "CHARGES" to "RESPAWN_ANCHOR_CHARGES",
        "SCAFFOLD_DISTANCE" to "STABILITY_DISTANCE",
        "CAULDRON_LEVEL" to "LEVEL_CAULDRON",
        "COMPOSTER_LEVEL" to "LEVEL_COMPOSTER",
        "HONEY_LEVEL" to "LEVEL_HONEY",
        "LIQUID_LEVEL" to "LEVEL_FLOWING",
        "ROTATION" to "ROTATION_16",
        "EAST_REDSTONE_SIDE" to "EAST_REDSTONE",
        "NORTH_REDSTONE_SIDE" to "NORTH_REDSTONE",
        "SOUTH_REDSTONE_SIDE" to "SOUTH_REDSTONE",
        "WEST_REDSTONE_SIDE" to "WEST_REDSTONE",
        "EAST_WALL_SIDE" to "EAST_WALL",
        "NORTH_WALL_SIDE" to "NORTH_WALL",
        "SOUTH_WALL_SIDE" to "SOUTH_WALL",
        "WEST_WALL_SIDE" to "WEST_WALL",
        "HOPPER_FACING" to "FACING_HOPPER",
        "INSTRUMENT" to "NOTEBLOCK_INSTRUMENT",
        "COMPARATOR_MODE" to "MODE_COMPARATOR",
        "STRUCTURE_MODE" to "STRUCTUREBLOCK_MODE",
        "STRAIGHT_RAIL_SHAPE" to "RAIL_SHAPE_STRAIGHT",
        "STAIR_SHAPE" to "STAIRS_SHAPE",
    )

    fun bootstrap() {
        Properties::class.java.declaredFields.forEach {
            if (it.name == "INSTANCE") return@forEach
            PROPERTIES[NAME_REWRITES.getOrDefault(it.name, it.name)] = it.get(null) as Property<*>
        }
    }

    override fun forBoolean(name: String): Property<Boolean> = BooleanProperty(name)

    override fun forInt(name: String, values: Set<Int>): Property<Int> = IntProperty(name, values.toImmutableSet())

    override fun <E> forEnum(
        name: String,
        type: Class<E>,
        values: Set<E>
    ): Property<E> where E : Enum<E>, E : StringSerializable = EnumProperty(name, type, values.toImmutableSet())
}
