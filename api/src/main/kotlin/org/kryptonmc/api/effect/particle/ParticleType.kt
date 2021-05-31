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

/**
 * Exposes all available particle types.
 *
 * See [here](https://minecraft.fandom.com/wiki/Particles) for more information on
 * all of these types.
 */
// TODO: Maybe use code generation for this?
@Suppress("unused")
object ParticleType : Iterable<Particle> {

    private val internalValues = mutableListOf<Particle>()

    /**
     * An immutable list of all [ParticleType], ordered by ID
     * This uses an [ImmutableList] to ensure that it cannot be casted to a mutable list and be mutated.
     *
     * Note: Avoid invoking this very often, as this will create a new copy of the internal list every time
     * it is invoked.
     */
    val values: List<Particle>
        @JvmStatic get() = ImmutableList.copyOf(internalValues)

    /**
     * An ambient effect. Used for beacon effects.
     */
    @JvmField
    val AMBIENT_ENTITY_EFFECT = simple("ambient_entity_effect")

    /**
     * This will be spawned when you attack a villager in a village.
     *
     * How dare you! Villagers have feelings to :(
     */
    @JvmField
    val ANGRY_VILLAGER = simple("angry_villager")

    /**
     * Outputted by barrier blocks.
     */
    @JvmField
    val BARRIER = simple("barrier")

    /**
     * Breaking blocks and armor stands as well as sprinting, iron golems walking, or falling will
     * spawn this effect.
     */
    @JvmField
    val BLOCK = block("block")

    /**
     * Entities in water, guardian laser beams and fishing will produce this.
     */
    @JvmField
    val BUBBLE = directional("bubble")

    /**
     * After an entity dies, this will be spawned.
     */
    @JvmField
    val CLOUD = directional("cloud")

    /**
     * Critical hits will spawn this.
     */
    @JvmField
    val CRIT = directional("crit")

    /**
     * Spawned when you perform a melee attack on a mob or player.
     */
    @JvmField
    val DAMAGE_INDICATOR = directional("damage_indicator")

    /**
     * The ender dragon's breath and dragon fireballs will spawn this.
     */
    @JvmField
    val DRAGON_BREATH = directional("dragon_breath")

    /**
     * Dripping lava through blocks where the drip hasn't fallen from the block yet.
     */
    @JvmField
    val DRIPPING_LAVA = simple("dripping_lava")

    /**
     * Dripping lava through blocks where the drip is falling towards the ground.
     */
    @JvmField
    val FALLING_LAVA = simple("falling_lava")

    /**
     * Dripping lava through blocks where the drip is on the ground.
     */
    @JvmField
    val LANDING_LAVA = simple("landing_lava")

    /**
     * Dripping water through blocks where the drip hasn't fallen from the block yet.
     */
    @JvmField
    val DRIPPING_WATER = simple("dripping_water")

    /**
     * Dripping water through blocks where the drip is falling towards the ground.
     */
    @JvmField
    val FALLING_WATER = simple("falling_water")

    /**
     * Redstone ore, powered redstone dust, redstone torches and powered redstone repeaters
     * will spawn this effect.
     */
    @JvmField
    val DUST = dust("dust")

    /**
     * Splash and lingering potions, bottles o' enchanting and evokers will spawn this effect.
     */
    @JvmField
    val EFFECT = simple("effect")

    /**
     * This spawns when the elder guardian gives you mining fatigue.
     */
    @JvmField
    val ELDER_GUARDIAN = simple("elder_guardian")

    /**
     * Attacking with a sword or axe enchanted with sharpness, smite, or bane of arthropods
     * will spawn this effect.
     */
    @JvmField
    val ENCHANTED_HIT = directional("enchanted_hit")

    /**
     * Spawns from bookshelves near an enchanting table.
     */
    @JvmField
    val ENCHANT = directional("enchant")

    /**
     * End rods and shulker bullets spawn this effect.
     */
    @JvmField
    val END_ROD = directional("end_rod")

    /**
     * Status effects, lingering potions, tipped arrows, trading, and withered armor
     * spawn this effect.
     */
    @JvmField
    val ENTITY_EFFECT = color("entity_effect")

    /**
     * Explosions, and the death of the ender dragon will spawn this effect.
     */
    @JvmField
    val EXPLOSION_EMITTER = simple("explosion_emitter")

    /**
     * Explosions, ghast fireballs, wither skulls, the death of the ender dragon and
     * shearing mooshrooms will spawn this effect.
     */
    @JvmField
    val EXPLOSION = simple("explosion")

    /**
     * Floating sand, gravel, concrete powder, and anvils will spawn this effect underneath them.
     *
     * How did we accomplish this in the first place? No idea.
     */
    @JvmField
    val FALLING_DUST = block("falling_dust")

    /**
     * Spawns from firework rocket trails and explosions, and when dolphins track shipwrecks and underwater
     * ruins.
     */
    @JvmField
    val FIREWORK = directional("firework")

    /**
     * Spawns from fishing.
     */
    @JvmField
    val FISHING = directional("fishing")

    /**
     * Torches, furnaces, magma cubes and spawners spawn this effect.
     */
    @JvmField
    val FLAME = directional("flame")

    /**
     * The soul torch equivalent of [FLAME].
     */
    @JvmField
    val SOUL_FIRE_FLAME = directional("soul_fire_flame")

    /**
     * Appears when walking on soul sand or soul soil with the soul speed enchantment.
     */
    @JvmField
    val SOUL = directional("soul")

    /**
     * The flash of light when firework rockets explode.
     */
    @JvmField
    val FLASH = simple("flash")

    /**
     * Bone mealing a crop, trading with villagers, feeding baby animals, and walking or jumping on
     * turtle eggs will spawn this effect.
     */
    @JvmField
    val HAPPY_VILLAGER = simple("happy_villager")

    /**
     * Spawns when you fill a composter.
     *
     * Giving back to the environment! Good on you!
     */
    @JvmField
    val COMPOSTER = simple("composter")

    /**
     * Spawns from breeding and taming animals.
     */
    @JvmField
    val HEART = simple("heart")

    /**
     * Spawns from instant health/damage splashes, lingering potions, and spectral arrows.
     */
    @JvmField
    val INSTANT_EFFECT = simple("instant_effect")

    /**
     * Eating, throwing eggs, splash potions, and eyes of ender, and breaking tools will spawn
     * this effect.
     */
    @JvmField
    val ITEM = item("item")

    /**
     * Spawns when a slime jumps.
     *
     * _I believe I can fly!_
     */
    @JvmField
    val ITEM_SLIME = simple("item_slime")

    /**
     * Spawns when throwing snowballs, creating withers, and creating iron golems.
     */
    @JvmField
    val ITEM_SNOWBALL = simple("item_snowball")

    /**
     * Fire, minecarts with furnaces, blazes, water flowing into lava, and lava flowing into water
     * will spawn this effect.
     */
    @JvmField
    val LARGE_SMOKE = directional("large_smoke")

    /**
     * Spawns from lava bubbles.
     */
    @JvmField
    val LAVA = simple("lava")

    /**
     * Mycelium blocks spawn this effect.
     */
    @JvmField
    val MYCELIUM = simple("mycelium")

    /**
     * Emitted from note blocks.
     */
    @JvmField
    val NOTE = note("note")

    /**
     * Explosions, the death of mobs, mobs spawned from a spawner, and silverfish infesting blocks
     * spawn this effect.
     *
     * _Poof!_
     */
    @JvmField
    val POOF = directional("poof")

    /**
     * Nether portals, endermen, endermites, ender pearls, eyes of ender, ender chests,
     * dragon eggs, teleporting from eating chorus fruits, and end gateway portals spawn
     * this effect.
     *
     * _This looks intersting! I wonder where it goes..._
     */
    @JvmField
    val PORTAL = directional("portal")

    /**
     * Spawns when it's raining.
     */
    @JvmField
    val RAIN = simple("rain")

    /**
     * Torches, primed TNT, droppers, dispensers, end portals, brewing stands, spawners, furnaces,
     * ghast fireballs, wither skulls, taming, withers, lava (when raining), placing an eye of ender
     * in an end portal frame, redstone torches burning out, and food items on a campfire will spawn
     * this effect.
     */
    @JvmField
    val SMOKE = directional("smoke")

    /**
     * Spawns when a baby panda sneezes.
     *
     * _Aww! How cute!_
     */
    @JvmField
    val SNEEZE = directional("sneeze")

    /**
     * Spawns when a player or mob gets spat at by a llama.
     */
    @JvmField
    val SPIT = directional("spit")

    /**
     * Produced by a squid when attacked.
     */
    @JvmField
    val SQUID_INK = directional("squid_ink")

    /**
     * Spawns when you perform a sweep attack with a sword.
     */
    @JvmField
    val SWEEP_ATTACK = simple("sweep_attack")

    /**
     * Spawns when you activate your totem of undying.
     *
     * _I'm invincible!_
     */
    @JvmField
    val TOTEM_OF_UNDYING = directional("totem_of_undying")

    /**
     * Seen whilst swimming underwater.
     */
    @JvmField
    val UNDERWATER = simple("underwater")

    /**
     * Spawns from entities in water, wolves shaking off after swimming, and boats.
     */
    @JvmField
    val SPLASH = simple("splash")

    /**
     * Spawns from witches.
     */
    @JvmField
    val WITCH = simple("witch")

    /**
     * Unused :(
     *
     * See [MC-132067](https://bugs.mojang.com/browse/MC-132067)
     */
    @JvmField
    val BUBBLE_POP = directional("bubble_pop")

    /**
     * Spawned from bubble column whirlpools made by magma blocks underwater.
     */
    @JvmField
    val CURRENT_DOWN = simple("current_down")

    /**
     * Spawned from upward bubble columns made by soul sand under water.
     */
    @JvmField
    val BUBBLE_COLUMN_UP = directional("bubble_column_up")

    /**
     * Spawns from activated conduits.
     *
     * _I am aquaman!_
     */
    @JvmField
    val NAUTILUS = directional("nautilus")

    /**
     * Trails behind swimming dolphins.
     */
    @JvmField
    val DOLPHIN = simple("dolphin")

    /**
     * Smoke produced by campfires and soul campfires.
     */
    @JvmField
    val CAMPFIRE_COSY_SMOKE = directional("campfire_cosy_smoke")

    /**
     * Smoke produced by campfires and soul campfires when above a hay bale.
     */
    @JvmField
    val CAMPFIRE_SIGNAL_SMOKE = directional("campfire_signal_smoke")

    /**
     * Dripping honey through blocks where the drip hasn't fallen from the block yet.
     */
    @JvmField
    val DRIPPING_HONEY = simple("dripping_honey")

    /**
     * Dripping honey through blocks where the drip is falling towards the ground.
     */
    @JvmField
    val FALLING_HONEY = simple("falling_honey")

    /**
     * Dripping honey through blocks where the drip is on the ground.
     */
    @JvmField
    val LANDING_HONEY = simple("landing_honey")

    /**
     * Spawns from the nectar on pollen-loaded bees.
     */
    @JvmField
    val FALLING_NECTAR = simple("falling_nectar")

    /**
     * Naturally generated in the soul sand valley biome.
     */
    @JvmField
    val ASH = simple("ash")

    /**
     * A crimson particle generated in crimson forest biomes.
     */
    @JvmField
    val CRIMSON_SPORE = simple("crimson_spore")

    /**
     * A warped particle generated in warped forest biomes.
     */
    @JvmField
    val WARPED_SPORE = simple("warped_spore")

    /**
     * Obsidian tears dripping through blocks where the drip hasn't fallen from the block yet.
     */
    @JvmField
    val DRIPPING_OBSIDIAN_TEAR = simple("dripping_obsidian_tear")

    /**
     * Obsidian tears dripping through blocks where the drip is falling towards the ground.
     */
    @JvmField
    val FALLING_OBSIDIAN_TEAR = simple("falling_obsidian_tear")

    /**
     * Obsidian tears dripping through blocks where the drip is on the ground.
     */
    @JvmField
    val LANDING_OBSIDIAN_TEAR = simple("landing_obsidian_tear")

    /**
     * No documentation on wiki.vg or the Minecraft wiki for this :(
     */
    @JvmField
    val REVERSE_PORTAL = directional("reverse_portal")

    /**
     * Naturally generated in basalt deltas biomes.
     */
    @JvmField
    val WHITE_ASH = simple("white_ash")

    private var id: Int = 0

    @JvmStatic
    private fun simple(name: String) = add(SimpleParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun directional(name: String) = add(DirectionalParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun block(name: String) = add(BlockParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun item(name: String) = add(ItemParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun dust(name: String) = add(DustParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun color(name: String) = add(ColorParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun note(name: String) = add(NoteParticle(Key.key(Key.MINECRAFT_NAMESPACE, name), id++))

    @JvmStatic
    private fun <T : Particle> add(particle: T): T {
        internalValues += particle
        return particle
    }

    override fun iterator() = internalValues.iterator()
}
