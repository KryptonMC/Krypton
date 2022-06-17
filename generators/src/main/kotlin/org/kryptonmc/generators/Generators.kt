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
package org.kryptonmc.generators

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import net.minecraft.SharedConstants
import net.minecraft.core.Registry
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.Bootstrap
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.BlockTags
import net.minecraft.tags.EntityTypeTags
import net.minecraft.tags.FluidTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import java.nio.file.Path

fun main() {
    SharedConstants.tryDetectVersion()
    Bootstrap.bootStrap()
    val output = Path.of("api/src/generated/kotlin")
    val generator = StandardGenerator(output)
    generator.run<Blocks, Block>(Registry.BLOCK, "block.Blocks", "block.Block", "BLOCK")
    generator.run<SoundEvents, SoundEvent>(Registry.SOUND_EVENT, "effect.sound.SoundEvents", "effect.sound.SoundEvent", "SOUND_EVENT")
    generator.run<Fluids, Fluid>(Registry.FLUID, "fluid.Fluids", "fluid.Fluid", "FLUID")
    generator.run<Items, Item>(Registry.ITEM, "item.ItemTypes", "item.ItemType", "ITEM")
    generator.run<Biomes, ResourceKey<*>>(BuiltinRegistries.BIOME, "world.biome.Biomes", "world.biome.Biome", "BIOME") {
        (it.get(null) as ResourceKey<*>).location()
    }
    generator.run<Attributes, Attribute>(Registry.ATTRIBUTE, "entity.attribute.AttributeTypes", "entity.attribute.AttributeType", "ATTRIBUTE")
    generator.run<BlockEntityType<*>, BlockEntityType<*>>(
        Registry.BLOCK_ENTITY_TYPE,
        "block.entity.BlockEntityTypes",
        "block.entity.BlockEntityType",
        "BLOCK_ENTITY_TYPE"
    )
    DyeColorGenerator(output).run()
    val pkg = "org.kryptonmc.api"
    val entityType = ClassName("$pkg.entity", "EntityType").parameterizedBy(STAR)
    val tagGenerator = TagGenerator(output)
    tagGenerator.run<BlockTags>("BlockTags", "BLOCKS", ClassName("org.kryptonmc.api.block", "Block"), "Block")
    tagGenerator.run<EntityTypeTags>("EntityTypeTags", "ENTITY_TYPES", entityType, "EntityType<\\*>")
    tagGenerator.run<FluidTags>("FluidTags", "FLUIDS", ClassName("org.kryptonmc.api.fluid", "Fluid"), "Fluid")
    tagGenerator.run<ItemTags>("ItemTags", "ITEMS", ClassName("org.kryptonmc.api.item", "ItemType"), "ItemType")
}
