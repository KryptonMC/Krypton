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
package org.kryptonmc.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.Material
import java.lang.reflect.Modifier

object BlockDataGenerator : DataGenerator {

    private val materialNames: Map<Material, String> = Material::class.java.declaredFields.asSequence()
        .filter { Modifier.isStatic(it.modifiers) }
        .filter { Modifier.isPublic(it.modifiers) }
        .filter { it.type == Material::class.java }
        .associate { it.get(null) as Material to it.name }

    private val soundTypeNames: Map<SoundType, String> = SoundType::class.java.declaredFields.asSequence()
        .filter { Modifier.isStatic(it.modifiers) }
        .filter { Modifier.isPublic(it.modifiers) }
        .filter { it.type == SoundType::class.java }
        .associate { it.get(null) as SoundType to it.name }

    private val propertyNames = getPropertyNames()
    private val propertyNameRewrites: Map<String, String> = mapOf(
        "HAS_BOTTLE_0" to "HAS_FIRST_BOTTLE",
        "HAS_BOTTLE_1" to "HAS_SECOND_BOTTLE",
        "HAS_BOTTLE_2" to "HAS_THIRD_BOTTLE",
        "FACING_HOPPER" to "HOPPER_FACING",
        "EAST_WALL" to "EAST_WALL_SIDE",
        "NORTH_WALL" to "NORTH_WALL_SIDE",
        "SOUTH_WALL" to "SOUTH_WALL_SIDE",
        "WEST_WALL" to "WEST_WALL_SIDE",
        "EAST_REDSTONE" to "EAST_REDSTONE_SIDE",
        "NORTH_REDSTONE" to "NORTH_REDSTONE_SIDE",
        "SOUTH_REDSTONE" to "SOUTH_REDSTONE_SIDE",
        "WEST_REDSTONE" to "WEST_REDSTONE_SIDE",
        "RAIL_SHAPE_STRAIGHT" to "STRAIGHT_RAIL_SHAPE",
        "LEVEL_CAULDRON" to "CAULDRON_LEVEL",
        "LEVEL_COMPOSTER" to "COMPOSTER_LEVEL",
        "LEVEL_FLOWING" to "LIQUID_LEVEL",
        "LEVEL_HONEY" to "HONEY_LEVEL",
        "STABILITY_DISTANCE" to "SCAFFOLD_DISTANCE",
        "ROTATION_16" to "ROTATION",
        "MODE_COMPARATOR" to "COMPARATOR_MODE",
        "NOTEBLOCK_INSTRUMENT" to "INSTRUMENT",
        "STAIRS_SHAPE" to "STAIR_SHAPE",
        "STRUCTUREBLOCK_MODE" to "STRUCTURE_MODE",
        "CHISELED_BOOKSHELF_SLOT_0_OCCUPIED" to "CHISELED_BOOKSHELF_FIRST_SLOT_OCCUPIED",
        "CHISELED_BOOKSHELF_SLOT_1_OCCUPIED" to "CHISELED_BOOKSHELF_SECOND_SLOT_OCCUPIED",
        "CHISELED_BOOKSHELF_SLOT_2_OCCUPIED" to "CHISELED_BOOKSHELF_THIRD_SLOT_OCCUPIED",
        "CHISELED_BOOKSHELF_SLOT_3_OCCUPIED" to "CHISELED_BOOKSHELF_FOURTH_SLOT_OCCUPIED",
        "CHISELED_BOOKSHELF_SLOT_4_OCCUPIED" to "CHISELED_BOOKSHELF_FIFTH_SLOT_OCCUPIED",
        "CHISELED_BOOKSHELF_SLOT_5_OCCUPIED" to "CHISELED_BOOKSHELF_SIXTH_SLOT_OCCUPIED"
    )

    override fun name(): String = "blocks"

    override fun generate(): JsonElement {
        val blocks = JsonObject()

        val registry = BuiltInRegistries.BLOCK
        val sortedKeys = BuiltInRegistries.BLOCK.keySet().stream().sorted(Comparator.comparingInt { registry.getId(registry.get(it)) }).toList()

        for (blockKey in sortedKeys) {
            val block = registry.get(blockKey)
            val state = block.defaultBlockState()
            val blockData = JsonObject()

            blockData.addProperty("material", getMaterialName(block))
            blockData.addProperty("collision", state.isRandomlyTicking)

            val soundType = block.getSoundType(block.defaultBlockState())
            val soundTypeName = checkNotNull(soundTypeNames.get(soundType)) { "Could not find name for sound type $soundType!" }
            blockData.addProperty("soundType", soundTypeName)

            blockData.addProperty("explosionResistance", block.explosionResistance)
            blockData.addProperty("hardness", block.defaultDestroyTime())
            blockData.addProperty("requiresCorrectTool", state.requiresCorrectToolForDrops())
            blockData.addProperty("friction", block.friction)
            blockData.addProperty("speedFactor", block.speedFactor)
            blockData.addProperty("jumpFactor", block.jumpFactor)

            val properties = BlockBehaviour::class.java.getDeclaredField("properties").apply { isAccessible = true }.get(block) as Properties
            val drops = Properties::class.java.getDeclaredField("drops").apply { isAccessible = true }.get(properties) as? ResourceLocation
            if (drops != null) blockData.addProperty("drops", drops.toString())

            blockData.addProperty("canOcclude", state.canOcclude())
            blockData.addProperty("air", state.isAir)
            blockData.addProperty("dynamicShape", block.hasDynamicShape())

            run {
                val statePropertyData = JsonArray()
                val stateProperties = block.stateDefinition.properties

                for (property in stateProperties) {
                    val name = propertyNames.get(property)
                    val rewrittenName = propertyNameRewrites.getOrDefault(name, name)
                    statePropertyData.add(rewrittenName)
                }

                blockData.add("properties", statePropertyData)
            }

            blocks.add(blockKey.toString(), blockData)
        }
        return blocks
    }

    @JvmStatic
    private fun getMaterialName(block: Block): String {
        val field = BlockBehaviour::class.java.getDeclaredField("material")
        field.isAccessible = true
        val material = field.get(block) as Material
        return requireNotNull(materialNames.get(material)) { "Could not find name for material $material!" }
    }

    @JvmStatic
    private fun getPropertyNames(): Map<Property<*>, String> {
        val result = LinkedHashMap<Property<*>, String>()
        BlockStateProperties::class.java.declaredFields.forEach { field ->
            if (!Property::class.java.isAssignableFrom(field.type)) return@forEach
            field.isAccessible = true
            val property = field.get(null) as Property<*>
            result.put(property, field.name)
        }
        return result
    }
}
