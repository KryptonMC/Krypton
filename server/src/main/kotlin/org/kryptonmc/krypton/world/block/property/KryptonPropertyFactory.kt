/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import org.kryptonmc.api.block.property.Properties
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.util.StringSerializable
import java.util.concurrent.ConcurrentHashMap

object KryptonPropertyFactory : Property.Factory {

    val PROPERTIES = ConcurrentHashMap<String, Property<*>>()
    // Rewrites from names in the Properties object to names from Mojang
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
        "REDSTONE_EAST" to "EAST_REDSTONE",
        "REDSTONE_NORTH" to "NORTH_REDSTONE",
        "REDSTONE_SOUTH" to "SOUTH_REDSTONE",
        "REDSTONE_WEST" to "WEST_REDSTONE",
        "WALL_EAST" to "EAST_WALL",
        "WALL_NORTH" to "NORTH_WALL",
        "WALL_SOUTH" to "SOUTH_WALL",
        "WALL_WEST" to "WEST_WALL",
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

    override fun forBoolean(name: String) = BooleanProperty(name)

    override fun forInt(name: String, values: Set<Int>) = IntProperty(name, values)

    override fun <E> forEnum(
        name: String,
        type: Class<E>,
        values: Set<E>
    ): Property<E> where E : Enum<E>, E : StringSerializable = EnumProperty(name, type, values)
}
