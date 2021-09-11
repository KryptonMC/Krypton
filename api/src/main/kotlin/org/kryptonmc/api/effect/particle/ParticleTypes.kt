/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries

/**
 * Exposes all available particle types.
 *
 * See [here](https://minecraft.fandom.com/wiki/Particles) for more information
 * on all of these types.
 */
@Suppress("unused", "UndocumentedPublicProperty")
public object ParticleTypes {

    // @formatter: off
    @JvmField public val AMBIENT_ENTITY_EFFECT: SimpleParticleType = simple("ambient_entity_effect")
    @JvmField public val ANGRY_VILLAGER: SimpleParticleType = simple("angry_villager")
    @JvmField public val BARRIER: SimpleParticleType = simple("barrier")
    @JvmField public val LIGHT: SimpleParticleType = simple("light")
    @JvmField public val BLOCK: BlockParticleType = block("block")
    @JvmField public val BUBBLE: DirectionalParticleType = directional("bubble")
    @JvmField public val CLOUD: DirectionalParticleType = directional("cloud")
    @JvmField public val CRIT: DirectionalParticleType = directional("crit")
    @JvmField public val DAMAGE_INDICATOR: DirectionalParticleType = directional("damage_indicator")
    @JvmField public val DRAGON_BREATH: DirectionalParticleType = directional("dragon_breath")
    @JvmField public val DRIPPING_LAVA: SimpleParticleType = simple("dripping_lava")
    @JvmField public val FALLING_LAVA: SimpleParticleType = simple("falling_lava")
    @JvmField public val LANDING_LAVA: SimpleParticleType = simple("landing_lava")
    @JvmField public val DRIPPING_WATER: SimpleParticleType = simple("dripping_water")
    @JvmField public val FALLING_WATER: SimpleParticleType = simple("falling_water")
    @JvmField public val DUST: DustParticleType = dust("dust")
    @JvmField public val DUST_COLOR_TRANSITION: DustTransitionParticleType = transition("dust_color_transition")
    @JvmField public val EFFECT: SimpleParticleType = simple("effect")
    @JvmField public val ELDER_GUARDIAN: SimpleParticleType = simple("elder_guardian")
    @JvmField public val ENCHANTED_HIT: DirectionalParticleType = directional("enchanted_hit")
    @JvmField public val ENCHANT: DirectionalParticleType = directional("enchant")
    @JvmField public val END_ROD: DirectionalParticleType = directional("end_rod")
    @JvmField public val ENTITY_EFFECT: ColorParticleType = color("entity_effect")
    @JvmField public val EXPLOSION_EMITTER: SimpleParticleType = simple("explosion_emitter")
    @JvmField public val EXPLOSION: SimpleParticleType = simple("explosion")
    @JvmField public val FALLING_DUST: BlockParticleType = block("falling_dust")
    @JvmField public val FIREWORK: DirectionalParticleType = directional("firework")
    @JvmField public val FISHING: DirectionalParticleType = directional("fishing")
    @JvmField public val FLAME: DirectionalParticleType = directional("flame")
    @JvmField public val SOUL_FIRE_FLAME: DirectionalParticleType = directional("soul_fire_flame")
    @JvmField public val SOUL: DirectionalParticleType = directional("soul")
    @JvmField public val FLASH: SimpleParticleType = simple("flash")
    @JvmField public val HAPPY_VILLAGER: SimpleParticleType = simple("happy_villager")
    @JvmField public val COMPOSTER: SimpleParticleType = simple("composter")
    @JvmField public val HEART: SimpleParticleType = simple("heart")
    @JvmField public val INSTANT_EFFECT: SimpleParticleType = simple("instant_effect")
    @JvmField public val ITEM: ItemParticleType = item("item")
    @JvmField public val ITEM_SLIME: SimpleParticleType = simple("item_slime")
    @JvmField public val ITEM_SNOWBALL: SimpleParticleType = simple("item_snowball")
    @JvmField public val LARGE_SMOKE: DirectionalParticleType = directional("large_smoke")
    @JvmField public val LAVA: SimpleParticleType = simple("lava")
    @JvmField public val MYCELIUM: SimpleParticleType = simple("mycelium")
    @JvmField public val NOTE: NoteParticleType = note("note")
    @JvmField public val POOF: DirectionalParticleType = directional("poof")
    @JvmField public val PORTAL: DirectionalParticleType = directional("portal")
    @JvmField public val RAIN: SimpleParticleType = simple("rain")
    @JvmField public val SMOKE: DirectionalParticleType = directional("smoke")
    @JvmField public val SNEEZE: DirectionalParticleType = directional("sneeze")
    @JvmField public val SPIT: DirectionalParticleType = directional("spit")
    @JvmField public val SQUID_INK: DirectionalParticleType = directional("squid_ink")
    @JvmField public val SWEEP_ATTACK: SimpleParticleType = simple("sweep_attack")
    @JvmField public val TOTEM_OF_UNDYING: DirectionalParticleType = directional("totem_of_undying")
    @JvmField public val UNDERWATER: SimpleParticleType = simple("underwater")
    @JvmField public val SPLASH: SimpleParticleType = simple("splash")
    @JvmField public val WITCH: SimpleParticleType = simple("witch")
    @JvmField public val BUBBLE_POP: DirectionalParticleType = directional("bubble_pop")
    @JvmField public val CURRENT_DOWN: SimpleParticleType = simple("current_down")
    @JvmField public val BUBBLE_COLUMN_UP: DirectionalParticleType = directional("bubble_column_up")
    @JvmField public val NAUTILUS: DirectionalParticleType = directional("nautilus")
    @JvmField public val DOLPHIN: SimpleParticleType = simple("dolphin")
    @JvmField public val CAMPFIRE_COSY_SMOKE: DirectionalParticleType = directional("campfire_cosy_smoke")
    @JvmField public val CAMPFIRE_SIGNAL_SMOKE: DirectionalParticleType = directional("campfire_signal_smoke")
    @JvmField public val DRIPPING_HONEY: SimpleParticleType = simple("dripping_honey")
    @JvmField public val FALLING_HONEY: SimpleParticleType = simple("falling_honey")
    @JvmField public val LANDING_HONEY: SimpleParticleType = simple("landing_honey")
    @JvmField public val FALLING_NECTAR: SimpleParticleType = simple("falling_nectar")
    @JvmField public val ASH: SimpleParticleType = simple("ash")
    @JvmField public val CRIMSON_SPORE: SimpleParticleType = simple("crimson_spore")
    @JvmField public val WARPED_SPORE: SimpleParticleType = simple("warped_spore")
    @JvmField public val DRIPPING_OBSIDIAN_TEAR: SimpleParticleType = simple("dripping_obsidian_tear")
    @JvmField public val FALLING_OBSIDIAN_TEAR: SimpleParticleType = simple("falling_obsidian_tear")
    @JvmField public val LANDING_OBSIDIAN_TEAR: SimpleParticleType = simple("landing_obsidian_tear")
    @JvmField public val REVERSE_PORTAL: DirectionalParticleType = directional("reverse_portal")
    @JvmField public val WHITE_ASH: SimpleParticleType = simple("white_ash")
    // @formatter: on

    private fun simple(name: String) = register(SimpleParticleType(Key.key(name)))
    private fun directional(name: String) = register(DirectionalParticleType(Key.key(name)))
    private fun block(name: String) = register(BlockParticleType(Key.key(name)))
    private fun item(name: String) = register(ItemParticleType(Key.key(name)))
    private fun dust(name: String) = register(DustParticleType(Key.key(name)))
    private fun transition(name: String) = register(DustTransitionParticleType(Key.key(name)))
    private fun color(name: String) = register(ColorParticleType(Key.key(name)))
    private fun note(name: String) = register(NoteParticleType(Key.key(name)))

    @Suppress("UNCHECKED_CAST") // This is fine
    private fun <T : ParticleType> register(particle: T): T =
        Registries.register(Registries.PARTICLE_TYPE, particle.key, particle) as T
}
