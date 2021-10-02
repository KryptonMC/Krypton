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
package org.kryptonmc.generators

import com.squareup.kotlinpoet.ClassName
import net.minecraft.SharedConstants
import net.minecraft.core.Registry
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.Bootstrap
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.decoration.Motive
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
    val generator = StandardGenerator(Path.of("api/src/generated/kotlin"))
    generator.run(
        Blocks::class.java,
        Registry.BLOCK,
        ClassName("org.kryptonmc.api.block", "Blocks"),
        Block::class.java,
        ClassName("org.kryptonmc.api.block", "Block"),
        "BLOCK"
    )
    generator.run(
        SoundEvents::class.java,
        Registry.SOUND_EVENT,
        ClassName("org.kryptonmc.api.effect.sound", "SoundEvents"),
        SoundEvent::class.java,
        ClassName("org.kryptonmc.api.effect.sound", "SoundEvent"),
        "SOUND_EVENT"
    )
    generator.run(
        Motive::class.java,
        Registry.MOTIVE,
        ClassName("org.kryptonmc.api.entity.hanging", "Canvases"),
        Motive::class.java,
        ClassName("org.kryptonmc.api.entity.hanging", "Canvas"),
        "CANVAS"
    )
    generator.run(
        Fluids::class.java,
        Registry.FLUID,
        ClassName("org.kryptonmc.api.fluid", "Fluids"),
        Fluid::class.java,
        ClassName("org.kryptonmc.api.fluid", "Fluid"),
        "FLUID"
    )
    generator.run(
        Items::class.java,
        Registry.ITEM,
        ClassName("org.kryptonmc.api.item", "ItemTypes"),
        Item::class.java,
        ClassName("org.kryptonmc.api.item", "ItemType"),
        "ITEM"
    )
    generator.run(
        Biomes::class.java,
        BuiltinRegistries.BIOME,
        ClassName("org.kryptonmc.api.world.biome", "Biomes"),
        ResourceKey::class.java,
        ClassName("org.kryptonmc.api.world.biome", "Biome"),
        "BIOME"
    ) { (it.get(null) as ResourceKey<*>).location() }
    generator.run(
        Attributes::class.java,
        Registry.ATTRIBUTE,
        ClassName("org.kryptonmc.api.entity.attribute", "AttributeTypes"),
        Attribute::class.java,
        ClassName("org.kryptonmc.api.entity.attribute", "AttributeType"),
        "ATTRIBUTE"
    )
    generator.run(
        BlockEntityType::class.java,
        Registry.BLOCK_ENTITY_TYPE,
        ClassName("org.kryptonmc.api.block.entity", "BlockEntityTypes"),
        BlockEntityType::class.java,
        ClassName("org.kryptonmc.api.block.entity", "BlockEntityType"),
        "BLOCK_ENTITY_TYPE"
    )
}
