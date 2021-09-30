package org.kryptonmc.generators

import com.squareup.kotlinpoet.ClassName
import net.minecraft.SharedConstants
import net.minecraft.core.Registry
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.Bootstrap
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.decoration.Motive
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
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
}
