/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.particle

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All of the built-in vanilla particle types.
 *
 * See [here](https://minecraft.fandom.com/wiki/Particles) for more information
 * on all of these types.
 */
@Catalogue(ParticleType::class)
public object ParticleTypes {

    // @formatter:off
    @JvmField
    public val AMBIENT_ENTITY_EFFECT: RegistryReference<SimpleParticleType> = of("ambient_entity_effect")
    @JvmField
    public val ANGRY_VILLAGER: RegistryReference<SimpleParticleType> = of("angry_villager")
    @JvmField
    public val BLOCK: RegistryReference<BlockParticleType> = of("block")
    @JvmField
    public val BLOCK_MARKER: RegistryReference<BlockParticleType> = of("block_marker")
    @JvmField
    public val BUBBLE: RegistryReference<DirectionalParticleType> = of("bubble")
    @JvmField
    public val CLOUD: RegistryReference<DirectionalParticleType> = of("cloud")
    @JvmField
    public val CRIT: RegistryReference<DirectionalParticleType> = of("crit")
    @JvmField
    public val DAMAGE_INDICATOR: RegistryReference<DirectionalParticleType> = of("damage_indicator")
    @JvmField
    public val DRAGON_BREATH: RegistryReference<DirectionalParticleType> = of("dragon_breath")
    @JvmField
    public val DRIPPING_LAVA: RegistryReference<SimpleParticleType> = of("dripping_lava")
    @JvmField
    public val FALLING_LAVA: RegistryReference<SimpleParticleType> = of("falling_lava")
    @JvmField
    public val LANDING_LAVA: RegistryReference<SimpleParticleType> = of("landing_lava")
    @JvmField
    public val DRIPPING_WATER: RegistryReference<SimpleParticleType> = of("dripping_water")
    @JvmField
    public val FALLING_WATER: RegistryReference<SimpleParticleType> = of("falling_water")
    @JvmField
    public val DUST: RegistryReference<DustParticleType> = of("dust")
    @JvmField
    public val DUST_COLOR_TRANSITION: RegistryReference<DustTransitionParticleType> = of("dust_color_transition")
    @JvmField
    public val EFFECT: RegistryReference<SimpleParticleType> = of("effect")
    @JvmField
    public val ELDER_GUARDIAN: RegistryReference<SimpleParticleType> = of("elder_guardian")
    @JvmField
    public val ENCHANTED_HIT: RegistryReference<DirectionalParticleType> = of("enchanted_hit")
    @JvmField
    public val ENCHANT: RegistryReference<DirectionalParticleType> = of("enchant")
    @JvmField
    public val END_ROD: RegistryReference<DirectionalParticleType> = of("end_rod")
    @JvmField
    public val ENTITY_EFFECT: RegistryReference<ColorParticleType> = of("entity_effect")
    @JvmField
    public val EXPLOSION_EMITTER: RegistryReference<SimpleParticleType> = of("explosion_emitter")
    @JvmField
    public val EXPLOSION: RegistryReference<SimpleParticleType> = of("explosion")
    @JvmField
    public val FALLING_DUST: RegistryReference<BlockParticleType> = of("falling_dust")
    @JvmField
    public val FIREWORK: RegistryReference<DirectionalParticleType> = of("firework")
    @JvmField
    public val FISHING: RegistryReference<DirectionalParticleType> = of("fishing")
    @JvmField
    public val FLAME: RegistryReference<DirectionalParticleType> = of("flame")
    @JvmField
    public val SOUL_FIRE_FLAME: RegistryReference<DirectionalParticleType> = of("soul_fire_flame")
    @JvmField
    public val SOUL: RegistryReference<DirectionalParticleType> = of("soul")
    @JvmField
    public val FLASH: RegistryReference<SimpleParticleType> = of("flash")
    @JvmField
    public val HAPPY_VILLAGER: RegistryReference<SimpleParticleType> = of("happy_villager")
    @JvmField
    public val COMPOSTER: RegistryReference<SimpleParticleType> = of("composter")
    @JvmField
    public val HEART: RegistryReference<SimpleParticleType> = of("heart")
    @JvmField
    public val INSTANT_EFFECT: RegistryReference<SimpleParticleType> = of("instant_effect")
    @JvmField
    public val ITEM: RegistryReference<ItemParticleType> = of("item")
    @JvmField
    public val VIBRATION: RegistryReference<VibrationParticleType> = of("vibration")
    @JvmField
    public val ITEM_SLIME: RegistryReference<SimpleParticleType> = of("item_slime")
    @JvmField
    public val ITEM_SNOWBALL: RegistryReference<SimpleParticleType> = of("item_snowball")
    @JvmField
    public val LARGE_SMOKE: RegistryReference<DirectionalParticleType> = of("large_smoke")
    @JvmField
    public val LAVA: RegistryReference<SimpleParticleType> = of("lava")
    @JvmField
    public val MYCELIUM: RegistryReference<SimpleParticleType> = of("mycelium")
    @JvmField
    public val NOTE: RegistryReference<NoteParticleType> = of("note")
    @JvmField
    public val POOF: RegistryReference<DirectionalParticleType> = of("poof")
    @JvmField
    public val PORTAL: RegistryReference<DirectionalParticleType> = of("portal")
    @JvmField
    public val RAIN: RegistryReference<SimpleParticleType> = of("rain")
    @JvmField
    public val SMOKE: RegistryReference<DirectionalParticleType> = of("smoke")
    @JvmField
    public val SNEEZE: RegistryReference<DirectionalParticleType> = of("sneeze")
    @JvmField
    public val SPIT: RegistryReference<DirectionalParticleType> = of("spit")
    @JvmField
    public val SQUID_INK: RegistryReference<DirectionalParticleType> = of("squid_ink")
    @JvmField
    public val SWEEP_ATTACK: RegistryReference<SimpleParticleType> = of("sweep_attack")
    @JvmField
    public val TOTEM_OF_UNDYING: RegistryReference<DirectionalParticleType> = of("totem_of_undying")
    @JvmField
    public val UNDERWATER: RegistryReference<SimpleParticleType> = of("underwater")
    @JvmField
    public val SPLASH: RegistryReference<SimpleParticleType> = of("splash")
    @JvmField
    public val WITCH: RegistryReference<SimpleParticleType> = of("witch")
    @JvmField
    public val BUBBLE_POP: RegistryReference<DirectionalParticleType> = of("bubble_pop")
    @JvmField
    public val CURRENT_DOWN: RegistryReference<SimpleParticleType> = of("current_down")
    @JvmField
    public val BUBBLE_COLUMN_UP: RegistryReference<DirectionalParticleType> = of("bubble_column_up")
    @JvmField
    public val NAUTILUS: RegistryReference<DirectionalParticleType> = of("nautilus")
    @JvmField
    public val DOLPHIN: RegistryReference<SimpleParticleType> = of("dolphin")
    @JvmField
    public val CAMPFIRE_COSY_SMOKE: RegistryReference<DirectionalParticleType> = of("campfire_cosy_smoke")
    @JvmField
    public val CAMPFIRE_SIGNAL_SMOKE: RegistryReference<DirectionalParticleType> = of("campfire_signal_smoke")
    @JvmField
    public val DRIPPING_HONEY: RegistryReference<SimpleParticleType> = of("dripping_honey")
    @JvmField
    public val FALLING_HONEY: RegistryReference<SimpleParticleType> = of("falling_honey")
    @JvmField
    public val LANDING_HONEY: RegistryReference<SimpleParticleType> = of("landing_honey")
    @JvmField
    public val FALLING_NECTAR: RegistryReference<SimpleParticleType> = of("falling_nectar")
    @JvmField
    public val FALLING_SPORE_BLOSSOM: RegistryReference<SimpleParticleType> = of("falling_spore_blossom")
    @JvmField
    public val ASH: RegistryReference<SimpleParticleType> = of("ash")
    @JvmField
    public val CRIMSON_SPORE: RegistryReference<SimpleParticleType> = of("crimson_spore")
    @JvmField
    public val WARPED_SPORE: RegistryReference<SimpleParticleType> = of("warped_spore")
    @JvmField
    public val SPORE_BLOSSOM_AIR: RegistryReference<SimpleParticleType> = of("spore_blossom_air")
    @JvmField
    public val DRIPPING_OBSIDIAN_TEAR: RegistryReference<SimpleParticleType> = of("dripping_obsidian_tear")
    @JvmField
    public val FALLING_OBSIDIAN_TEAR: RegistryReference<SimpleParticleType> = of("falling_obsidian_tear")
    @JvmField
    public val LANDING_OBSIDIAN_TEAR: RegistryReference<SimpleParticleType> = of("landing_obsidian_tear")
    @JvmField
    public val REVERSE_PORTAL: RegistryReference<DirectionalParticleType> = of("reverse_portal")
    @JvmField
    public val WHITE_ASH: RegistryReference<SimpleParticleType> = of("white_ash")
    @JvmField
    public val SMALL_FLAME: RegistryReference<SimpleParticleType> = of("small_flame")
    @JvmField
    public val SNOWFLAKE: RegistryReference<SimpleParticleType> = of("snowflake")
    @JvmField
    public val DRIPPING_DRIPSTONE_LAVA: RegistryReference<SimpleParticleType> = of("dripping_dripstone_lava")
    @JvmField
    public val FALLING_DRIPSTONE_LAVA: RegistryReference<SimpleParticleType> = of("falling_dripstone_lava")
    @JvmField
    public val DRIPPING_DRIPSTONE_WATER: RegistryReference<SimpleParticleType> = of("dripping_dripstone_water")
    @JvmField
    public val FALLING_DRIPSTONE_WATER: RegistryReference<SimpleParticleType> = of("falling_dripstone_water")
    @JvmField
    public val GLOW_SQUID_INK: RegistryReference<SimpleParticleType> = of("glow_squid_ink")
    @JvmField
    public val GLOW: RegistryReference<SimpleParticleType> = of("glow")
    @JvmField
    public val WAX_ON: RegistryReference<SimpleParticleType> = of("wax_on")
    @JvmField
    public val WAX_OFF: RegistryReference<SimpleParticleType> = of("wax_off")
    @JvmField
    public val ELECTRIC_SPARK: RegistryReference<SimpleParticleType> = of("electric_spark")
    @JvmField
    public val SCRAPE: RegistryReference<SimpleParticleType> = of("scrape")

    // @formatter:on
    @JvmStatic
    private fun <T : ParticleType> of(name: String): RegistryReference<T> = RegistryReference.of(Registries.PARTICLE_TYPE, Key.key(name))
}
