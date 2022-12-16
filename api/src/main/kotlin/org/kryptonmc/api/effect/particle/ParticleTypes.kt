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
    public val AMBIENT_ENTITY_EFFECT: SimpleParticleType = get("ambient_entity_effect")
    @JvmField
    public val ANGRY_VILLAGER: SimpleParticleType = get("angry_villager")
    @JvmField
    public val BLOCK: BlockParticleType = get("block")
    @JvmField
    public val BLOCK_MARKER: BlockParticleType = get("block_marker")
    @JvmField
    public val BUBBLE: DirectionalParticleType = get("bubble")
    @JvmField
    public val CLOUD: DirectionalParticleType = get("cloud")
    @JvmField
    public val CRIT: DirectionalParticleType = get("crit")
    @JvmField
    public val DAMAGE_INDICATOR: DirectionalParticleType = get("damage_indicator")
    @JvmField
    public val DRAGON_BREATH: DirectionalParticleType = get("dragon_breath")
    @JvmField
    public val DRIPPING_LAVA: SimpleParticleType = get("dripping_lava")
    @JvmField
    public val FALLING_LAVA: SimpleParticleType = get("falling_lava")
    @JvmField
    public val LANDING_LAVA: SimpleParticleType = get("landing_lava")
    @JvmField
    public val DRIPPING_WATER: SimpleParticleType = get("dripping_water")
    @JvmField
    public val FALLING_WATER: SimpleParticleType = get("falling_water")
    @JvmField
    public val DUST: DustParticleType = get("dust")
    @JvmField
    public val DUST_COLOR_TRANSITION: DustTransitionParticleType = get("dust_color_transition")
    @JvmField
    public val EFFECT: SimpleParticleType = get("effect")
    @JvmField
    public val ELDER_GUARDIAN: SimpleParticleType = get("elder_guardian")
    @JvmField
    public val ENCHANTED_HIT: DirectionalParticleType = get("enchanted_hit")
    @JvmField
    public val ENCHANT: DirectionalParticleType = get("enchant")
    @JvmField
    public val END_ROD: DirectionalParticleType = get("end_rod")
    @JvmField
    public val ENTITY_EFFECT: ColorParticleType = get("entity_effect")
    @JvmField
    public val EXPLOSION_EMITTER: SimpleParticleType = get("explosion_emitter")
    @JvmField
    public val EXPLOSION: SimpleParticleType = get("explosion")
    @JvmField
    public val FALLING_DUST: BlockParticleType = get("falling_dust")
    @JvmField
    public val FIREWORK: DirectionalParticleType = get("firework")
    @JvmField
    public val FISHING: DirectionalParticleType = get("fishing")
    @JvmField
    public val FLAME: DirectionalParticleType = get("flame")
    @JvmField
    public val SOUL_FIRE_FLAME: DirectionalParticleType = get("soul_fire_flame")
    @JvmField
    public val SOUL: DirectionalParticleType = get("soul")
    @JvmField
    public val FLASH: SimpleParticleType = get("flash")
    @JvmField
    public val HAPPY_VILLAGER: SimpleParticleType = get("happy_villager")
    @JvmField
    public val COMPOSTER: SimpleParticleType = get("composter")
    @JvmField
    public val HEART: SimpleParticleType = get("heart")
    @JvmField
    public val INSTANT_EFFECT: SimpleParticleType = get("instant_effect")
    @JvmField
    public val ITEM: ItemParticleType = get("item")
    @JvmField
    public val VIBRATION: VibrationParticleType = get("vibration")
    @JvmField
    public val ITEM_SLIME: SimpleParticleType = get("item_slime")
    @JvmField
    public val ITEM_SNOWBALL: SimpleParticleType = get("item_snowball")
    @JvmField
    public val LARGE_SMOKE: DirectionalParticleType = get("large_smoke")
    @JvmField
    public val LAVA: SimpleParticleType = get("lava")
    @JvmField
    public val MYCELIUM: SimpleParticleType = get("mycelium")
    @JvmField
    public val NOTE: NoteParticleType = get("note")
    @JvmField
    public val POOF: DirectionalParticleType = get("poof")
    @JvmField
    public val PORTAL: DirectionalParticleType = get("portal")
    @JvmField
    public val RAIN: SimpleParticleType = get("rain")
    @JvmField
    public val SMOKE: DirectionalParticleType = get("smoke")
    @JvmField
    public val SNEEZE: DirectionalParticleType = get("sneeze")
    @JvmField
    public val SPIT: DirectionalParticleType = get("spit")
    @JvmField
    public val SQUID_INK: DirectionalParticleType = get("squid_ink")
    @JvmField
    public val SWEEP_ATTACK: SimpleParticleType = get("sweep_attack")
    @JvmField
    public val TOTEM_OF_UNDYING: DirectionalParticleType = get("totem_of_undying")
    @JvmField
    public val UNDERWATER: SimpleParticleType = get("underwater")
    @JvmField
    public val SPLASH: SimpleParticleType = get("splash")
    @JvmField
    public val WITCH: SimpleParticleType = get("witch")
    @JvmField
    public val BUBBLE_POP: DirectionalParticleType = get("bubble_pop")
    @JvmField
    public val CURRENT_DOWN: SimpleParticleType = get("current_down")
    @JvmField
    public val BUBBLE_COLUMN_UP: DirectionalParticleType = get("bubble_column_up")
    @JvmField
    public val NAUTILUS: DirectionalParticleType = get("nautilus")
    @JvmField
    public val DOLPHIN: SimpleParticleType = get("dolphin")
    @JvmField
    public val CAMPFIRE_COSY_SMOKE: DirectionalParticleType = get("campfire_cosy_smoke")
    @JvmField
    public val CAMPFIRE_SIGNAL_SMOKE: DirectionalParticleType = get("campfire_signal_smoke")
    @JvmField
    public val DRIPPING_HONEY: SimpleParticleType = get("dripping_honey")
    @JvmField
    public val FALLING_HONEY: SimpleParticleType = get("falling_honey")
    @JvmField
    public val LANDING_HONEY: SimpleParticleType = get("landing_honey")
    @JvmField
    public val FALLING_NECTAR: SimpleParticleType = get("falling_nectar")
    @JvmField
    public val FALLING_SPORE_BLOSSOM: SimpleParticleType = get("falling_spore_blossom")
    @JvmField
    public val ASH: SimpleParticleType = get("ash")
    @JvmField
    public val CRIMSON_SPORE: SimpleParticleType = get("crimson_spore")
    @JvmField
    public val WARPED_SPORE: SimpleParticleType = get("warped_spore")
    @JvmField
    public val SPORE_BLOSSOM_AIR: SimpleParticleType = get("spore_blossom_air")
    @JvmField
    public val DRIPPING_OBSIDIAN_TEAR: SimpleParticleType = get("dripping_obsidian_tear")
    @JvmField
    public val FALLING_OBSIDIAN_TEAR: SimpleParticleType = get("falling_obsidian_tear")
    @JvmField
    public val LANDING_OBSIDIAN_TEAR: SimpleParticleType = get("landing_obsidian_tear")
    @JvmField
    public val REVERSE_PORTAL: DirectionalParticleType = get("reverse_portal")
    @JvmField
    public val WHITE_ASH: SimpleParticleType = get("white_ash")
    @JvmField
    public val SMALL_FLAME: SimpleParticleType = get("small_flame")
    @JvmField
    public val SNOWFLAKE: SimpleParticleType = get("snowflake")
    @JvmField
    public val DRIPPING_DRIPSTONE_LAVA: SimpleParticleType = get("dripping_dripstone_lava")
    @JvmField
    public val FALLING_DRIPSTONE_LAVA: SimpleParticleType = get("falling_dripstone_lava")
    @JvmField
    public val DRIPPING_DRIPSTONE_WATER: SimpleParticleType = get("dripping_dripstone_water")
    @JvmField
    public val FALLING_DRIPSTONE_WATER: SimpleParticleType = get("falling_dripstone_water")
    @JvmField
    public val GLOW_SQUID_INK: SimpleParticleType = get("glow_squid_ink")
    @JvmField
    public val GLOW: SimpleParticleType = get("glow")
    @JvmField
    public val WAX_ON: SimpleParticleType = get("wax_on")
    @JvmField
    public val WAX_OFF: SimpleParticleType = get("wax_off")
    @JvmField
    public val ELECTRIC_SPARK: SimpleParticleType = get("electric_spark")
    @JvmField
    public val SCRAPE: SimpleParticleType = get("scrape")

    // @formatter:on
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T : ParticleType> get(name: String): T = Registries.PARTICLE_TYPE.get(Key.key(name)) as T
}
