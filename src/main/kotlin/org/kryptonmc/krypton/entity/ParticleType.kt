package org.kryptonmc.krypton.entity

import org.kryptonmc.krypton.registry.NamespacedKey

open class Particle(val type: ParticleType)

data class BlockParticle(val state: Int) : Particle(ParticleType.BLOCK)

data class DustParticle(
    val red: Float,
    val green: Float,
    val blue: Float,
    val scale: Float
) : Particle(ParticleType.DUST)

data class FallingDustParticle(val state: Int) : Particle(ParticleType.FALLING_DUST)

data class ItemParticle(val slot: Slot) : Particle(ParticleType.ITEM)

enum class ParticleType(val key: NamespacedKey, val id: Int) {

    AMBIENT_ENTITY_EFFECT(NamespacedKey(value = "ambient_entity_effect"), 0),
    ANGRY_VILLAGER(NamespacedKey(value = "angry_villager"), 1),
    BARRIER(NamespacedKey(value = "barrier"), 2),
    BLOCK(NamespacedKey(value = "block"), 3),
    BUBBLE(NamespacedKey(value = "bubble"), 4),
    CLOUD(NamespacedKey(value = "cloud"), 5),
    CRIT(NamespacedKey(value = "crit"), 6),
    DAMAGE_INDICATOR(NamespacedKey(value = "damage_indicator"), 7),
    DRAGON_BREATH(NamespacedKey(value = "dragon_breath"), 8),
    DRIPPING_LAVA(NamespacedKey(value = "dripping_lava"), 9),
    FALLING_LAVA(NamespacedKey(value = "falling_lava"), 10),
    LANDING_LAVA(NamespacedKey(value = "landing_lava"), 11),
    DRIPPING_WATER(NamespacedKey(value = "dripping_water"), 12),
    FALLING_WATER(NamespacedKey(value = "falling_water"), 13),
    DUST(NamespacedKey(value = "dust"), 14),
    EFFECT(NamespacedKey(value = "effect"), 15),
    ELDER_GUARDIAN(NamespacedKey(value = "elder_guardian"), 16),
    ENCHANTED_HIT(NamespacedKey(value = "enchanted_hit"), 17),
    ENCHANT(NamespacedKey(value = "enchant"), 18),
    END_ROD(NamespacedKey(value = "end_rod"), 19),
    ENTITY_EFFECT(NamespacedKey(value = "entity_effect"), 20),
    EXPLOSION_EMITTER(NamespacedKey(value = "explosion_emitter"), 21),
    EXPLOSION(NamespacedKey(value = "explosion"), 22),
    FALLING_DUST(NamespacedKey(value = "falling_dust"), 23),
    FIREWORK(NamespacedKey(value = "firework"), 24),
    FISHING(NamespacedKey(value = "fishing"), 25),
    FLAME(NamespacedKey(value = "flame"), 26),
    FLASH(NamespacedKey(value = "flash"), 27),
    HAPPY_VILLAGER(NamespacedKey(value = "happy_villager"), 28),
    COMPOSTER(NamespacedKey(value = "composter"), 29),
    HEART(NamespacedKey(value = "heart"), 30),
    INSTANT_EFFECT(NamespacedKey(value = "instant_effect"), 31),
    ITEM(NamespacedKey(value = "item"), 32),
    ITEM_SLIME(NamespacedKey(value = "item_slime"), 33),
    ITEM_SNOWBALL(NamespacedKey(value = "item_snowball"), 34),
    LARGE_SMOKE(NamespacedKey(value = "large_smoke"), 35),
    LAVA(NamespacedKey(value = "lava"), 36),
    MYCELIUM(NamespacedKey(value = "mycelium"), 37),
    NOTE(NamespacedKey(value = "note"), 38),
    POOF(NamespacedKey(value = "poof"), 39),
    PORTAL(NamespacedKey(value = "portal"), 40),
    RAIN(NamespacedKey(value = "rain"), 41),
    SMOKE(NamespacedKey(value = "smoke"), 42),
    SNEEZE(NamespacedKey(value = "sneeze"), 43),
    SPIT(NamespacedKey(value = "spit"), 44),
    SQUID_INK(NamespacedKey(value = "squid_ink"), 45),
    SWEEP_ATTACK(NamespacedKey(value = "sweep_attack"), 46),
    TOTEM_OF_UNDYING(NamespacedKey(value = "totem_of_undying"), 47),
    UNDERWATER(NamespacedKey(value = "underwater"), 48),
    SPLASH(NamespacedKey(value = "splash"), 49),
    WITCH(NamespacedKey(value = "witch"), 50),
    BUBBLE_POP(NamespacedKey(value = "bubble_pop"), 51),
    CURRENT_DOWN(NamespacedKey(value = "current_down"), 52),
    BUBBLE_COLUMN_UP(NamespacedKey(value = "bubble_column_up"), 53),
    NAUTILUS(NamespacedKey(value = "nautilus"), 54),
    DOLPHIN(NamespacedKey(value = "dolphin"), 55),
    CAMPFIRE_COSY_SMOKE(NamespacedKey(value = "campfire_cosy_smoke"), 56),
    CAMPFIRE_SIGNAL_SMOKE(NamespacedKey(value = "campfire_signal_smoke"), 57),
    DRIPPING_HONEY(NamespacedKey(value = "dripping_honey"), 58),
    FALLING_HONEY(NamespacedKey(value = "falling_honey"), 59),
    LANDING_HONEY(NamespacedKey(value = "landing_honey"), 60),
    FALLING_NECTAR(NamespacedKey(value = "falling_nectar"), 61)
}