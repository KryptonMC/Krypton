/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import com.google.common.collect.ImmutableList
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.MINECRAFT_NAMESPACE
import net.kyori.adventure.key.Key.key

/**
 * Interface for a particle.
 * Contains the [Key] and internal ID of the particle type.
 */
interface Particle {

    /**
     * The namespaced key
     */
    val key: Key

    /**
     * The internal ID
     */
    val id: Int
}

/**
 * Represents a particle that has basic options available.
 */
class SimpleParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: ParticleEffectBuilder get() = ParticleEffectBuilder(this)
}

/**
 * Represents a particle that can have velocity applied in a direction.
 */
class DirectionalParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: DirectionalParticleEffectBuilder get() = DirectionalParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a block texture for its appearance.
 */
class BlockParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: BlockParticleEffectBuilder get() = BlockParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses an item texture for its appearance.
 */
class ItemParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: ItemParticleEffectBuilder get() = ItemParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color for its appearance.
 */
class ColorParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: ColorParticleEffectBuilder get() = ColorParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a color and scale for its appearance.
 */
class DustParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: DustParticleEffectBuilder get() = DustParticleEffectBuilder(this)
}

/**
 * Represents a particle that uses a specific note value for its color appearance.
 */
class NoteParticle internal constructor(override val key: Key, override val id: Int) : Particle {

    val builder: NoteParticleEffectBuilder get() = NoteParticleEffectBuilder(this)
}

/**
 * Exposes all available particle types.
 */
@Suppress("unused")
object ParticleType : Iterable<Particle> {

    private val internalValues = mutableListOf<Particle>()

    /**
     * An immutable list of all [ParticleType], ordered by ID
     *
     * This uses an [ImmutableList] to ensure that it cannot be casted to a mutable list and be mutated
     */
    val values: List<Particle> @JvmStatic get() = ImmutableList.copyOf(internalValues)

    @JvmField val AMBIENT_ENTITY_EFFECT = simple("ambient_entity_effect")
    @JvmField val ANGRY_VILLAGER = simple("angry_villager")
    @JvmField val BARRIER = simple("barrier")
    @JvmField val BLOCK = block("block")
    @JvmField val BUBBLE = directional("bubble")
    @JvmField val CLOUD = directional("cloud")
    @JvmField val CRIT = directional("crit")
    @JvmField val DAMAGE_INDICATOR = directional("damage_indicator")
    @JvmField val DRAGON_BREATH = directional("dragon_breath")
    @JvmField val DRIPPING_LAVA = simple("dripping_lava")
    @JvmField val FALLING_LAVA = simple("falling_lava")
    @JvmField val LANDING_LAVA = simple("landing_lava")
    @JvmField val DRIPPING_WATER = simple("dripping_water")
    @JvmField val FALLING_WATER = simple("falling_water")
    @JvmField val DUST = dust("dust")
    @JvmField val EFFECT = simple("effect")
    @JvmField val ELDER_GUARDIAN = simple("elder_guardian")
    @JvmField val ENCHANTED_HIT = directional("enchanted_hit")
    @JvmField val ENCHANT = directional("enchant")
    @JvmField val END_ROD = directional("end_rod")
    @JvmField val ENTITY_EFFECT = color("entity_effect")
    @JvmField val EXPLOSION_EMITTER = simple("explosion_emitter")
    @JvmField val EXPLOSION = simple("explosion")
    @JvmField val FALLING_DUST = block("falling_dust")
    @JvmField val FIREWORK = directional("firework")
    @JvmField val FISHING = directional("fishing")
    @JvmField val FLAME = directional("flame")
    @JvmField val SOUL_FIRE_FLAME = directional("soul_fire_flame")
    @JvmField val SOUL = directional("soul")
    @JvmField val FLASH = simple("flash")
    @JvmField val HAPPY_VILLAGER = simple("happy_villager")
    @JvmField val COMPOSTER = simple("composter")
    @JvmField val HEART = simple("heart")
    @JvmField val INSTANT_EFFECT = simple("instant_effect")
    @JvmField val ITEM = item("item")
    @JvmField val ITEM_SLIME = simple("item_slime")
    @JvmField val ITEM_SNOWBALL = simple("item_snowball")
    @JvmField val LARGE_SMOKE = directional("large_smoke")
    @JvmField val LAVA = simple("lava")
    @JvmField val MYCELIUM = simple("mycelium")
    @JvmField val NOTE = note("note")
    @JvmField val POOF = directional("poof")
    @JvmField val PORTAL = directional("portal")
    @JvmField val RAIN = simple("rain")
    @JvmField val SMOKE = directional("smoke")
    @JvmField val SNEEZE = directional("sneeze")
    @JvmField val SPIT = directional("spit")
    @JvmField val SQUID_INK = directional("squid_ink")
    @JvmField val SWEEP_ATTACK = simple("sweep_attack")
    @JvmField val TOTEM_OF_UNDYING = directional("totem_of_undying")
    @JvmField val UNDERWATER = simple("underwater")
    @JvmField val SPLASH = simple("splash")
    @JvmField val WITCH = simple("witch")
    @JvmField val BUBBLE_POP = directional("bubble_pop")
    @JvmField val CURRENT_DOWN = simple("current_down")
    @JvmField val BUBBLE_COLUMN_UP = directional("bubble_column_up")
    @JvmField val NAUTILUS = directional("nautilus")
    @JvmField val DOLPHIN = simple("dolphin")
    @JvmField val CAMPFIRE_COSY_SMOKE = directional("campfire_cosy_smoke")
    @JvmField val CAMPFIRE_SIGNAL_SMOKE = directional("campfire_signal_smoke")
    @JvmField val DRIPPING_HONEY = simple("dripping_honey")
    @JvmField val FALLING_HONEY = simple("falling_honey")
    @JvmField val LANDING_HONEY = simple("landing_honey")
    @JvmField val FALLING_NECTAR = simple("falling_nectar")
    @JvmField val ASH = simple("ash")
    @JvmField val CRIMSON_SPORE = simple("crimson_spore")
    @JvmField val WARPED_SPORE = simple("warped_spore")
    @JvmField val DRIPPING_OBSIDIAN_TEAR = simple("dripping_obsidian_tear")
    @JvmField val FALLING_OBSIDIAN_TEAR = simple("falling_obsidian_tear")
    @JvmField val LANDING_OBSIDIAN_TEAR = simple("landing_obsidian_tear")
    @JvmField val REVERSE_PORTAL = directional("reverse_portal")
    @JvmField val WHITE_ASH = simple("white_ash")

    private var id: Int = 0

    @JvmStatic private fun simple(name: String) = add(SimpleParticle(key(MINECRAFT_NAMESPACE, name), id++))
    @JvmStatic private fun directional(name: String) = add(DirectionalParticle(key(MINECRAFT_NAMESPACE, name), id++))
    @JvmStatic private fun block(name: String) = add(BlockParticle(key(MINECRAFT_NAMESPACE, name), id++))
    @JvmStatic private fun item(name: String) = add(ItemParticle(key(MINECRAFT_NAMESPACE, name), id++))
    @JvmStatic private fun dust(name: String) = add(DustParticle(key(MINECRAFT_NAMESPACE, name), id++))
    @JvmStatic private fun color(name: String) = add(ColorParticle(key(MINECRAFT_NAMESPACE, name), id++))
    @JvmStatic private fun note(name: String) = add(NoteParticle(key(MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic private fun <T : Particle> add(particle: T): T {
        internalValues += particle
        return particle
    }

    override fun iterator() = internalValues.iterator()
}
