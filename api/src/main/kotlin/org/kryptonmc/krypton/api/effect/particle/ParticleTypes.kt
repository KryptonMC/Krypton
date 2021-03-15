package org.kryptonmc.krypton.api.effect.particle

import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * @author Esophose
 */
interface Particle {

    /**
     * The namespaced key
     */
    val key: NamespacedKey

    /**
     * The internal ID
     */
    val id: Int
}

class SimpleParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: ParticleEffectBuilder get() = ParticleEffectBuilder(this)
}

class DirectionalParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: DirectionalParticleEffectBuilder get() = DirectionalParticleEffectBuilder(this)
}

class BlockParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: BlockParticleEffectBuilder get() = BlockParticleEffectBuilder(this)
}

class ItemParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: ItemParticleEffectBuilder get() = ItemParticleEffectBuilder(this)
}

class ColorParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: ColorParticleEffectBuilder get() = ColorParticleEffectBuilder(this)
}

class DustParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: DustParticleEffectBuilder get() = DustParticleEffectBuilder(this)
}

class NoteParticle internal constructor(override val key: NamespacedKey, override val id: Int) : Particle {
    val builder: NoteParticleEffectBuilder get() = NoteParticleEffectBuilder(this)
}

object ParticleType {

    private val _values = mutableListOf<Particle>()

    /**
     * An immutable list of all [ParticleType], ordered by ID
     */
    val values
        get() = _values.toList()

    val AMBIENT_ENTITY_EFFECT = simple("ambient_entity_effect")
    val ANGRY_VILLAGER = simple("angry_villager")
    val BARRIER = simple("barrier")
    val BLOCK = block("block")
    val BUBBLE = directional("bubble")
    val CLOUD = directional("cloud")
    val CRIT = directional("crit")
    val DAMAGE_INDICATOR = directional("damage_indicator")
    val DRAGON_BREATH = directional("dragon_breath")
    val DRIPPING_LAVA = simple("dripping_lava")
    val FALLING_LAVA = simple("falling_lava")
    val LANDING_LAVA = simple("landing_lava")
    val DRIPPING_WATER = simple("dripping_water")
    val FALLING_WATER = simple("falling_water")
    val DUST = dust("dust")
    val EFFECT = simple("effect")
    val ELDER_GUARDIAN = simple("elder_guardian")
    val ENCHANTED_HIT = directional("enchanted_hit")
    val ENCHANT = directional("enchant")
    val END_ROD = directional("end_rod")
    val ENTITY_EFFECT = color("entity_effect")
    val EXPLOSION_EMITTER = simple("explosion_emitter")
    val EXPLOSION = simple("explosion")
    val FALLING_DUST = block("falling_dust")
    val FIREWORK = directional("firework")
    val FISHING = directional("fishing")
    val FLAME = directional("flame")
    val SOUL_FIRE_FLAME = directional("soul_fire_flame")
    val SOUL = directional("soul")
    val FLASH = simple("flash")
    val HAPPY_VILLAGER = simple("happy_villager")
    val COMPOSTER = simple("composter")
    val HEART = simple("heart")
    val INSTANT_EFFECT = simple("instant_effect")
    val ITEM = item("item")
    val ITEM_SLIME = simple("item_slime")
    val ITEM_SNOWBALL = simple("item_snowball")
    val LARGE_SMOKE = directional("large_smoke")
    val LAVA = simple("lava")
    val MYCELIUM = simple("mycelium")
    val NOTE = note("note")
    val POOF = directional("poof")
    val PORTAL = directional("portal")
    val RAIN = simple("rain")
    val SMOKE = directional("smoke")
    val SNEEZE = directional("sneeze")
    val SPIT = directional("spit")
    val SQUID_INK = directional("squid_ink")
    val SWEEP_ATTACK = simple("sweep_attack")
    val TOTEM_OF_UNDYING = directional("totem_of_undying")
    val UNDERWATER = simple("underwater")
    val SPLASH = simple("splash")
    val WITCH = simple("witch")
    val BUBBLE_POP = directional("bubble_pop")
    val CURRENT_DOWN = simple("current_down")
    val BUBBLE_COLUMN_UP = directional("bubble_column_up")
    val NAUTILUS = directional("nautilus")
    val DOLPHIN = simple("dolphin")
    val CAMPFIRE_COSY_SMOKE = directional("campfire_cosy_smoke")
    val CAMPFIRE_SIGNAL_SMOKE = directional("campfire_signal_smoke")
    val DRIPPING_HONEY = simple("dripping_honey")
    val FALLING_HONEY = simple("falling_honey")
    val LANDING_HONEY = simple("landing_honey")
    val FALLING_NECTAR = simple("falling_nectar")
    val ASH = simple("ash")
    val CRIMSON_SPORE = simple("crimson_spore")
    val WARPED_SPORE = simple("warped_spore")
    val DRIPPING_OBSIDIAN_TEAR = simple("dripping_obsidian_tear")
    val FALLING_OBSIDIAN_TEAR = simple("falling_obsidian_tear")
    val LANDING_OBSIDIAN_TEAR = simple("landing_obsidian_tear")
    val REVERSE_PORTAL = directional("reverse_portal")
    val WHITE_ASH = simple("white_ash")

    private var id: Int = 0

    private fun simple(name: String) = add(SimpleParticle(NamespacedKey(value = name), id++))
    private fun directional(name: String) = add(DirectionalParticle(NamespacedKey(value = name), id++))
    private fun block(name: String) = add(BlockParticle(NamespacedKey(value = name), id++))
    private fun item(name: String) = add(ItemParticle(NamespacedKey(value = name), id++))
    private fun dust(name: String) = add(DustParticle(NamespacedKey(value = name), id++))
    private fun color(name: String) = add(ColorParticle(NamespacedKey(value = name), id++))
    private fun note(name: String) = add(NoteParticle(NamespacedKey(value = name), id++))

    private fun <T : Particle> add(particle: T): T {
        _values += particle
        return particle
    }
}
